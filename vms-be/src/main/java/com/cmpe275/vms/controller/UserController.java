package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.*;
import com.cmpe275.vms.payload.AdminReport;
import com.cmpe275.vms.payload.PatientReport;
import com.cmpe275.vms.payload.UserRequest;
import com.cmpe275.vms.repository.AppointmentRepository;
import com.cmpe275.vms.repository.ClinicRepository;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.security.CurrentUser;
import com.cmpe275.vms.security.UserPrincipal;
import com.cmpe275.vms.util.MailUtil;
import com.cmpe275.vms.util.RandomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@PreAuthorize("hasAuthority('PATIENT') or hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @GetMapping("/me")
    public ResponseEntity<com.cmpe275.vms.model.User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userPrincipal.getUsername())));
    }

    @GetMapping("/report")
    public ResponseEntity getPatientReport(@CurrentUser UserPrincipal principal, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date1, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date2) {
        String email = principal.getUsername();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user", "email", email));


        List<Object[]> appointmentList = appointmentRepository.findAppointmentsBetweenDates(date1.toString(), date2.toString(), user.getMrn());

        int total = appointmentList.size();
        long totalNoShow = appointmentList.stream().filter((Object[] a) -> a[1].toString().equals(AppointmentStatus.NOSHOW.toString())).count();

        double noShowRate = 0.0;
        if(total != 0) {
            noShowRate = (double) totalNoShow / (double) total;
        }

        PatientReport report = new PatientReport(user, (int)totalNoShow, total, noShowRate);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/adminReport")
    public ResponseEntity getAdminReport(@CurrentUser UserPrincipal principal,@RequestParam Integer clinicId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date1, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date2) {

        Clinic clinic = clinicRepository.findById(clinicId).orElseThrow(() -> new ResourceNotFoundException("clinic", "id", clinicId));

        List<Object[]> appointmentList = appointmentRepository.findClinicAppointmentsBetweenDates(date1.toString(), date2.toString(), clinicId);

        int total = appointmentList.size();
        long totalNoShow = appointmentList.stream().filter((Object[] a) -> a[1].toString().equals(AppointmentStatus.NOSHOW.toString())).count();

        double noShowRate = 0.0;
        if(total != 0) {
            noShowRate = (double) totalNoShow / (double) total;
        }

        AdminReport report = new AdminReport(clinic, (int)totalNoShow, total, noShowRate);

        return ResponseEntity.ok(report);
    }

    @PutMapping("/me")
    @Transactional
    public ResponseEntity<com.cmpe275.vms.model.User> updateCurrentUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody UserRequest user) {
        User loggedInUser = userRepository.findByEmail(userPrincipal.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "email", userPrincipal.getUsername()));

        loggedInUser.setFirstName(user.getFirstName());
        loggedInUser.setLastName(user.getLastName());
        loggedInUser.setMiddleName(user.getMiddleName());
        loggedInUser.setAddress(user.getAddress());
        loggedInUser.setDateOfBirth(user.getDateOfBirth());
        loggedInUser.setGender(user.getGender());

        User updatedUser = userRepository.save(loggedInUser);

        return ResponseEntity.ok(updatedUser);
    }
}
