package com.cmpe275.vms.payload;

import com.cmpe275.vms.model.Clinic;

public class AdminReport {
    private Clinic clinic;
    private Integer totalNoShow;
    private Integer total;
    private Double noShowRate;

    public AdminReport () {}

    public AdminReport(Clinic clinic, Integer totalNoShow, Integer total, Double noShowRate) {
        this.clinic = clinic;
        this.totalNoShow = totalNoShow;
        this.total = total;
        this.noShowRate = noShowRate;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
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
