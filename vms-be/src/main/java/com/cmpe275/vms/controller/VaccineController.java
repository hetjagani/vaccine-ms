package com.cmpe275.vms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.Disease;
import com.cmpe275.vms.model.User;
import com.cmpe275.vms.model.Vaccine;
import com.cmpe275.vms.payload.VaccineDueResp;
import com.cmpe275.vms.payload.VaccineRequest;
import com.cmpe275.vms.repository.AppointmentRepository;
import com.cmpe275.vms.repository.DiseaseRepository;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.repository.VaccineRepository;
import com.cmpe275.vms.security.CurrentUser;
import com.cmpe275.vms.security.UserPrincipal;

// left to add the authorization logic for vaccines endpoint
@RestController
@RequestMapping(path = "/vaccines")
@PreAuthorize("hasAuthority('PATIENT') or hasAuthority('ADMIN')")
public class VaccineController {
    
	@Autowired
    private VaccineRepository vaccineRepository;

	@Autowired
	private DiseaseRepository diseaseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@GetMapping
	public ResponseEntity<List<VaccineDueResp>> getAllVaccinesDue(@CurrentUser UserPrincipal userPrincipal){
		Optional<User> oneUser = userRepository.findByEmail(userPrincipal.getName());
		
		if(oneUser.isEmpty()) {
			throw new ResourceNotFoundException("Email", userPrincipal.getName(), "not Exists");
		}
		
		User user = oneUser.get();
		
		List<Object[]> vaccineList = appointmentRepository.findUserVaccineShotsTaken(user.getMrn());
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(Object[] v : vaccineList) {
			map.put(Integer.parseInt(v[0].toString()), Integer.parseInt(v[1].toString()));
		}
		
		List<Vaccine> vaccinesList = vaccineRepository.findAll();
		
		List<VaccineDueResp> vaccineResp = new ArrayList<VaccineDueResp>();
		
		for(Vaccine v : vaccinesList) {
			if(map.containsKey(v.getId())) {
				int val = v.getNumOfShots()-map.get(v.getId());
				if(val > 0)
					vaccineResp.add(new VaccineDueResp(v.getName(),v.getManufacturer(),v.getNumOfShots(),v.getShotInterval(),v.getDuration(),map.get(v.getId()-val)));
			}
			vaccineResp.add(new VaccineDueResp(v.getName(),v.getManufacturer(),v.getNumOfShots(),v.getShotInterval(),v.getDuration(),1));
		}
		
		return ResponseEntity.ok(vaccineResp);
	}
	
	@GetMapping
	public ResponseEntity<List<Vaccine>> getAllVaccines(){
		List<Vaccine> vaccineList = vaccineRepository.findAll();
		return ResponseEntity.ok(vaccineList);
	}

	@GetMapping("/{id}")
    public ResponseEntity<com.cmpe275.vms.model.Vaccine> getVaccineById(@PathVariable Integer id){
    	return ResponseEntity.ok(vaccineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vaccine", "id", id)));
    }
    
    @PostMapping
    public ResponseEntity<?> createVaccine(@Valid @RequestBody VaccineRequest vaccine){
    	Vaccine dbVaccine = new Vaccine(vaccine.getName(),vaccine.getManufacturer(),vaccine.getNumOfShots(),vaccine.getShotInterval(),vaccine.getDuration() );
    	List<Disease> diseaseList = diseaseRepository.findAllById(vaccine.getDiseaseIds());
    	

    	dbVaccine.setDiseases(diseaseList);
    	Vaccine createdVaccine = vaccineRepository.save(dbVaccine);

    	return new ResponseEntity<>(createdVaccine, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.cmpe275.vms.model.Vaccine> updateVaccine(@PathVariable Integer id,@Valid @RequestBody VaccineRequest vaccine){
    	Optional<Vaccine> optVaccine = vaccineRepository.findById(id);
    	if(optVaccine.isEmpty()) {
    		throw new ResourceNotFoundException("Vaccine", "id", id);
    	}
    	Vaccine existVaccine = optVaccine.get();
    	existVaccine.setName(vaccine.getName());
    	existVaccine.setDuration(vaccine.getDuration());
    	existVaccine.setManufacturer(vaccine.getManufacturer());
    	existVaccine.setNumOfShots(vaccine.getNumOfShots());
    	existVaccine.setShotInterval(vaccine.getShotInterval());
    	
    	List<Disease> diseaseList = diseaseRepository.findAllById(vaccine.getDiseaseIds());
    	existVaccine.setDiseases(diseaseList);

    	Vaccine updatedVaccine = vaccineRepository.save(existVaccine);

    	return ResponseEntity.ok(updatedVaccine);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVaccine(@PathVariable Integer id){
    	Optional<Vaccine> optVaccine = vaccineRepository.findById(id);
    	if(optVaccine.isEmpty()) {
    		throw new ResourceNotFoundException("Vaccine", "id", id);
    	}
    	
        vaccineRepository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
