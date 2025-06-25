package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final SchoolService schoolService;
    private final ModelMapper modelMapper;

    public AdminController(UserService userService, SchoolService schoolService, ModelMapper modelMapper) {
        this.userService = userService;
        this.schoolService = schoolService;
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
            @RequestParam Long directorId,
            RedirectAttributes redirectAttributes
    ) {
        School school = schoolService.getSchoolById(id);
        school.setName(name);
        school.setAddress(address);

        User director = userService.getUserById(directorId);
        school.setDirector(director);

        schoolService.updateSchool(school);

        redirectAttributes.addFlashAttribute("successMessage", "Промяната е направена успешно!");

        return "redirect:/admin/schools";
    }
}
