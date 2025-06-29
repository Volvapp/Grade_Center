package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.dto.bindingDTOs.CreateGradeBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.EditGradeBindingDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.GradeViewDTO;
import com.uni.GradeCenter.service.GradeService;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.TeacherService;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final ScheduleService scheduleService;
    private final GradeService gradeService;

    public TeacherController(TeacherService teacherService, ScheduleService scheduleService, GradeService gradeService) {
        this.teacherService = teacherService;
        this.scheduleService = scheduleService;
        this.gradeService = gradeService;
    }

    @GetMapping("/grades")
    public String grades(Model model, Principal principal) {
        Teacher teacher = teacherService.findByUsername(principal.getName());

        List<GradeEntryViewDTO> gradeEntries = new ArrayList<>();

        for (Subject subject : teacher.getQualifiedSubjects()) {
            List<Schedule> schedules = scheduleService.findByTeacherAndSubject(teacher, subject);
            for (Schedule schedule : schedules) {
                Classroom classroom = schedule.getClassroom();
                for (Student student : classroom.getStudents()) {
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
}
