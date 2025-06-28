package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.ClassroomDTO;
import com.uni.GradeCenter.model.dto.ParentDTO;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateSchoolBindingDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.service.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private final ModelMapper modelMapper;

    public AdminController(UserService userService, SchoolService schoolService, StudentService studentService, ParentService parentService, ClassroomService classroomService, TeacherService teacherService, ScheduleService scheduleService, SubjectService subjectService, ModelMapper modelMapper) {
        this.userService = userService;
        this.schoolService = schoolService;
        this.studentService = studentService;
        this.parentService = parentService;
        this.classroomService = classroomService;
        this.teacherService = teacherService;
        this.scheduleService = scheduleService;
        this.subjectService = subjectService;
        this.modelMapper = modelMapper;
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
        model.addAttribute("schools", schoolService.getAllSchoolDTOs());
        model.addAttribute("directors", userService.getUsersByRole(Role.DIRECTOR));
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
        List<User> allDirectors = userService.getUsersByRole(Role.DIRECTOR);
        Set<Long> assignedDirectorIds = schoolService.getAllSchools().stream()
                .map(school -> school.getDirector().getId())
                .collect(Collectors.toSet());

        List<User> unassignedDirectors = allDirectors.stream()
                .filter(director -> !assignedDirectorIds.contains(director.getId()))
                .collect(Collectors.toList());

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
            @RequestParam Long classroomId,
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

        School school = schoolService.getSchoolById(schoolId);
        teacher.setSchool(school);

        if (qualifiedSubjectsIds != null && !qualifiedSubjectsIds.isEmpty()) {
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
        @PostMapping("schools/classrooms/create")
        public String createClassroom(
                @RequestParam String name,
                @RequestParam Integer grade,
                @RequestParam Long schoolId,
                RedirectAttributes redirectAttributes
        ) {
            School school = schoolService.getSchoolById(schoolId);

            Classroom classroom = new Classroom();
            classroom.setName(name);
            classroom.setGrade(grade);
            classroom.setSchool(school);

            classroomService.createClassroom(classroom);

            redirectAttributes.addFlashAttribute("successMessage", "Паралелката е създадена успешно.");
            return "redirect:/admin/schools";
        }

    }