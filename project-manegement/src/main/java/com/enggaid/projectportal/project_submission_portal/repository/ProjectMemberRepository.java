package com.enggaid.projectportal.project_submission_portal.repository;

import com.enggaid.projectportal.project_submission_portal.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    // ✅ Correct way to query by projectGroup.groupid
    List<ProjectMember> findByProjectGroup_Groupid(Long groupid);

    Optional<ProjectMember> findByEmail(String email);

    Optional<ProjectMember> findByEnrollmentNumber(String enrollmentNumber);
}
