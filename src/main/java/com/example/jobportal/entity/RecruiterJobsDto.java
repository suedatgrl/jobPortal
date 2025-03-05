package com.example.jobportal.entity;

import jakarta.persistence.Entity;


public class RecruiterJobsDto {

    private long totalCandidates;

    private Integer jobPostId;

    private String jobTitle;

    private JobLocation jobLocationId;

    private JobCompany jobCompanyId;

    //parameters should be in order bcs constructor is used in JobPostActivityService
    public RecruiterJobsDto(long totalCandidates, Integer jobPostId, String jobTitle,  JobLocation jobLocationId, JobCompany jobCompanyId) {
        this.totalCandidates = totalCandidates;
        this.jobCompanyId = jobCompanyId;
        this.jobLocationId = jobLocationId;
        this.jobTitle = jobTitle;
        this.jobPostId = jobPostId;
    }


    public long getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(long totalCandidates) {
        this.totalCandidates = totalCandidates;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocationId) {
        this.jobLocationId = jobLocationId;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompanyId) {
        this.jobCompanyId = jobCompanyId;
    }


}
