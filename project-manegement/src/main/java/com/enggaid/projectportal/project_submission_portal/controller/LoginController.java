package com.enggaid.projectportal.project_submission_portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login (Model model){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model){
        boolean isValid = authenticateUser(username, password);

        if (isValid){
            model.addAttribute("success", "Login Sucessfully");
            return "redirect:/dashboard";
        }else {
            model.addAttribute("error", "invalid Username And Password");
            return "login";
        }
    }

    private boolean authenticateUser(String username, String password){
        return  "admin".equals(username) && "password123".equals(password);
    }
}
