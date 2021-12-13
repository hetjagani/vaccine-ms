package com.cmpe275.vms.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.Clinic;
import com.cmpe275.vms.payload.ClinicRequest;
import com.cmpe275.vms.repository.ClinicRepository;

// left to add the authorization logic for diseases endpoint
@RestController
@RequestMapping(path = "/clinics")
@PreAuthorize("hasAuthority('PATIENT') or hasAuthority('ADMIN')")
public class ClinicController {
    
	@Autowired
    private ClinicRepository clinicRepository;

    @GetMapping
    public ResponseEntity<List<Clinic>> getAllClinics(){
        List<Clinic> clinics = clinicRepository.findAll();
        return ResponseEntity.ok(clinics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable Integer id){
    	return ResponseEntity.ok(clinicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clinic", "id", id)));
    }
    
    @PostMapping
    public ResponseEntity<?> createClinic(@Valid @RequestBody ClinicRequest clinic){
    	DateTimeFormatter dmf = DateTimeFormatter.ofPattern("HH:mm");
    	Clinic dbClinic = new Clinic(clinic.getName(), clinic.getAddress(), LocalTime.parse(clinic.getStartTime(),dmf), LocalTime.parse(clinic.getEndTime(),dmf), clinic.getNumberOfPhysicians());
    	Clinic createdClinic = clinicRepository.save(dbClinic);
    	return new ResponseEntity<>(createdClinic, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clinic> updateClinic(@PathVariable Integer id,@Valid @RequestBody ClinicRequest clinic){
    	DateTimeFormatter dmf = DateTimeFormatter.ofPattern("HH:mm");
    	Optional<Clinic> optClinic = clinicRepository.findById(id);
    	if(optClinic.isEmpty()) {
    		throw new ResourceNotFoundException("Clinic", "id", id);
    	}
    	
    	Clinic existClinic = optClinic.get();
    	existClinic.setAddress(clinic.getAddress());
    	existClinic.setName(clinic.getName());
    	existClinic.setStartTime(LocalTime.parse(clinic.getStartTime(),dmf));
    	existClinic.setEndTime(LocalTime.parse(clinic.getEndTime(),dmf));
    	existClinic.setNumberOfPhysicians(clinic.getNumberOfPhysicians());
    	
    	Clinic updatedClinic = clinicRepository.save(existClinic);
    	return ResponseEntity.ok(updatedClinic);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClinic(@PathVariable Integer id){
    	Optional<Clinic> optClinic = clinicRepository.findById(id);
    	if(optClinic.isEmpty()) {
    		throw new ResourceNotFoundException("Clinic", "id", id);
    	}
    	
        clinicRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
