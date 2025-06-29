package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.service.ParentService;
import com.uni.GradeCenter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/parent")
public class ParentController {
    private final UserService userService;
    private final ParentService parentService;

    public ParentController(UserService userService, ParentService parentService) {
        this.userService = userService;
        this.parentService = parentService;
    }

    @GetMapping("/child")
    public String parent(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Parent parent = parentService.getParentByUserId(user.getId());

        model.addAttribute("parent", parent);

        return "parent-child.html";
    }
}
