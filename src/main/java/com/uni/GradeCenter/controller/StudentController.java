package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final UserService userService;
    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final ModelMapper modelMapper;

    public StudentController(UserService userService, StudentService studentService, ScheduleService scheduleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/statistics")
    public String statistics(Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        Student student = studentService.getStudentByUserId(user.getId());

        model.addAttribute("student", student);

        return "student-statistics";
    }

    @GetMapping("/schedule")
    public String schedule(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Student student = studentService.getStudentByUserId(user.getId());

        model.addAttribute("schedules", scheduleService.mapToView(student.getClassroom().getSchedules()));

        return "student-schedule";
    }

}
