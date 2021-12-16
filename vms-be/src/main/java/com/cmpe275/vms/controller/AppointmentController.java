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
import com.cmpe275.vms.security.CurrentUser;
import com.cmpe275.vms.security.UserPrincipal;

import com.cmpe275.vms.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Appointment>> getAllAppointments(@CurrentUser UserPrincipal userPrincipal, @RequestParam(required=false) String past, @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        System.out.println(date);
        Optional<User> optUser = userRepository.findByEmail(userPrincipal.getUsername());
        
        if(optUser.isEmpty()) {
        	throw new ResourceNotFoundException("User with Email:", userPrincipal.getUsername(), "not found");
        }
        
        User user = optUser.get();
    	List<Appointment> appointments = new ArrayList<Appointment>();
        if(date!=null && past==null) {
            LocalDate ld1 = date.plusMonths(12);
            System.out.println();
            appointments = appointmentRepository.findNext12MonthsAppointments(date.toString(),ld1.toString(),LocalTime.now().toString(),user.getMrn());
        	
        }else if((date==null) && (past.equalsIgnoreCase("false") || past==null)) {
        	appointments = appointmentRepository.findAllFutureAppointments(LocalDate.now().toString(),LocalTime.now().toString(),user.getMrn());
        }else {
        	appointments = appointmentRepository.findAllPastAppointments(LocalDate.now().toString(),LocalTime.now().toString(),user.getMrn());
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

        String emailSubject = "You have an appointment on "+ request.getDate() + " for vaccination.";
        String emailText = "Congratulations, you have successfully booked an appointment for vaccination.\nBelow are the details of your appointment.\n";
        emailText += "Appointment ID: " + createdAppointment.getId() + "\n";
        emailText += "Appointment Date: " + createdAppointment.getDate() + "\n";
        emailText += "Appointment Time: " + createdAppointment.getTime() + "\n\n";
        emailText += "Appointment Status: " + createdAppointment.getStatus().toString() + "\n\n";
        emailText += "Clinic Name: " + createdAppointment.getClinic().getName() + "\n";
        emailText += "Clinic Address: " + createdAppointment.getClinic().getAddress() + "\n\n";
        emailText += "Vaccine Details: \n";
        List<Vaccine> vaccines = createdAppointment.getVaccines();
        for(int i=0; i<vaccines.size(); i++) {
            emailText += "\t"+(i+1)+". Name: "+vaccines.get(i).getName()+" Manufacturer: "+vaccines.get(i).getManufacturer()+"\n";
        }

        try {
            MailUtil.sendMail(emailText, emailSubject, user.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

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

        String emailSubject = "You have an updated your appointment on "+ request.getDate() + " for vaccination.";
        String emailText = "Congratulations, your appointment is updated.\nBelow are the details of your appointment.\n";
        emailText += "Appointment ID: " + updatedAppointment.getId() + "\n";
        emailText += "Appointment Date: " + updatedAppointment.getDate() + "\n";
        emailText += "Appointment Time: " + updatedAppointment.getTime() + "\n\n";
        emailText += "Appointment Status: " + updatedAppointment.getStatus().toString() + "\n\n";
        emailText += "Clinic Name: " + updatedAppointment.getClinic().getName() + "\n";
        emailText += "Clinic Address: " + updatedAppointment.getClinic().getAddress() + "\n\n";
        emailText += "Vaccine Details: \n";
        List<Vaccine> vaccines = updatedAppointment.getVaccines();
        for(int i=0; i<vaccines.size(); i++) {
            emailText += "\t"+(i+1)+". Name: "+vaccines.get(i).getName()+" Manufacturer: "+vaccines.get(i).getManufacturer()+"\n";
        }

        try {
            MailUtil.sendMail(emailText, emailSubject, dbAppointment.getUser().getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

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
