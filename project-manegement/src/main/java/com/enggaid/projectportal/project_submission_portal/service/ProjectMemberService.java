package com.enggaid.projectportal.project_submission_portal.service;

import com.enggaid.projectportal.project_submission_portal.model.ProjectMember;
import com.enggaid.projectportal.project_submission_portal.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberRepository memberRepository;

    public boolean deleteById(Long id){
        memberRepository.deleteById(id);
        return true;
    }


    // Create or Update Member
    public ProjectMember saveMember(ProjectMember projectMember) {
        return memberRepository.save(projectMember);
    }

    // Get All Members
    public List<ProjectMember> getAllMembers() {
        return memberRepository.findAll();
    }

    // Get Member By ID
    public Optional<ProjectMember> getProjectMemberById(Long id){
        return memberRepository.findById(id);
    }


    // Delete Member By ID
    public boolean deleteMemberById(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get Members By Group ID
    public List<ProjectMember> getProjectMembersByGroupId(Long groupid) {
        return memberRepository.findByProjectGroup_Groupid(groupid);
    }

    public void updateMember(ProjectMember projectMember) {
        memberRepository.save(projectMember);
    }

    public Optional<ProjectMember> getMemberById(Long id) {
        return memberRepository.findById(id);
    }
}
