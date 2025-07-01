package com.uni.GradeCenter.controller;


import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.service.GradeService;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/director")
public class DirectorController {
    private final UserService userService;
    private final SchoolService schoolService;
    private final GradeService gradeService;

    public DirectorController(UserService userService, SchoolService schoolService, GradeService gradeService) {
        this.userService = userService;
        this.schoolService = schoolService;
        this.gradeService = gradeService;
    }

    @GetMapping("/school")
    public String schoolInformation(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        School school = schoolService.findByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("school", school);
        model.addAttribute("teachers", school.getTeachers());
        model.addAttribute("students", school.getStudents());
        model.addAttribute("subjects", school.getSubjects());
        model.addAttribute("classrooms", school.getClassrooms());
        List<Parent> parents = school.getStudents().stream()
                .map(Student::getParent)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        model.addAttribute("parents", parents);
        Map<String, Long> gradesPerSubject = gradeService.countGradesPerSubjectForSchool(school.getId());
        Map<String, Double> averagePerTeacher = gradeService.averageGradesPerTeacherForSchool(school.getId());
        Double overallAverage = gradeService.calculateOverallAverageForSchool(school.getId());

        model.addAttribute("gradesPerSubject", gradesPerSubject);
        model.addAttribute("averagePerTeacher", averagePerTeacher);
        model.addAttribute("overallAverage", overallAverage);

        return "director-school";
    }

}
