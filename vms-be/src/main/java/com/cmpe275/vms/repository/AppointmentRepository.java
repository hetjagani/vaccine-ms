package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Query(value="SELECT * from appointment WHERE date>?1 OR (time>?2 AND date=?1) ORDER BY date ASC, TIME ASC;",nativeQuery=true)
	List<Appointment> findAllFutureAppointments(String currentDate, String currentTime); 

	@Query(value="SELECT * from appointment WHERE date<?1 OR (time<?2 AND date=?1) ORDER BY date DESC, TIME DESC;",nativeQuery=true)
	List<Appointment> findAllPastAppointments(String currentDate, String currentTime); 
	
	@Query(value="SELECT * from appointment WHERE (date>?1 OR (time>?3 AND date=?1)) AND (date<?2 OR (time<?3 AND date=?2)) ORDER BY date ASC, time ASC;",nativeQuery=true)
	List<Appointment> findNext12MonthsAppointments(String date1,String date2, String currentTime);
	
	@Query(value="SELECT time, COUNT(*) AS slots FROM appointment WHERE clinic_id=?1 and date=?2 GROUP BY time;",nativeQuery=true)
	List<Object[]> findAllSlotsForDateAndClinic(int i, String date);
}
