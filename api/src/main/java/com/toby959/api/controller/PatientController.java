package com.toby959.api.controller;

import com.toby959.api.domain.patient.*;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
}
