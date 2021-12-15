package com.cmpe275.vms.payload;

import java.time.LocalTime;

public class AppointmentSlotsResp {
	private LocalTime time;
	private int slots;
	
	public AppointmentSlotsResp(LocalTime string, int slots) {
		this.time = string;
		this.slots = slots;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}
}
