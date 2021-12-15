package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.BadRequestException;
import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.*;
import com.cmpe275.vms.payload.AppointmentRequest;
import com.cmpe275.vms.payload.AppointmentSlotsResp;
import com.cmpe275.vms.repository.AppointmentRepository;
import com.cmpe275.vms.repository.ClinicRepository;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    @GetMapping("/slots")
    public ResponseEntity<List<AppointmentSlotsResp>> getAllSlots(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date, @RequestParam String clinicId){
        if(date==null && clinicId==null) {
        	throw new BadRequestException("Must have date and clinicId query parameter");
        }
        
    	Clinic clinic = clinicRepository.findById(Integer.parseInt(clinicId)).orElseThrow(() -> new ResourceNotFoundException("clinic", "id", clinicId));
        
        List<LocalTime> timeSlots = new ArrayList<LocalTime>();
        LocalTime current = clinic.getStartTime();
        while(current.isBefore(clinic.getEndTime())) {
        	timeSlots.add(current);
        	current = current.plusMinutes(15);
        }
        
        // query to fetch all the appointments done for the date and the clinic
        List<Object[]> dbExistAppointments = appointmentRepository.findAllSlotsForDateAndClinic(Integer.parseInt(clinicId), date.toString());
    
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(Object[] asp: dbExistAppointments) {
        	map.put(asp[0].toString().substring(0, 5), Integer.parseInt(asp[1].toString()));
        }
        
        List<AppointmentSlotsResp> allSlots = new ArrayList<AppointmentSlotsResp>();
        DateTimeFormatter dmf = DateTimeFormatter.ofPattern("HH:mm");
        for(LocalTime lt: timeSlots) {
        	
        	if(map.containsKey(lt.toString())) {
        		int val = clinic.getNumberOfPhysicians() - map.get(lt.toString());
        		if(val > 0)
        			allSlots.add(new AppointmentSlotsResp(lt, val));
        	}else {
        		allSlots.add(new AppointmentSlotsResp(lt, clinic.getNumberOfPhysicians()));
        	}
        }
        
        return ResponseEntity.ok(allSlots);
    }
    
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(@RequestParam(required=false) String past, @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        System.out.println(date);
    	List<Appointment> appointments = new ArrayList<Appointment>();
        if(date!=null && past==null) {
            LocalDate ld1 = date.plusMonths(12);
            LocalDate ld2 = date.plusMonths(24);
            appointments = appointmentRepository.findNext12MonthsAppointments(ld1.toString(),ld2.toString());
        	
        }else if((date==null) && (past.equalsIgnoreCase("false") || past==null)) {
        	appointments = appointmentRepository.findAllFutureAppointments();
        }else {
        	appointments = appointmentRepository.findAllPastAppointments();
        }
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

        // just check whether the number of vaccines are less than 4 or not for right now logic
        if(request.getVaccineIds().size() > 4) {
        	throw new BadRequestException("Can't have more than four vaccines in single appointment");
        }
        
        // check whether the appointment is within the 12 months from current time or not
        LocalDate dateC = request.getDate();
        LocalDate dateF = LocalDate.now().plusMonths(12);
        
        if(dateC.isAfter(dateF)) {
        	throw new BadRequestException("Can't opt for appointments date after 12 months");
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
