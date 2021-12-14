package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Query(value="SELECT * from appointment WHERE date>current_date() OR (time>current_time() AND date=current_date()) ORDER BY date ASC, TIME ASC;",nativeQuery=true)
	List<Appointment> findAllFutureAppointments(); 

	@Query(value="SELECT * from appointment WHERE date<current_date() OR (time<current_time() AND date=current_date()) ORDER BY date DESC, TIME DESC;",nativeQuery=true)
	List<Appointment> findAllPastAppointments(); 
	
	@Query(value="SELECT * from appointment WHERE (date>?1 OR (time>current_time() AND date=?1)) AND (date<?2 OR (time<current_time() AND date=?2)) ORDER BY date DESC, time DESC;",nativeQuery=true)
	List<Appointment> findNext12MonthsAppointments(String date1,String date2);
}
