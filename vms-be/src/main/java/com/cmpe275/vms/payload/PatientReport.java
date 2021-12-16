package com.cmpe275.vms.payload;

import com.cmpe275.vms.model.User;

public class PatientReport {
    private User user;
    private Integer totalNoShow;
    private Integer total;
    private Double noShowRate;

    public PatientReport () {}

    public PatientReport(User user, Integer totalNoShow, Integer total, Double noShowRate) {
        this.user = user;
        this.totalNoShow = totalNoShow;
        this.total = total;
        this.noShowRate = noShowRate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotalNoShow() {
        return totalNoShow;
    }

    public void setTotalNoShow(Integer totalNoShow) {
        this.totalNoShow = totalNoShow;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getNoShowRate() {
        return noShowRate;
    }

    public void setNoShowRate(Double noShowRate) {
        this.noShowRate = noShowRate;
    }
}
