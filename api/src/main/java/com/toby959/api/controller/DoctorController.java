package com.toby959.api.controller;

import com.toby959.api.domain.address.DataAddress;
import com.toby959.api.domain.doctor.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("api/v1/doctors")
public class DoctorController {

    private final IDoctorRepository repository;


    public DoctorController(IDoctorRepository repository) {
        this.repository = repository;
    }

    @PostMapping()               // origin
    //@Secured("ROLE_ADMIN")     -- ejercicio video --
    //@PostMapping("/register")               // -- chat --
    //@PreAuthorize("hasRole('ROL_ADMIN')")   // -- chat --
    public ResponseEntity<DoctorResponseData> registerDoctor(@RequestBody @Valid MedicalRecordData medicalRecordData,
                                                             UriComponentsBuilder uriComponentsBuilder) {
       Doctor doctor = new Doctor(medicalRecordData);
       doctor = repository.save(doctor);

        DoctorResponseData doctorResponseData = new DoctorResponseData(
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getDocument(),
                new DataAddress(doctor.getAddress().getStreet(), doctor.getAddress().getDistrict(),
                        doctor.getAddress().getCity(), doctor.getAddress().getNumber(),
                        doctor.getAddress().getAddition()));
        final Boolean active = doctor.getActive();

        URI url = uriComponentsBuilder.path("/api/v1/doctors/{id}").buildAndExpand(doctor.getId()).toUri();
        return ResponseEntity.created(url).body(doctorResponseData);
    }

//    @PostMapping()
//    @Secured("ROLE_ADMIN")
//    public ResponseEntity<DoctorResponseData> registerDoctor(@RequestBody @Valid MedicalRecordData medicalRecordData,
//                                                             UriComponentsBuilder uriComponentsBuilder) {
//        Doctor doctor = repository.save(new Doctor(medicalRecordData));
//        DoctorResponseData doctorResponseData = new DoctorResponseData(doctor.getId(), doctor.getName()
//                , doctor.getEmail(), doctor.getPhone(), doctor.getSpecialty().toString(),
//                new DataAddress(doctor.getAddress().getStreet(), doctor.getAddress().getDistrict(),
//                        doctor.getAddress().getCity(), doctor.getAddress().getNumber(),
//                        doctor.getAddress().getAddition()));
//        URI url = uriComponentsBuilder.path("/api/v1/doctors/{id}").buildAndExpand(doctor.getId()).toUri();
//        return ResponseEntity.created(url).body(doctorResponseData);
//    }

    @GetMapping
    public ResponseEntity<Page<DataListDoctors>> listDoctors(@PageableDefault(size = 4) Pageable pageable) {

        return ResponseEntity.ok(repository.findByActiveTrue(pageable).map(DataListDoctors::new));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DoctorResponseData> updateDoctor(@PathVariable Long id, @RequestBody DataUpDateDoctor dataUpDateDoctor) {

        Doctor doctor = repository.getReferenceById(dataUpDateDoctor.id());
        doctor.updateData(dataUpDateDoctor);
        return ResponseEntity.ok(new DoctorResponseData(doctor.getId(), doctor.getName()
                , doctor.getEmail(), doctor.getPhone(), doctor.getSpecialty().toString(),
                new DataAddress(doctor.getAddress().getStreet(), doctor.getAddress().getDistrict(),
                        doctor.getAddress().getCity(), doctor.getAddress().getNumber(),
                        doctor.getAddress().getAddition())));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        Doctor doctor = repository.getReferenceById(id);
        doctor.deactivate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseData> returnMedicalData(@PathVariable Long id) {
        Doctor doctor = repository.getReferenceById(id);
        var dataDoctor = new DoctorResponseData(doctor.getId(), doctor.getName()
                , doctor.getEmail(), doctor.getPhone(), doctor.getSpecialty().toString(),
                new DataAddress(doctor.getAddress().getStreet(), doctor.getAddress().getDistrict(),
                        doctor.getAddress().getCity(), doctor.getAddress().getNumber(),
                        doctor.getAddress().getAddition()));
        return ResponseEntity.ok(dataDoctor);
    }
}