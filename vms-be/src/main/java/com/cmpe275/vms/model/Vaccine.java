package com.cmpe275.vms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String manufacturer;
    private Integer numOfShots;
    private Integer shotInterval;       // in days
    private Integer duration;           // # days vaccine is good for; -1 for lifetime

    @ManyToMany
    @JoinTable(
            name = "disease_vaccine",
            joinColumns = {@JoinColumn(name = "vaccine_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "disease_id", referencedColumnName = "id")}
    )
    @JsonIgnoreProperties({"vaccines"})
    private List<Disease> diseases ;

    @ManyToMany(mappedBy = "vaccines")
    @JsonIgnoreProperties({"vaccines"})
    private Set<com.cmpe275.vms.model.Appointment> appointments;

    public Vaccine() {}

    public Vaccine(String name, String manufacturer, Integer numOfShots, Integer shotInterval, Integer duration) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.numOfShots = numOfShots;
        this.shotInterval = shotInterval;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getNumOfShots() {
        return numOfShots;
    }

    public void setNumOfShots(Integer numOfShots) {
        this.numOfShots = numOfShots;
    }

    public Integer getShotInterval() {
        return shotInterval;
    }

    public void setShotInterval(Integer shotInterval) {
        this.shotInterval = shotInterval;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<com.cmpe275.vms.model.Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public Set<com.cmpe275.vms.model.Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<com.cmpe275.vms.model.Appointment> appointments) {
        this.appointments = appointments;
    }
}
