package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Query(value="SELECT * from appointment WHERE user_id=?3 AND (date>?1 OR (time>?2 AND date=?1)) ORDER BY date ASC, TIME ASC;",nativeQuery=true)
	List<Appointment> findAllFutureAppointments(String currentDate, String currentTime, String userId); 

	@Query(value="SELECT * from appointment WHERE user_id=?3 AND (date<?1 OR (time<?2 AND date=?1)) ORDER BY date DESC, TIME DESC;",nativeQuery=true)
	List<Appointment> findAllPastAppointments(String currentDate, String currentTime, String userId); 
	
	@Query(value="SELECT * from appointment WHERE user_id=?4 AND (date>?1 OR (time>?3 AND date=?1)) AND (date<?2 OR (time<?3 AND date=?2)) ORDER BY date ASC, time ASC;",nativeQuery=true)
	List<Appointment> findNext12MonthsAppointments(String date1,String date2, String currentTime, String userId);
	
	@Query(value="SELECT time, COUNT(*) AS slots FROM appointment WHERE clinic_id=?1 and date=?2 GROUP BY time;",nativeQuery=true)
	List<Object[]> findAllSlotsForDateAndClinic(int i, String date);
	
	@Query(value="SELECT vaccine_id as id, COUNT(*) as num_of_shots FROM appointment JOIN appointment_vaccine ON appointment.id = appointment_vaccine.appointment_id where user_id=?1 AND status='CHECKIN' GROUP BY vaccine_id;", nativeQuery=true)
	List<Object[]> findUserVaccineShotsTaken(String userId);
	
	@Query(value="SELECT vaccine_id as id, COUNT(*) as num_of_shots FROM appointment JOIN appointment_vaccine ON appointment.id = appointment_vaccine.appointment_id where user_id=?1 AND status!='NOSHOW' AND (date<?2 OR (time<?3 AND date=?2)) GROUP BY vaccine_id;",nativeQuery=true)
	List<Object[]> findUserVaccineFromGivenTime(String userId, String currentDate, String currentTime);

	@Query(value = "SELECT * from appointment WHERE user_id=?3 AND date>?1 AND date<?2 ORDER BY date ASC, time ASC;", nativeQuery = true)
	List<Object[]> findAppointmentsBetweenDates(String date1, String date2, String userId);

	@Query(value = "SELECT * from appointment WHERE date>?1 AND date<?2 AND clinic_id=?3 ORDER BY date ASC, time ASC;", nativeQuery = true)
	List<Object[]> findClinicAppointmentsBetweenDates(String date1, String date2, Integer clinicId);
}
