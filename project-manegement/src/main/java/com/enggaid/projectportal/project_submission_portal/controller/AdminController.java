package com.enggaid.projectportal.project_submission_portal.controller;

import com.enggaid.projectportal.project_submission_portal.model.ProjectGroup;
import com.enggaid.projectportal.project_submission_portal.model.ProjectMember;
import com.enggaid.projectportal.project_submission_portal.model.User;
import com.enggaid.projectportal.project_submission_portal.service.ProjectGroupService;
import com.enggaid.projectportal.project_submission_portal.service.ProjectHeadService;
import com.enggaid.projectportal.project_submission_portal.service.ProjectMemberService;
import com.enggaid.projectportal.project_submission_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectGroupService projectGroupService;

    @Autowired
    private ProjectHeadService projectHeadService;

    @Autowired
    private ProjectMemberService projectMemberService;



    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal){
        String username = principal.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user != null ? user : new User());
        model.addAttribute("projectGroupList", projectGroupService.getAllProjectGroup());
        model.addAttribute("projectHeadList", projectHeadService.getAllProjectHead());
        model.addAttribute("projectMemberList", projectMemberService.getAllMembers());
        model.addAttribute("users", userService.getAllUsers());

        return "admin/admin-dashboard";
    }


    //View All Users
    @GetMapping("/users")
    public String viewAllUsers(Model model){
        List<User> users =userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-form";
    }

    //Create Users
    @GetMapping("/user/create")
    public String showCreateUserForm(Model model){
        return "admin/user-create";
    }
    //Handle by only Admin to create the user
    @PostMapping("/user/create")
    public String createUser(@ModelAttribute User user, @AuthenticationPrincipal UserDetails userDetails, Model model){

        //Check Log in User is Admin Or nOt
        if(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            // admin proceed to create user
            userService.createUser(user);
            model.addAttribute("Success", "User Created Successfully");
            return "redirect:/admin/users";
        }else {
            // if is not ADMIN then Denied it
            model.addAttribute("Error", "You do not have permission to create users.");
            return "redirect:/home";
        }
    }

    //Delete Users
    @PostMapping("/deleteUserData/{id}")
    public String deleteUserData(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    //View All Project Members
    @GetMapping("/member")
    public String viewAllProjectMembers(Model model) {
        List<ProjectMember> projectMembers = projectMemberService.getAllMembers();
        model.addAttribute("member", projectMembers);
        return "admin/member";
    }
    @GetMapping("/projects")
    public String showAllProjects(Model model) {
        model.addAttribute("projectGroupList", projectGroupService.getAllProjectGroup());
        return "admin/projects"; // looks for templates/admin/projects.html
    }

    //Edit Members
    @GetMapping("/member/edit/{id}")
    public String editMember(@PathVariable Long id, Model model){
        Optional<ProjectMember> member = projectMemberService.getProjectMemberById(id);
        if (member.isPresent()){
            model.addAttribute("member", member.get());
            return "admin/member-form";
        }
        model.addAttribute("error", "Project Member Not Found");
        return "redirect:/admin/member";
    }

    @GetMapping("/member/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        if (projectMemberService.deleteById(id)) {
            return "redirect:/admin/member";
        }
        return "redirect:/admin/member?error=delete_failed";
    }





}
