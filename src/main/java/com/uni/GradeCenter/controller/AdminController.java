package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.AvailableSlotDTO;
import com.uni.GradeCenter.model.dto.ClassroomDTO;
import com.uni.GradeCenter.model.dto.SchoolDTO;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateScheduleBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateSchoolBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateSubjectBindingDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.ClassroomViewDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.SubjectViewDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.service.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final SchoolService schoolService;
    private final StudentService studentService;
    private final ParentService parentService;
    private final ClassroomService classroomService;
    private final TeacherService teacherService;
    private final ScheduleService scheduleService;
    private final SubjectService subjectService;

    public AdminController(UserService userService, SchoolService schoolService, StudentService studentService,
                           ParentService parentService, ClassroomService classroomService, TeacherService teacherService,
                           ScheduleService scheduleService, SubjectService subjectService) {
        this.userService = userService;
        this.schoolService = schoolService;
        this.studentService = studentService;
        this.parentService = parentService;
        this.classroomService = classroomService;
        this.teacherService = teacherService;
        this.scheduleService = scheduleService;
        this.subjectService = subjectService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin-users";
    }


    @PostMapping("/users/edit/{id}")
    public String editUser(
            @PathVariable Long id,
            @ModelAttribute("userDTO") UserDTO userDTO,
            RedirectAttributes redirectAttributes
    ) {
        userService.updateUserFromDTO(id, userDTO);

        redirectAttributes.addFlashAttribute("successMessage", "Промяната е направена успешно!");

        return "redirect:/admin/users";
    }

    @GetMapping("/schools")
    public String schools(Model model) {
        List<SchoolDTO> schools = schoolService.getAllSchoolDTOs();
        List<User> allDirectors = userService.getUsersByRole(Role.DIRECTOR);


        Set<Long> assignedDirectorIds = schools.stream()
                .map(SchoolDTO::getDirectorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        List<User> unassignedDirectors = allDirectors.stream()
                .filter(director -> !assignedDirectorIds.contains(director.getId()))
                .collect(Collectors.toList());

        model.addAttribute("schools", schools);
        model.addAttribute("allDirectors", allDirectors);
        model.addAttribute("unassignedDirectors", unassignedDirectors);

        return "admin-schools";
    }



    @PostMapping("/schools/edit/{id}")
    public String editSchool(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam(required = false) Long directorId,
            RedirectAttributes redirectAttributes
    ) {
        School school = schoolService.getSchoolById(id);
        school.setName(name);
        school.setAddress(address);

        if (directorId != null) {
            User director = userService.getUserById(directorId);
            school.setDirector(director);
        } else {
            school.setDirector(null);
        }

        schoolService.updateSchool(school);

        redirectAttributes.addFlashAttribute("successMessage", "Промяната е направена успешно!");

        return "redirect:/admin/schools";
    }


    @GetMapping("/schools/create")
    public String createSchoolForm(Model model) {
        if (!model.containsAttribute("createSchoolBindingDTO")) {
            model.addAttribute("createSchoolBindingDTO", new CreateSchoolBindingDTO());
        }
        List<User> unassignedDirectors = userService.getUnassignedDirectors();

        model.addAttribute("directors", unassignedDirectors);

        return "admin-schools-create";
    }

    @PostMapping("/schools/create")
    public String createSchool(
            @Valid @ModelAttribute("createSchoolBindingDTO") CreateSchoolBindingDTO createSchoolBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createSchoolBindingDTO", createSchoolBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createSchoolBindingDTO", bindingResult);
            return "redirect:/admin/schools/create";
        }

        User director = userService.getUserById(Long.parseLong(createSchoolBindingDTO.getDirector()));

        School school = new School();
        school.setName(createSchoolBindingDTO.getName());
        school.setAddress(createSchoolBindingDTO.getAddress());
        school.setDirector(director);
        school.setClassrooms(new ArrayList<>());
        school.setStudents(new ArrayList<>());
        school.setTeachers(new ArrayList<>());

        schoolService.saveSchool(school);

        redirectAttributes.addFlashAttribute("successMessage", "Училището беше създадено успешно!");
        return "redirect:/admin/schools";
    }

    @GetMapping("/students")
    public String students(Model model) {
        List<Long> userIds = userService.getUsersByRole(Role.STUDENT)
                .stream()
                .map(User::getId)
                .toList();

        List<Student> studentsByUsers = studentService.getStudentsByUserIds(userIds);

        model.addAttribute("students", studentsByUsers);
        model.addAttribute("schools", schoolService.getAllSchools());
        model.addAttribute("classrooms", classroomService.getAllClassrooms());
        return "admin-students";
    }

    @PostMapping("/students/edit/{id}")
    public String editStudent(
            @PathVariable Long id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam Long schoolId,
            @RequestParam(required = false) Long classroomId,
            RedirectAttributes redirectAttributes
    ) {
        studentService.updateStudentInline(id, firstName, lastName, email, username, schoolId, classroomId);
        redirectAttributes.addFlashAttribute("successMessage", "Ученикът е обновен успешно.");
        return "redirect:/admin/students";
    }


    @GetMapping("/parents")
    public String parents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("parents", parentService.getAllParents());
        return "admin-parents";
    }

    @PostMapping("/parents/edit/{id}")
    public String updateParent(@PathVariable Long id,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam(required = false) Long childId,
                               RedirectAttributes redirectAttributes) {

        parentService.updateParentInline(id, firstName, lastName, email, childId);

        redirectAttributes.addFlashAttribute("successMessage", "Родителят е обновен успешно.");
        return "redirect:/admin/parents";
    }


    @GetMapping("/teachers")
    public String teachers(@RequestParam(required = false) Long editId, Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);

        if (editId != null) {
            Teacher teacher = teacherService.getTeacherById(editId);
            List<School> schools = schoolService.getAllSchools();

            List<Subject> subjects = new ArrayList<>();
            if (teacher.getSchool() != null) {
                subjects = subjectService.getAllSubjectsBySchoolId(teacher.getSchool().getId());
            }

            model.addAttribute("teacherToEdit", teacher);
            model.addAttribute("schools", schools);
            model.addAttribute("subjects", subjects);
        }

        return "admin-teachers";
    }


    @PostMapping("/teachers/{id}")
    public String updateTeacher(
            @PathVariable Long id,
            @RequestParam Long schoolId,
            @RequestParam(required = false, name = "qualifiedSubjects") List<Long> qualifiedSubjectsIds,
            RedirectAttributes redirectAttributes
    ) {
        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher.getSchool() == null) {
            teacher.setSchool(schoolService.getSchoolById(schoolId));
        }
        if (teacher.getSchool().getId() != schoolId) {
            teacher.setQualifiedSubjects(new ArrayList<>());
        }
        School school = schoolService.getSchoolById(schoolId);
        teacher.setSchool(school);

        if (qualifiedSubjectsIds != null && !qualifiedSubjectsIds.isEmpty() && teacher.getSchool().getId() == schoolId) {
            List<Subject> subjects = subjectService.getAllSubjectsByIds(qualifiedSubjectsIds);
            teacher.setQualifiedSubjects(subjects);
        } else {
            teacher.setQualifiedSubjects(new ArrayList<>());
        }

        teacherService.updateTeacher(teacher);

        redirectAttributes.addFlashAttribute("successMessage", "Промяната е направена успешно!");
        return "redirect:/admin/teachers";
    }

    @GetMapping("schools/classrooms/create")
    public String createClassroomForm(Model model) {
        model.addAttribute("classroom", new ClassroomDTO());
        model.addAttribute("schools", schoolService.getAllSchools());
        return "admin-schools-classroom-create";
    }

    @PostMapping("/schools/classrooms/create")
    public String createClassroom(
            @Valid @ModelAttribute("classroom") ClassroomDTO classroomDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("schools", schoolService.getAllSchools());
            return "admin-schools-classroom-create";
        }

        School school = schoolService.getSchoolById(classroomDTO.getSchoolId());
        if (classroomService.checkAvailability(classroomDTO, school)) {
            model.addAttribute("takenMessage", "Паралелка с това име съществува!");
            model.addAttribute("schools", schoolService.getAllSchools());
            return "admin-schools-classroom-create";
        }

        Classroom classroom = new Classroom();
        classroom.setName(classroomDTO.getName());
        classroom.setGrade(classroomDTO.getGrade());
        classroom.setSchool(school);

        classroomService.createClassroom(classroom);

        redirectAttributes.addFlashAttribute("successMessage", "Паралелката е създадена успешно.");
        return "redirect:/admin/schools";
    }



    @GetMapping("/subjects/create")
    public String createSubjectForm(Model model) {
        if (!model.containsAttribute("createSubjectBindingDTO")) {
            model.addAttribute("createSubjectBindingDTO", new CreateSubjectBindingDTO());
        }
        List<School> schools = schoolService.getAllSchools();

        model.addAttribute("schools", schools);

        return "admin-subject-create";
    }

    @PostMapping("/subjects/create")
    public String createSubject(
            @Valid @ModelAttribute("createSubjectBindingDTO") CreateSubjectBindingDTO createSubjectBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createSubjectBindingDTO", createSubjectBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createSubjectBindingDTO",
                    bindingResult);
            return "redirect:/admin/subjects/create";
        }

        Optional<School> bySchoolName = schoolService.findBySchoolName(createSubjectBindingDTO.getSchool());

        if (bySchoolName.isPresent()) {
            for (Subject subject : bySchoolName.get().getSubjects()) {
                if (subject.getName().equals(createSubjectBindingDTO.getName())) {
                    bindingResult.rejectValue("name", "subject.exists", "Този предмет вече съществува в избраното училище.");
                    redirectAttributes.addFlashAttribute("createSubjectBindingDTO", createSubjectBindingDTO);
                    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createSubjectBindingDTO", bindingResult);
                    return "redirect:/admin/subjects/create";
                }
            }

            Subject subject = new Subject();
            subject.setName(createSubjectBindingDTO.getName());
            subject.setSchool(bySchoolName.get());

            subject = subjectService.createSubject(subject);

            bySchoolName.get().getSubjects().add(subject);
            schoolService.saveSchool(bySchoolName.get());
        }

        redirectAttributes.addFlashAttribute("successMessage", "Предметът беше създаден успешно!");
        return "redirect:/admin/schools";
    }

    @GetMapping("/schedules")
    public String schedules(Model model) {
        List<ClassroomViewDTO> classrooms = classroomService.getAllClassroomViewDTOs();

        model.addAttribute("classroomViews", classrooms);

        return "admin-create-schedules";
    }

    @GetMapping("/schedules/available-times")
    @ResponseBody
    public List<Map<String, String>> getAvailableStartTimes(
            @RequestParam Long classroomId,
            @RequestParam Long subjectId,
            @RequestParam DayOfWeek dayOfWeek) {

        Classroom classroom = classroomService.getClassroomById(classroomId);
        Subject subject = subjectService.getSubjectById(subjectId);

        List<AvailableSlotDTO> slots = scheduleService.getAvailableStartTimesWithTeachers(classroom, subject, dayOfWeek);

        return slots.stream()
                .map(slot -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("time", slot.getTime().toString());
                    map.put("teacher", slot.getTeacherName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/schedules/create")
    public String schedulesCreate(@RequestParam("classroomId") Long classroomId, Model model) {
        Classroom classroom = classroomService.getClassroomById(classroomId);

        List<Subject> subjects = subjectService.getSubjectsBySchool(classroom.getSchool());
        List<LocalTime> dailyLessonSlots = scheduleService.getDailyLessonSlots();

        CreateScheduleBindingDTO dto = new CreateScheduleBindingDTO();
        dto.setClassroomId(classroomId);

        model.addAttribute("classroom", classroom);
        model.addAttribute("subjects", subjects);
        model.addAttribute("availableStartTimes", dailyLessonSlots);
        model.addAttribute("createScheduleBindingDTO", dto);

        return "admin-create-schedule";
    }

    @PostMapping("/schedules/create")
    public String createSchedule(@ModelAttribute("createScheduleBindingDTO") CreateScheduleBindingDTO createScheduleBindingDTO,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("createScheduleBindingDTO", createScheduleBindingDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createScheduleBindingDTO", bindingResult);
                return "redirect:/admin/schedules/create?classroomId=" + createScheduleBindingDTO.getClassroomId();
            }

            scheduleService.createScheduleFrontend(createScheduleBindingDTO);

            redirectAttributes.addFlashAttribute("successMessage", "Часът беше успешно създаден.");
            return "redirect:/admin/schedules";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No teacher found for this time period");
            return "redirect:/admin/schedules/create?classroomId=" + createScheduleBindingDTO.getClassroomId();
        }
    }

    @GetMapping("/statistics")
    public String showSchoolsInfo(Model model) {
        List<School> schools = schoolService.getAllSchools();
        Map<Long, List<SubjectViewDTO>> schoolStats = schoolService.getSchoolStatistics();
        model.addAttribute("schools", schools);
        model.addAttribute("schoolStats", schoolStats);
        return "admin-statistics";
    }

}