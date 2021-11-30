package com.cmpe275.vms.model;

import com.cmpe275.vms.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Entity
public class User {
    @Id
    private String mrn;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date createdAt;

    @Email
    @Column(unique = true)
    private String email;

    @DateTimeFormat(pattern="dd-MM-yyyy")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @NotNull
    private String password;

    private Boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"user"})
    private List<Appointment> appointments;

    public User() {}

    public User(UserRepository userRepository) {
        List<User> users = userRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        if(users.size() > 0) {
            this.mrn = String.valueOf(Integer.parseInt(users.get(0).mrn)+1);
        } else {
            this.mrn = String.valueOf(1000);
        }
    }

    public User(String mrn, String firstName, String middleName, String lastName, String email, Date dateOfBirth, Gender gender, Address address, Role role) {
        this.mrn = mrn;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.createdAt = new Date();
    }

    public User(UserRepository userRepository, String firstName, String middleName, String lastName, String email, Date dateOfBirth, Gender gender, Address address, Role role) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.createdAt = new Date();

        List<User> users = userRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        if(users.size() > 0) {
            this.mrn = String.valueOf(Integer.parseInt(users.get(0).mrn)+1);
        } else {
            this.mrn = String.valueOf(1000);
        }
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "User{" +
                "mrn='" + mrn + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", address=" + address +
                ", role=" + role +
                ", isVerified=" + isVerified +
                ", password =" + password +
                '}';
    }
}
