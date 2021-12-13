package com.cmpe275.vms.payload;

import java.util.Set;

public class VaccineRequest {
	private String name;
    private String manufacturer;
    private Integer numOfShots;
    private Integer shotInterval;       
    private Integer duration; 
    private Set<Integer> diseaseIdList;

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

	public Set<Integer> getDiseaseIdList() {
		return diseaseIdList;
	}

	public void setDiseaseIdList(Set<Integer> diseaseIdList) {
		this.diseaseIdList = diseaseIdList;
	}
}
