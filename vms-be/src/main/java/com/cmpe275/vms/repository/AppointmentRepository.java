package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Query(value="SELECT * from appointment WHERE convert(date,date)>current_date() OR (time>current_time() AND convert(date,date)=current_date()) ORDER BY convert(date,date) ASC, TIME ASC;",nativeQuery=true)
	List<Appointment> findAllFutureAppointments(); 

	@Query(value="SELECT * from appointment WHERE convert(date,date)<current_date() OR (time<current_time() AND convert(date,date)=current_date()) ORDER BY convert(date,date) DESC, TIME DESC;",nativeQuery=true)
	List<Appointment> findAllPastAppointments(); 
	
	@Query(value="SELECT * from appointment WHERE (convert(date,date)>?1 OR (time>current_time() AND convert(date,date)=?1)) AND (convert(date,date)<?2 OR (time<current_time() AND convert(date,date)=?2)) ORDER BY convert(date,date) DESC, time DESC;",nativeQuery=true)
	List<Appointment> findNext12MonthsAppointments(String date1,String date2);
	
	@Query(value="SELECT time, COUNT(*) AS slots FROM appointment WHERE clinic_id=?1 and convert(date,date)=?2 GROUP BY time;",nativeQuery=true)
	List<Object[]> findAllSlotsForDateAndClinic(int i, String date);
}
