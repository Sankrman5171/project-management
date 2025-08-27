package com.enggaid.projectportal.project_submission_portal.service;

import com.enggaid.projectportal.project_submission_portal.model.ProjectGroup;
import com.enggaid.projectportal.project_submission_portal.repository.ProjectGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectGroupService {

    @Autowired
    private ProjectGroupRepository projectGroupRepository;

    // Save a new Project Group
    public ProjectGroup saveProjectGroup(ProjectGroup projectGroup) {
        return projectGroupRepository.save(projectGroup);
    }

    //delete a projectGroup by its id
    public boolean deleteProjectGroupById(Long id){
        if(projectGroupRepository.existsById(id)){
            projectGroupRepository.deleteById(id);
            return true; //deletion take place
        }
        return false; // no team founds
    }

    //Update ProjectGroup
    public ProjectGroup updateProjectGroup(Long id, ProjectGroup projectGroup){
        Optional<ProjectGroup> group = projectGroupRepository.findByProjectHead_Id(id);
        if (group.isPresent()){
            ProjectGroup projectGroup1 =group.get();
            projectGroup1.setProjectFiles(projectGroup.getProjectFiles());
            projectGroup1.setProjectHead(projectGroup.getProjectHead());
            projectGroup1.setProjectTitle(projectGroup.getProjectTitle());
            projectGroup1.setDescription(projectGroup.getDescription());
            projectGroup1.setSubmissionDate(projectGroup.getSubmissionDate());
            return projectGroupRepository.save(projectGroup);
        }
        return null;
    }

    //Get a Project by Projecthead Id

    public ProjectGroup getProjectGroupByProjectHeadId(Long id){
        return projectGroupRepository.findByProjectHead_Id(id).orElse(null);
    }

    //get all Project Group
    public List<ProjectGroup> getAllProjectGroup(){
        return projectGroupRepository.findAll();
    }

    //Get Team By Id
    public Optional<ProjectGroup> getProjectGroupById(Long groupid){
        return projectGroupRepository.findByProjectHead_Id(groupid);
    }

    //Check if the project group is authorized for specific projecthead
    public boolean isAuthorizedProjectHead (Long groupid, Long id){
        Optional<ProjectGroup> group =projectGroupRepository.findById(groupid);
        return group.isPresent() && group.get().getProjectHead().getId().equals(id);
    }

    public void save(ProjectGroup projectGroup){
        projectGroupRepository.save(projectGroup);
    }
}
