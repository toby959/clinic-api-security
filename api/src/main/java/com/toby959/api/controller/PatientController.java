package com.toby959.api.controller;

import com.toby959.api.domain.address.DataAddress;
import com.toby959.api.domain.patient.*;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final IPatientRepository repository;

    public PatientController(IPatientRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public void registerPatient(@RequestBody @Valid PatientRecordData data) {
        repository.save(new Patient(data));
    }

    @GetMapping
    public Page<DataListPatient> listPatient(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(DataListPatient::new);
    }

    @PutMapping("/{id}")
    @Transactional
    public void updatePatient(@RequestBody @Valid DataUpdatePatient data, @PathVariable Long id) {
        var patient = repository.getReferenceById(id);
        patient.updateData(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletePatient(@PathVariable Long id) {
        var patient = repository.getReferenceById(id);
        patient.deactivate();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseData> returnPatientData(@PathVariable Long id) {
        Patient patient = repository.getReferenceById(id);
        var dataPatient = new PatientResponseData(patient.getId(), patient.getName()
                , patient.getEmail(), patient.getPhone(), patient.getDocument(),
                new DataAddress(patient.getAddress().getStreet(), patient.getAddress().getDistrict(),
                        patient.getAddress().getCity(), patient.getAddress().getNumber(),
                        patient.getAddress().getAddition()));
        return ResponseEntity.ok(dataPatient);
    }

}
