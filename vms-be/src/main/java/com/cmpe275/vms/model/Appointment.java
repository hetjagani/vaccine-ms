package com.cmpe275.vms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @DateTimeFormat(pattern = "HH-mm")
    private LocalTime time;

    private String status;

    @ManyToMany
    @JoinTable(
            name = "appointment_vaccine",
            joinColumns = {@JoinColumn(name="appointment_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "vaccine_id", referencedColumnName = "id")}
    )
    @JsonIgnoreProperties({"appointments"})
    private List<Vaccine> vaccines;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonIgnoreProperties({"appointments"})
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"appointments"})
    private User user;

    public Appointment() {}

    public Appointment(Integer id, LocalTime time, String status) {
        this.id = id;
        this.time = time;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
