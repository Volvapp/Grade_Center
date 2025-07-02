package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.dto.bindingDTOs.AbsenceCreateBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateGradeBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.EditGradeBindingDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.AbsenceEntryViewDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.GradeViewDTO;
import com.uni.GradeCenter.service.*;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.viewDTOs.GradeEntryViewDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final ScheduleService scheduleService;
    private final GradeService gradeService;
    private final AbsenceService absenceService;
    private final UserService userService;

    public TeacherController(TeacherService teacherService, ScheduleService scheduleService, GradeService gradeService, AbsenceService absenceService, UserService userService) {
        this.teacherService = teacherService;
        this.scheduleService = scheduleService;
        this.gradeService = gradeService;
        this.absenceService = absenceService;
        this.userService = userService;
    }

    @GetMapping("/grades")
    public String grades(Model model, Principal principal) {
        Teacher teacher = teacherService.findByUsername(principal.getName());
        List<GradeEntryViewDTO> gradeEntries = new ArrayList<>();

        // Използваме Set, за да пазим уникални комбинации "studentId:subjectId"
        Set<String> seenStudentSubjectPairs = new HashSet<>();

        for (Subject subject : teacher.getQualifiedSubjects()) {
            List<Schedule> schedules = scheduleService.findByTeacherAndSubject(teacher, subject);
            for (Schedule schedule : schedules) {
                Classroom classroom = schedule.getClassroom();
                for (Student student : classroom.getStudents()) {
                    String uniqueKey = student.getId() + ":" + subject.getId();
                    if (seenStudentSubjectPairs.contains(uniqueKey)) {
                        continue; // вече сме добавили тази комбинация
                    }
                    seenStudentSubjectPairs.add(uniqueKey);

                    GradeEntryViewDTO dto = new GradeEntryViewDTO();
                    dto.setSchoolName(classroom.getSchool().getName());
                    dto.setClassroomName(classroom.getName());
                    dto.setClassroomId(classroom.getId());
                    dto.setStudentFullName(student.getUser().getFirstName() + " " + student.getUser().getLastName());
                    dto.setStudentId(student.getId());
                    dto.setSubjectName(subject.getName());
                    dto.setSubjectId(subject.getId());

                    List<Grade> studentGrades = gradeService.findByStudentAndSubject(student, subject);
                    List<GradeViewDTO> gradeDTOs = studentGrades.stream()
                            .map(g -> new GradeViewDTO(g.getId(), g.getValue()))
                            .collect(Collectors.toList());

                    dto.setGrades(gradeDTOs);
                    gradeEntries.add(dto);
                }
            }
        }

        model.addAttribute("gradeEntries", gradeEntries);
        return "teacher-grades";
    }

    @GetMapping("/grade/create")
    public String showGradeForm(@RequestParam Long classroomId,
                                @RequestParam String classroomName,
                                @RequestParam Long studentId,
                                @RequestParam String studentName,
                                @RequestParam Long subjectId,
                                @RequestParam String subjectName,
                                Model model) {
        CreateGradeBindingDTO dto = new CreateGradeBindingDTO();
        dto.setClassroomId(classroomId);
        dto.setClassroomName(classroomName);
        dto.setStudentId(studentId);
        dto.setStudentName(studentName);
        dto.setSubjectId(subjectId);
        dto.setSubjectName(subjectName);
        model.addAttribute("createGradeBindingDTO", dto);
        return "teacher-grade-create";
    }

    @PostMapping("/grade/create")
    public String submitGrade(@Valid @ModelAttribute("createGradeBindingDTO") CreateGradeBindingDTO dto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createGradeBindingDTO", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createGradeBindingDTO", bindingResult);
            return "redirect:/teacher/grade/create?classroomId=" + dto.getClassroomId()
                    + "&classroomName=" + dto.getClassroomName()
                    + "&studentId=" + dto.getStudentId()
                    + "&studentName=" + dto.getStudentName()
                    + "&subjectId=" + dto.getSubjectId()
                    + "&subjectName=" + dto.getSubjectName();
        }

        gradeService.createGradeFrontend(dto, principal.getName());

        redirectAttributes.addFlashAttribute("successMessage", "Оценката беше записана успешно.");
        return "redirect:/teacher/grades";
    }

    @DeleteMapping("/grade/delete")
    public String deleteGrade(@RequestParam Long gradeId) {
        gradeService.deleteGrade(gradeId);
        return "redirect:/teacher/grades";
    }

    @GetMapping("/grade/edit")
    public String showEditGradeForm(@RequestParam Long gradeId, Model model) {
        Grade grade = gradeService.getGradeById(gradeId);

        EditGradeBindingDTO dto = new EditGradeBindingDTO();
        dto.setGradeId(grade.getId());
        dto.setValue(grade.getValue());

        model.addAttribute("editGradeBindingDTO", dto);
        return "teacher-grade-edit";
    }

    @PutMapping("/grade/edit")
    public String editGrade(@ModelAttribute("editGradeBindingDTO") @Valid EditGradeBindingDTO dto,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/teacher/grade/edit?gradeId=" + dto.getGradeId();
        }

        gradeService.updateGradeFrontend(dto);
        return "redirect:/teacher/grades";
    }

    @GetMapping("/absences")
    public String absences(Model model, Principal principal) {
        Teacher teacher = teacherService.findByUsername(principal.getName());

        List<AbsenceEntryViewDTO> absenceEntries = new ArrayList<>();
        Set<String> processed = new HashSet<>();

        for (Subject subject : teacher.getQualifiedSubjects()) {
            List<Schedule> schedules = scheduleService.findByTeacherAndSubject(teacher, subject);

            for (Schedule schedule : schedules) {
                Classroom classroom = schedule.getClassroom();

                for (Student student : classroom.getStudents()) {
                    String uniqueKey = student.getId() + "-" + subject.getId() + "-" + classroom.getId();

                    if (processed.contains(uniqueKey)) {
                        continue;
                    }
                    processed.add(uniqueKey);

                    AbsenceEntryViewDTO dto = new AbsenceEntryViewDTO();
                    dto.setSchoolName(classroom.getSchool().getName());
                    dto.setClassroomName(classroom.getName());
                    dto.setClassroomId(classroom.getId());
                    dto.setStudentFullName(student.getUser().getFirstName() + " " + student.getUser().getLastName());
                    dto.setStudentId(student.getId());
                    dto.setSubjectName(subject.getName());
                    dto.setSubjectId(subject.getId());

                    Absence absence = absenceService.findByStudentAndSubject(student, subject);

                    if (absence != null) {
                        dto.setAbsenceDate(absence.getDate());
                    } else {
                        dto.setAbsenceDate(null);
                    }

                    absenceEntries.add(dto);
                }
            }
        }

        model.addAttribute("absenceEntries", absenceEntries);

        return "teacher-absences";
    }

    @GetMapping("/absence/create")
    public String showAbsenceForm(@RequestParam Long classroomId,
                                  @RequestParam String classroomName,
                                  @RequestParam Long studentId,
                                  @RequestParam String studentName,
                                  @RequestParam Long subjectId,
                                  @RequestParam String subjectName,
                                  Model model) {
        AbsenceCreateBindingDTO dto = new AbsenceCreateBindingDTO();
        dto.setClassroomId(classroomId);
        dto.setClassroomName(classroomName);
        dto.setStudentId(studentId);
        dto.setStudentName(studentName);
        dto.setSubjectId(subjectId);
        dto.setSubjectName(subjectName);
        model.addAttribute("createAbsenceBindingDTO", dto);

        return "teacher-absence-create";
    }

    @PostMapping("/absence/create")
    public String submitAbsence(@Valid @ModelAttribute("createAbsenceBindingDTO") AbsenceCreateBindingDTO dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createAbsenceBindingDTO", dto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createAbsenceBindingDTO", bindingResult);
            return "redirect:/teacher/absence/create?classroomId=" + dto.getClassroomId()
                    + "&classroomName=" + dto.getClassroomName()
                    + "&studentId=" + dto.getStudentId()
                    + "&studentName=" + dto.getStudentName()
                    + "&subjectId=" + dto.getSubjectId()
                    + "&subjectName=" + dto.getSubjectName();
        }

        absenceService.createAbsenceFrontend(dto, principal.getName());

        redirectAttributes.addFlashAttribute("successMessage", "Отсъствието беше записано успешно.");
        return "redirect:/teacher/absences";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Teacher teacher = teacherService.getTeacherByUserId(user.getId());

        model.addAttribute("schedules", scheduleService.mapToView(teacher.getSchedules()));

        return "teacher-schedule";
    }
}
