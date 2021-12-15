package com.cmpe275.vms.payload;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class VaccineRequest {
	@NotBlank
	private String name;
	
	@NotBlank
    private String manufacturer;
    
	private Integer numOfShots;
	private Integer shotInterval;       

	private Integer duration; 
    private List<Integer> diseaseIds;

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
	
	public List<Integer> getDiseaseIds() {
		return diseaseIds;
	}

	public void setDiseaseIds(List<Integer> diseaseIds) {
		this.diseaseIds = diseaseIds;
	}

	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
