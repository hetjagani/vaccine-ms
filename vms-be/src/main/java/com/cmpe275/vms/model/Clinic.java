package com.cmpe275.vms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Embedded
    private Address address;

    @DateTimeFormat(pattern = "HH-mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH-mm")
    private LocalTime endTime;

    private Integer numberOfPhysicians;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"clinic"})
    private List<com.cmpe275.vms.model.Appointment> appointments;

    public Clinic() {}

    public Clinic(Integer id, Address address, LocalTime startTime, LocalTime endTime, Integer numberOfPhysicians) {
        this.id = id;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfPhysicians = numberOfPhysicians;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getNumberOfPhysicians() {
        return numberOfPhysicians;
    }

    public void setNumberOfPhysicians(Integer numberOfPhysicians) {
        this.numberOfPhysicians = numberOfPhysicians;
    }

    public List<com.cmpe275.vms.model.Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<com.cmpe275.vms.model.Appointment> appointments) {
        this.appointments = appointments;
    }
}
