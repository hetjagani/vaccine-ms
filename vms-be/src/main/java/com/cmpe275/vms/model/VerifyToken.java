package com.cmpe275.vms.model;

import javax.persistence.*;

@Entity
public class VerifyToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String token;

    public VerifyToken() {}

    public VerifyToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
