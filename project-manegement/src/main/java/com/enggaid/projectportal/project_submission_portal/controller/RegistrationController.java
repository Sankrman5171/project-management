package com.enggaid.projectportal.project_submission_portal.controller;

import com.enggaid.projectportal.project_submission_portal.model.Role;
import com.enggaid.projectportal.project_submission_portal.model.User;
import com.enggaid.projectportal.project_submission_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User  user, Model model){
        //Check if the username alredy exists
        User existingUser = userService.findByUsername(user.getUsername());

        if (existingUser !=null){
            model.addAttribute("error", "Username Already Exist");
            return "register";
        }

        try{
            Role role = Role.valueOf(user.getRole().toString());
            user.setRole(role);
        }catch (IllegalArgumentException e){
            model.addAttribute("error", "Invalid Role Selected");
            return  "register";
        }

        try{
            userService.saveUser(user);
            model.addAttribute("success", "Registration Successful ! you Can Now Log in.");
        }catch (Exception e){
            model.addAttribute("error", "An error occurence during registratio. Please Try again.");
            return "register";
        }

        return "redirect:/login";
    }
}
