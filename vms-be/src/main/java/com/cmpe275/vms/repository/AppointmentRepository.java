package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
//	@Query(value="SELECT * FROM APPOINT",nativeQuery=true)
//	List<Appointment> findAllFutureAppointments();
}
