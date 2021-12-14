package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.*;
import com.cmpe275.vms.payload.AppointmentRequest;
import com.cmpe275.vms.repository.AppointmentRepository;
import com.cmpe275.vms.repository.ClinicRepository;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/appointments")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PATIENT')")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentByID(@PathVariable Integer id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("appointment", "id", id));

        return ResponseEntity.ok(appointment);
    }

    // TODO: 15 minute intervals only accepted
    // TODO: send email for the appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(Principal principal, @RequestBody AppointmentRequest request) {
        String loggedInEmail = principal.getName();

        // fetch logged in user
        User loginUser = userRepository.findByEmail(loggedInEmail).orElseThrow(() -> new ResourceNotFoundException("user", "email", loggedInEmail));

        if(loginUser.getMrn() == request.getUserId()) {
            throw new SecurityException("cannot create other user's appointment");
        }

        List<Vaccine> vaccineList = vaccineRepository.findAllById(request.getVaccineIds());

        Clinic clinic = clinicRepository.findById(request.getClinicId()).orElseThrow(() -> new ResourceNotFoundException("clinic", "id", request.getClinicId()));

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", request.getUserId()));

        Appointment appointment = new Appointment();
        appointment.setTime(request.getTime());
        appointment.setStatus(AppointmentStatus.INIT);
        appointment.setClinic(clinic);
        appointment.setUser(user);
        appointment.setVaccines(vaccineList);
        appointment.setDate(request.getDate());

        Appointment createdAppointment = appointmentRepository.save(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
    }

    // TODO: 15 minute intervals only accepted
    // TODO: send email for the appointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(Principal principal, @PathVariable Integer id, @RequestBody AppointmentRequest request) {
        String loggedInEmail = principal.getName();

        Appointment dbAppointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("appointment", "id", id));

        if(!dbAppointment.getUser().getEmail().equals(loggedInEmail)) {
            throw new SecurityException("cannot update other user's appointment");
        }

        List<Vaccine> vaccineList = vaccineRepository.findAllById(request.getVaccineIds());

        dbAppointment.setTime(request.getTime());
        dbAppointment.setStatus(request.getStatus());
        dbAppointment.setVaccines(vaccineList);

        Appointment updatedAppointment = appointmentRepository.save(dbAppointment);

        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(Principal principal, @PathVariable Integer id){
        String loggedInEmail = principal.getName();
        Appointment dbAppointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("appointment", "id", id));

        if(!dbAppointment.getUser().getEmail().equals(loggedInEmail)) {
            throw new SecurityException("cannot delete other user's appointment");
        }

        appointmentRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
