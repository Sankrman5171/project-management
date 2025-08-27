package com.enggaid.projectportal.project_submission_portal.service;

import com.enggaid.projectportal.project_submission_portal.model.ProjectHead;
import com.enggaid.projectportal.project_submission_portal.repository.ProjectHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectHeadService {

    @Autowired
    private ProjectHeadRepository repository;

    //get all Project Head
    public List<ProjectHead> getAllProjectHead(){
        return repository.findAll();
    }

    // get ProjectHead By ID
    public Optional<ProjectHead> getProjectHead(Long id){
        return repository.findById(id);
    }

    //Delete by id
    public  void deleteProjectHead(Long id){
        repository.deleteById(id);
    }

    public void save(ProjectHead projectHead){
        repository.save(projectHead);
    }
}
