package com.toby959.api.controller;

import com.toby959.api.domain.address.DataAddress;
import com.toby959.api.domain.doctor.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearer-key")
public class DoctorController {

    private final IDoctorRepository repository;


    public DoctorController(IDoctorRepository repository) {
        this.repository = repository;
    }

    @PostMapping()
    @Operation(
            summary = "Register a new Doctor",
            description = "Creates a new doctor record and returns the created doctor's details.",
            tags = {"Doctor"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Medical record data for the new doctor",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MedicalRecordData.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                          responseCode = "201",
                          description = "Doctor registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DoctorResponseData.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Doctor already exists",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
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


    @GetMapping
    @Operation(
            summary = "List active doctors",
            description = "Retrieves a paginated list of active doctors.",
            tags = {"Doctor"},
            parameters = {
                    @Parameter(name = "page", description = "Page number to retrieve (0-based)", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Number of records per page", required = false, schema = @Schema(type = "integer", defaultValue = "4")),
                    @Parameter(name = "sort", description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", required = false)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of active doctors",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No active doctors found",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Page<DataListDoctors>> listDoctors(@PageableDefault(size = 4) Pageable pageable) {

        return ResponseEntity.ok(repository.findByActiveTrue(pageable).map(DataListDoctors::new));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Update an existing doctor",
            description = "Updates the details of an existing doctor identified by the given ID.",
            tags = {"Doctor"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the doctor to be updated", required = true, schema = @Schema(type = "integer"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated data for the doctor",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataUpDateDoctor.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Doctor updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DoctorResponseData.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Doctor not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content
                    )
            }
    )
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
    @Operation(
            summary = "Delete a doctor",
            description = "Deactivates a doctor record identified by the given ID.",
            tags = {"Doctor"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the doctor to be deleted", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Doctor successfully deactivated"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Doctor not found",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        Doctor doctor = repository.getReferenceById(id);
        doctor.deactivate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve medical data of a doctor",
            description = "Returns the medical data of a doctor identified by the given ID.",
            tags = {"Doctor"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the doctor whose medical data is to be retrieved", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of doctor data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DoctorResponseData.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Doctor not found",
                            content = @Content
                    )
            }
    )
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