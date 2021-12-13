package com.cmpe275.vms.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.cmpe275.vms.model.Address;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ClinicRequest {
	@NotBlank
	private String name;
	
	private Address address;
	
	@JsonFormat(pattern = "HH:mm")
	@NotEmpty
	private String startTime;

	@JsonFormat(pattern = "HH:mm")
	private String endTime;

	private Integer numberOfPhysicians;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getNumberOfPhysicians() {
		return numberOfPhysicians;
	}

	public void setNumberOfPhysicians(Integer numberOfPhysicians) {
		this.numberOfPhysicians = numberOfPhysicians;
	}
}
