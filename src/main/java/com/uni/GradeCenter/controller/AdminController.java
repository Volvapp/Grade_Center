package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateSchoolBindingDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

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

    @GetMapping("/schools/create")
    public String createSchoolForm(Model model) {
        if (!model.containsAttribute("createSchoolBindingDTO")) {
            model.addAttribute("createSchoolBindingDTO", new CreateSchoolBindingDTO());
        }

        model.addAttribute("directors", userService.getUsersByRole(Role.DIRECTOR));

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
}
