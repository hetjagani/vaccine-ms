package com.cmpe275.vms.payload;

import com.cmpe275.vms.model.Address;
import com.cmpe275.vms.model.Gender;
import com.cmpe275.vms.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class UserRequest {
    @NotBlank
    private String firstName;
    private String middleName;
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private Date dateOfBirth;

    private Boolean isVerified = false;
    private Gender gender;
    private Role role;
    private Address address;

    @NotBlank
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public UserRequest() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }

    public com.cmpe275.vms.model.Address getAddress() {
        return address;
    }

    public void setAddress(com.cmpe275.vms.model.Address address) {
        this.address = address;
    }

    public com.cmpe275.vms.model.Gender getGender() {
        return gender;
    }

    public void setGender(com.cmpe275.vms.model.Gender gender) {
        this.gender = gender;
    }

    public com.cmpe275.vms.model.Role getRole() {
        return role;
    }

    public void setRole(com.cmpe275.vms.model.Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", isVerified=" + isVerified +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", address=" + address +
                ", password='" + password + '\'' +
                '}';
    }
}
