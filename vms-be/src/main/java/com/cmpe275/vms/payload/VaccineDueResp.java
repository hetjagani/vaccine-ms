package com.cmpe275.vms.payload;

public class VaccineDueResp {
	private Integer id;
    private String name;
    private String manufacturer;
    private Integer numOfShots;
    private Integer shotInterval;       // in days
    private Integer duration;           // # days vaccine is good for; -1 for lifetime
    private Integer shotNumber;
    
	public VaccineDueResp(Integer id,String name, String manufacturer, Integer numOfShots, Integer shotInterval, Integer duration,
			Integer shotNumber) {
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
		this.numOfShots = numOfShots;
		this.shotInterval = shotInterval;
		this.duration = duration;
		this.shotNumber = shotNumber;
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
	public Integer getShotNumber() {
		return shotNumber;
	}
	public void setShotNumber(Integer shotNumber) {
		this.shotNumber = shotNumber;
	}
    
    
}
