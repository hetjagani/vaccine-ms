package com.cmpe275.vms.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.Disease;
import com.cmpe275.vms.payload.DiseaseRequest;
import com.cmpe275.vms.repository.DiseaseRepository;

// left to add the authorization logic for diseases endpoint
@RestController
public class DiseaseController {
    
	@Autowired
    private DiseaseRepository diseaseRepository;
    
    @GetMapping("/diseases/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<com.cmpe275.vms.model.Disease> getDiseaseById(@PathVariable Integer id){
    	return ResponseEntity.ok(diseaseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Disease", "id", id)));
    }
    
    @PostMapping("/diseases")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> createDisease(@Valid @RequestBody DiseaseRequest disease){
    	Disease dbDisease = new Disease(disease.getName(), disease.getDescription());
    	Disease createdDisease = diseaseRepository.save(dbDisease);
    	return new ResponseEntity<>(createdDisease, HttpStatus.CREATED);
    }

    @PutMapping("/diseases/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<com.cmpe275.vms.model.Disease> updateDisease(@PathVariable Integer id,@Valid @RequestBody DiseaseRequest disease){
    	Optional<Disease> optDisease = diseaseRepository.findById(id);
    	if(optDisease.isEmpty()) {
    		throw new ResourceNotFoundException("Disease", "id", id);
    	}
    	Disease existDisease = optDisease.get();
    	existDisease.setDescription(disease.getDescription());
    	// only add the below line if name can be updated
    	existDisease.setName(disease.getName());
    	
    	Disease updatedDisease = diseaseRepository.save(existDisease);
    	return ResponseEntity.ok(updatedDisease);
    }
    
    @DeleteMapping("/diseases/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> deleteDisease(@PathVariable Integer id){
    	Optional<Disease> optDisease = diseaseRepository.findById(id);
    	if(optDisease.isEmpty()) {
    		throw new ResourceNotFoundException("Disease", "id", id);
    	}
    	
        diseaseRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
