package com.enggaid.projectportal.project_submission_portal.controller;

import com.enggaid.projectportal.project_submission_portal.model.ProjectGroup;
import com.enggaid.projectportal.project_submission_portal.model.ProjectHead;
import com.enggaid.projectportal.project_submission_portal.model.ProjectMember;
import com.enggaid.projectportal.project_submission_portal.model.User;
import com.enggaid.projectportal.project_submission_portal.service.ProjectGroupService;
import com.enggaid.projectportal.project_submission_portal.service.ProjectHeadService;
import com.enggaid.projectportal.project_submission_portal.service.ProjectMemberService;
import com.enggaid.projectportal.project_submission_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projecthead")
public class ProjectHeadController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectGroupService projectGroupService;

    @Autowired
    private ProjectHeadService projectHeadService;

    @Autowired
    private ProjectMemberService projectMemberService;



    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal.getName();

        User projectHead = userService.findByUsername(username);
        if (projectHead == null) {
            model.addAttribute("errorMessage", "Project Head not found.");
            return "projecthead/projecthead-dashboard"; // Show error in dashboard itself
        }

        model.addAttribute("user", projectHead);

        ProjectGroup projectGroup = projectGroupService.getProjectGroupByProjectHeadId(projectHead.getId());
        if (projectGroup == null) {
            model.addAttribute("projectGroup", null);
            model.addAttribute("projectMemberList", Collections.emptyList());
            model.addAttribute("groupError", "No project group assigned yet.");
        } else {
            model.addAttribute("projectGroup", projectGroup);
            model.addAttribute("projectMemberList", projectGroup.getMembers());
        }

        return "projecthead/projecthead-dashboard";
    }



    @GetMapping("/projectgroup")
    public String getProjectGroup(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> projectHead = Optional.ofNullable(userService.findByUsername(username));

        if (projectHead.isPresent()) {
            User loggedInProjectHead = projectHead.get();
            ProjectGroup projectGroup = projectGroupService.getProjectGroupByProjectHeadId(loggedInProjectHead.getId());

            if (projectGroup != null) {
                model.addAttribute("projectgroup", List.of(projectGroup));
            } else {
                model.addAttribute("projectgroup", Collections.emptyList());
                model.addAttribute("message", "Project Group not found");
            }
        } else {
            model.addAttribute("message", "Project Head not found");
        }

        return "projecthead/projectgroup";
    }

    @GetMapping("/projectgroup/new")
    public String getNewProjectGroupForm(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Optional<User> projectHead = Optional.ofNullable(userService.findByUsername(username));

        if (projectHead.isPresent()) {
            ProjectGroup projectGroup = new ProjectGroup();
            projectGroup.setProjectHead(projectHead.get());
            model.addAttribute("projectgroup", projectGroup);
            return "projecthead/projectgroup-form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Project Head not found. Please try again.");
            return "redirect:/login";
        }
    }

    @GetMapping("/projectgroup/{id}/edit")
    public String editProjectGroupForm(@PathVariable("id") Long groupId, Model model, Principal principal) {
        User projectHead = userService.findByUsername(principal.getName());
        ProjectGroup projectGroup = projectGroupService.getProjectGroupById(groupId).orElse(null);

        if (projectHead != null && projectGroup != null && projectGroup.getProjectHead().getId().equals(projectHead.getId())) {
            model.addAttribute("projectgroup", projectGroup);
            return "projecthead/member-form";
        } else {
            model.addAttribute("error", "You are not authorized to edit this team");
            return "redirect:/projecthead/projectgroup";
        }
    }

    @PostMapping("/projectgroup/save")
    public String saveProjectGroup(@ModelAttribute ProjectGroup projectGroup, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User projectHead = userService.findByUsername(principal.getName());

            if (projectHead == null) {
                redirectAttributes.addFlashAttribute("error", "Project Head not found");
                return "redirect:/login";
            }

            projectGroup.setProjectHead(projectHead);

            if (projectGroup.getGroupid() != null) {
                ProjectGroup existingGroup = projectGroupService.getProjectGroupById(projectGroup.getGroupid()).orElse(null);

                if (existingGroup != null && existingGroup.getProjectHead().getId().equals(projectHead.getId())) {
                    existingGroup.setProjectTitle(projectGroup.getProjectTitle());
                    existingGroup.setProjectFiles(projectGroup.getProjectFiles());
                    existingGroup.setDescription(projectGroup.getDescription());
                    existingGroup.setStatus(projectGroup.getStatus());
                    existingGroup.setSubmissionDate(projectGroup.getSubmissionDate());
                    existingGroup.setMembers(projectGroup.getMembers());

                    projectGroupService.save(existingGroup);
                    redirectAttributes.addFlashAttribute("success", "Project Group updated successfully");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Unauthorized action. You cannot edit this team.");
                    return "redirect:/projecthead/projectgroup";
                }
            } else {
                projectGroupService.save(projectGroup);
                redirectAttributes.addFlashAttribute("success", "Project Group created successfully");
            }

            return "redirect:/projecthead/projectgroup";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error occurred while saving Project Group");
            return "redirect:/projecthead/projectgroup/new";
        }
    }

    @PostMapping("/projectgroup/{groupid}/update")
    public String updateProjectGroup(@PathVariable("groupid") Long groupId, @ModelAttribute ProjectGroup projectGroup, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            User projectHead = userService.findByUsername(principal.getName());
            projectGroup.setProjectHead(projectHead);

            ProjectGroup existingGroup = projectGroupService.getProjectGroupById(groupId).orElse(null);

            if (existingGroup != null && existingGroup.getProjectHead().getId().equals(projectHead.getId())) {
                existingGroup.setProjectTitle(projectGroup.getProjectTitle());
                existingGroup.setProjectFiles(projectGroup.getProjectFiles());
                existingGroup.setDescription(projectGroup.getDescription());
                existingGroup.setStatus(projectGroup.getStatus());
                existingGroup.setSubmissionDate(projectGroup.getSubmissionDate());
                existingGroup.setMembers(projectGroup.getMembers());

                projectGroupService.save(existingGroup);
                redirectAttributes.addFlashAttribute("success", "Project Group updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Unauthorized action. You cannot edit this project group.");
                return "redirect:/projecthead/projectgroup";
            }

            return "redirect:/projecthead/projectgroup";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error occurred while updating Project Group");
            return "redirect:/projecthead/projectgroup";
        }
    }

    @PostMapping("/projectgroup/{id}/delete")
    public String deleteProjectGroup(@PathVariable("id") Long groupId, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Optional<User> projectHead = Optional.ofNullable(userService.findByUsername(username));

        if (projectHead.isPresent()) {
            if (projectGroupService.isAuthorizedProjectHead(groupId, projectHead.get().getId())) {
                boolean isDeleted = projectGroupService.deleteProjectGroupById(groupId);

                if (isDeleted) {
                    redirectAttributes.addFlashAttribute("success", "Project Group deleted successfully");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Unable to delete the Project Group. Please try again.");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access. You cannot delete this Project Group.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Project Head not found. Please try again.");
        }

        return "redirect:/projecthead/projectgroup";
    }


    //View and Manage Members
    @GetMapping("/member")
    public String viewMember(Model model, Principal principal){
        String username =principal.getName();
        Optional<User> projecthead =Optional.ofNullable(userService.findByUsername(username));

        if (projecthead.isPresent()){
            ProjectGroup projectGroup =projectGroupService.getProjectGroupByProjectHeadId(projecthead.get().getId());

            if (projectGroup!=null){
                model.addAttribute("member",projectGroup.getMembers());
            }else {
                model.addAttribute("message", "You Dont manage Any Project Group");
                return "Error";
            }
        }else {
            model.addAttribute("message", "Project Head Not Found");
            return "error";
        }
        return "captain/member";
    }

    //New Memebere Form
    @GetMapping("/member/new")
    public String showMemberForm(Model model){
        List<ProjectGroup> projectGroups =projectGroupService.getAllProjectGroup();
        model.addAttribute("member",new ProjectMember());
        model.addAttribute("projectgroup",projectGroups);
        return "projecthead/member-form";
    }

    //View Member
    @GetMapping("/member/{id}")
    public String viewMember(@PathVariable("id") Long id, Model model, Principal principal){
        Optional<ProjectMember> projectMember= projectMemberService.getProjectMemberById(id);
        String username =principal.getName();
        Optional<User>projecthead =Optional.ofNullable(userService.findByUsername(username));

        if (projectMember.isPresent() && projecthead.isPresent()){
            ProjectMember projectMember1=projectMember.get();
            ProjectGroup projectGroup =projectGroupService.getProjectGroupByProjectHeadId(projecthead.get().getId());

            //Ensure That Memeeber belong to ProjectHead Teams
            if(projectGroup!=null && projectMember1.getProjectGroup()!=null && projectMember1.getProjectGroup().getGroupid().equals(projectGroup.getGroupid())) {
                model.addAttribute("member", projectMember1);
                return "projecthead/view-member";
            }
        }
        model.addAttribute("message", "player not found or unauthorized access");
        return "error";
    }

    @PostMapping("/member")
    public String savemember(@ModelAttribute ProjectMember projectMember, Principal principal, RedirectAttributes redirectAttributes){
        String username =principal.getName();
        Optional<User>projecthead =Optional.ofNullable(userService.findByUsername(username));

        if (projecthead.isPresent()){
            ProjectGroup projectGroup =projectGroupService.getProjectGroupByProjectHeadId(projecthead.get().getId());
            if (projectGroup !=null){
                //Check if the log in user is the projecthead of the project group
                if (!projectGroupService.isAuthorizedProjectHead(projectGroup.getGroupid(),projecthead.get().getId())){
                    redirectAttributes.addFlashAttribute("error", "You are not Authorised to manage this Project Group");
                    return "redirect:/projecthead/member";
                }

                //Check if Member ID Exist and if the member belongs to correct project group
                if (projectMember.getId()!=null){
                    Optional<ProjectMember> existingMemeber =projectMemberService.getProjectMemberById(projectMember.getId());

                    if (existingMemeber.isPresent() && existingMemeber.get().getProjectGroup().getGroupid().equals(projectGroup.getGroupid())){
                        //Update player if its the same team
                        projectMember.setProjectGroup(projectGroup);
                        projectMemberService.updateMember(projectMember);
                        redirectAttributes.addFlashAttribute("Success", "Member Update Sucessfully");

                    }else {
                        //Unauthorization
                        redirectAttributes.addFlashAttribute("Error", "Unauthorized Access You Cannot edit project group of others Project Group");
                        return "redirect:projecthead/member";
                    }
                }else {
                    //Create New Member and Assign to the Project Group
                    projectMember.setProjectGroup(projectGroup);
                    projectMemberService.saveMember(projectMember);
                    redirectAttributes.addFlashAttribute("Success", "Memeber Added Successfully");
                }
                return "redirect:/projecthead/member"; //Redirect to the Member List
            }else {
                //if no Project Group found for the creation , show Error MSG
                redirectAttributes.addFlashAttribute("Error", "No project GRoup Found");
                return "redirect:/projecthead/member";
            }
        }
        // If no projecthead is found, show error
        redirectAttributes.addFlashAttribute("error", "Project Head not found.");
        return "redirect:/login"; // Redirect to login page
    }

    //Edit Member Form
    @GetMapping("/member/{id}/update")
    public String editMember(@PathVariable("id") Long id, Model model,Principal principal){
        String username =principal.getName();
        Optional<User> projecthead =Optional.ofNullable(userService.findByUsername(username));

        if (projecthead.isPresent()){
            ProjectGroup projectGroup =projectGroupService.getProjectGroupByProjectHeadId(projecthead.get().getId());
            Optional<ProjectMember> projectMember = projectMemberService.getProjectMemberById(id);

            if (projectGroup!=null && projectMember.isPresent() &&projectMember.get().getProjectGroup().getGroupid().equals(projectGroup.getGroupid())){
                model.addAttribute("member", projectMember.get());
                return "projecthead/member-form";
            }else {
                model.addAttribute("error", "Member not found");
                return "redirect:/projecthead/member";
            }
        }
        model.addAttribute("error", "project Head not found");
        return "redirect:/login";
    }


    //Delete Member
    @PostMapping("/member/{id}/delete")
    public String deletemember(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes){
        String username =principal.getName();
        Optional<User> projecthead =Optional.ofNullable(userService.findByUsername(username));

        if (projecthead.isPresent()){
            ProjectGroup projectGroup =projectGroupService.getProjectGroupByProjectHeadId(projecthead.get().getId());
            Optional<ProjectMember> projectMember =projectMemberService.getProjectMemberById(id);

            if (projectGroup!=null && projectMember.isPresent() && projectMember.get().getProjectGroup().getGroupid().equals(projectGroup.getGroupid())){
                boolean isDeleted =projectMemberService.deleteById(id);
                if (isDeleted){
                    redirectAttributes.addFlashAttribute("Success", "Member Deleted Successfulyy");
                }else {
                    redirectAttributes.addFlashAttribute("error", "Something went to wrong please try again");
                }
            }else {
                redirectAttributes.addFlashAttribute("error","Member not found. or Unauthorized Access");
            }

        }else {
            redirectAttributes.addFlashAttribute("error", "ProjectHead is Not Found. Please Login again ");
        }
        return "redirect:/projecthead/member";
    }

}

