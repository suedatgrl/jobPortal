package com.example.jobportal.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String city;

    private String country;

    private String employmentType;

    private String firstName;

    private String lastName;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    private String resume;

    private String state;

    private String workAuthorization;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, targetEntity = Skills.class)
    private List<Skills> skills;

    public JobSeekerProfile() {
    }

    public JobSeekerProfile(Users userId) {
        this.userId = userId;
    }

    public JobSeekerProfile(Integer userAccountId, Users userId,  String firstName, String country, String lastName, String city, String state, String workAuthorization, String employmentType, String resume, String profilePhoto, List<Skills> skills) {
        this.userAccountId = userAccountId;
        this.skills = skills;
        this.userId = userId;
        this.firstName = firstName;
        this.city = city;
        this.country = country;
        this.employmentType = employmentType;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.resume = resume;
        this.state = state;
        this.workAuthorization = workAuthorization;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null || userAccountId == null) return null;
        return "photos/candidate/" + userAccountId + "/" + profilePhoto;


    }



    @Override
    public String toString() {
        return "JobSeekerProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", employmentType='" + employmentType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", resume='" + resume + '\'' +
                ", state='" + state + '\'' +
                ", workAuthorization='" + workAuthorization + '\'' +
                '}';
    }
}
