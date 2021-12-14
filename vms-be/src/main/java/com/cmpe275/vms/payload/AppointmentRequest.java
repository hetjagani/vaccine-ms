package com.cmpe275.vms.payload;

import com.cmpe275.vms.model.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class AppointmentRequest {
	@DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;     // time in interval of 15 minutes

	private List<Integer> vaccineIds; // atmost 4 vaccines
    private Integer clinicId;
    private String userId;
    private AppointmentStatus status;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private Date appDate;

    public AppointmentRequest(LocalTime time, List<Integer> vaccineIds, Integer clinicId, String userId, Date appDate) {
        this.time = time;
        this.vaccineIds = vaccineIds;
        this.clinicId = clinicId;
        this.userId = userId;
        this.appDate = appDate;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<Integer> getVaccineIds() {
        return vaccineIds;
    }

    public void setVaccineIds(List<Integer> vaccineIds) {
        this.vaccineIds = vaccineIds;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    
	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	@Override
    public String toString() {
        return "AppointmentRequest{" +
                "time=" + time +
                ", vaccineIds=" + vaccineIds +
                ", clinicId=" + clinicId +
                ", userId='" + userId + '\'' +
                '}';
    }
}
