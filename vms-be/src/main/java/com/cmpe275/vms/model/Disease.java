package com.cmpe275.vms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "disease_vaccine",
            joinColumns = {@JoinColumn(name = "disease_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "vaccine_id", referencedColumnName = "id")}
    )
    @JsonIgnoreProperties({"diseases"})
    private Set<com.cmpe275.vms.model.Vaccine> vaccines = new HashSet<com.cmpe275.vms.model.Vaccine>();

    public Disease() {}

    public Disease(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(Set<com.cmpe275.vms.model.Vaccine> vaccines) {
        this.vaccines = vaccines;
    }
}
