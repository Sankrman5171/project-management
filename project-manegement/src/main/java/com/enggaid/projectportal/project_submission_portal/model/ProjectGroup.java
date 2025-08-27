package com.enggaid.projectportal.project_submission_portal.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupid;

    private String projectTitle;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToOne
    @JoinColumn(name = "head_id")
    private ProjectHead projectHead;  // Corrected to ProjectHead instead of User

    @OneToMany(mappedBy = "projectGroup", cascade = CascadeType.ALL)
    private List<ProjectMember> members;

    @OneToMany(mappedBy = "projectGroup", cascade = CascadeType.ALL)
    private List<ProjectFile> projectFiles;

    private String submissionDate;

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public ProjectHead getProjectHead() {
        return projectHead;
    }

    public void setProjectHead(ProjectHead projectHead) {
        this.projectHead = projectHead;
    }

    public List<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMember> members) {
        this.members = members;
    }

    public List<ProjectFile> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(List<ProjectFile> projectFiles) {
        this.projectFiles = projectFiles;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    // Getters and setters omitted for brevity
}
