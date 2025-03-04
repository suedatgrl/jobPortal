package com.example.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {

    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users userId;

    private String city;

    private String country;

    private String company;

    private String firstName;

    private String lastName;
    @Column(nullable = true, length = 64)
    private String profilePhoto;

    private String state;

    public RecruiterProfile() {
    }

    public RecruiterProfile(int userAccountId, Users userId, String city, String country, String company, String firstName, String lastName, String profilePhoto, String state) {
        this.userAccountId = userAccountId;
        this.userId = userId;
        this.city = city;
        this.country = country;
        this.company = company;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.state = state;
    }

    public RecruiterProfile(Users users) {
        this.userId = users;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Transient // This annotation is used to indicate that a field is not to be persisted in the database.
    public String getPhotosImagePath(){
        if(profilePhoto == null || userAccountId == 0) return null;

        return "/photos/recruiter" + userAccountId + "/" + profilePhoto;

    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
