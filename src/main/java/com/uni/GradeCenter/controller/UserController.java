package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        return "users-login";
    }

    @GetMapping("/register")
    public String register(Model model, Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }

        if (!model.containsAttribute("isUserOrEmailOccupied")) {
            model.addAttribute("isUserOrEmailOccupied", false);
        }

        model.addAttribute("userDTO", new UserDTO());

        return "users-register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.UserDTO", bindingResult);
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "users-register";
        }
        if (userService.usernameOrEmailExists(userDTO.getUsername(), userDTO.getEmail())) {
            redirectAttributes.addFlashAttribute("isUserOrEmailOccupied", true);
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            return "redirect:/users/register";
        }

        User user = this.modelMapper.map(userDTO, User.class);
        this.userService.createUser(user);
        return "redirect:/users/login";
    }



}
