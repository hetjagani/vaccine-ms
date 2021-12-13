package com.cmpe275.vms.controller;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.Disease;
import com.cmpe275.vms.model.Vaccine;
import com.cmpe275.vms.payload.VaccineRequest;
import com.cmpe275.vms.repository.DiseaseRepository;
import com.cmpe275.vms.repository.VaccineRepository;

// left to add the authorization logic for vaccines endpoint
@RestController
public class VaccineController {
    
	@Autowired
    private VaccineRepository vaccineRepository;
    
	@Autowired
	private DiseaseRepository diseaseRepository;
	
    @GetMapping("/vaccines/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<com.cmpe275.vms.model.Vaccine> getDiseaseById(@PathVariable Integer id){
    	return ResponseEntity.ok(vaccineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", id)));
    }
    
    @PostMapping("/vaccines")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineRequest vaccine){
    	Vaccine dbVaccine = new Vaccine(vaccine.getName(),vaccine.getManufacturer(),vaccine.getNumOfShots(),vaccine.getShotInterval(),vaccine.getDuration() );
    	List<Disease> diseaseList = diseaseRepository.findAllById(vaccine.getDiseaseIdList());
    	
    	if(diseaseList.size() != vaccine.getDiseaseIdList().size()) {
    		throw new ResourceNotFoundException("Disease", "ids", vaccine.getDiseaseIdList());
    	}
    	
    	Set<Disease> diseaseSet = new HashSet<Disease>(diseaseList);
    	
    	dbVaccine.setDiseases(diseaseSet);
    	Vaccine createdVaccine = vaccineRepository.save(dbVaccine);
    	
    	for(Disease d : createdVaccine.getDiseases()) {
    		d.getVaccines().add(createdVaccine);
    		diseaseRepository.save(d);
    	}
    	
    	return new ResponseEntity<>(createdVaccine, HttpStatus.CREATED);
    }

    @PutMapping("/vaccines/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<com.cmpe275.vms.model.Vaccine> updateVaccine(@PathVariable Integer id,@Valid @RequestBody VaccineRequest vaccine){
    	Optional<Vaccine> optVaccine = vaccineRepository.findById(id);
    	if(optVaccine.isEmpty()) {
    		throw new ResourceNotFoundException("Vaccine", "id", id);
    	}
    	Vaccine existVaccine = optVaccine.get();
    	// uncomment the below line to not allow user to set name
    	existVaccine.setName(vaccine.getName());
    	existVaccine.setDuration(vaccine.getDuration());
    	existVaccine.setManufacturer(vaccine.getManufacturer());
    	existVaccine.setNumOfShots(vaccine.getNumOfShots());
    	existVaccine.setShotInterval(vaccine.getShotInterval());
    	
    	List<Disease> diseaseList = diseaseRepository.findAllById(vaccine.getDiseaseIdList());
    	
    	if(diseaseList.size() != vaccine.getDiseaseIdList().size()) {
    		throw new ResourceNotFoundException("Disease", "ids", vaccine.getDiseaseIdList());
    	}
    	
    	Set<Disease> diseaseSet = new HashSet<Disease>(diseaseList);
    	
    	existVaccine.setDiseases(diseaseSet);
    	
    	Vaccine updatedVaccine = vaccineRepository.save(existVaccine);
    	
    	
    	return ResponseEntity.ok(updatedVaccine);
    }
    
    @DeleteMapping("/vaccines/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> deleteVaccine(@PathVariable Integer id){
    	Optional<Vaccine> optVaccine = vaccineRepository.findById(id);
    	if(optVaccine.isEmpty()) {
    		throw new ResourceNotFoundException("Vaccine", "id", id);
    	}
    	
        vaccineRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
