package com.enggaid.projectportal.project_submission_portal.repository;

import com.enggaid.projectportal.project_submission_portal.model.ProjectGroup;
import com.enggaid.projectportal.project_submission_portal.model.ProjectHead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {


    // Find a Group by Group_head ID
    //Optional<ProjectGroup> findByProjectHead_Id(Long id);
    Optional<ProjectGroup> findByProjectHead_Id(Long groupid); // Corrected method name

    // List of all projectGroups associated with specific project head by ID
    //List<ProjectGroup> findByProjectHead_Id(Long id);

    // List of all projectGroups associated with specific project head by name
    List<ProjectGroup> findByProjectHead_Name(String name);
}
