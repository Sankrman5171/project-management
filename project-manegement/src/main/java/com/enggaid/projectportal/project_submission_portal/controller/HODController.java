package com.enggaid.projectportal.project_submission_portal.controller;

import com.enggaid.projectportal.project_submission_portal.model.User;
import com.enggaid.projectportal.project_submission_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/hod")
public class HODController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal){
        String username = principal.getName();

        return "hod/hod-dashboard";
    }
}
