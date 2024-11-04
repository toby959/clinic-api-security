package com.toby959.api.controller;

import com.toby959.api.domain.address.DataAddress;
import com.toby959.api.domain.patient.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@SecurityRequirement(name = "bearer-key")
public class PatientController {

    private final IPatientRepository repository;

    public PatientController(IPatientRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    @Operation(
            summary = "Register a new Patient",
            description = "Creates a new patient record and saves it to the database.",
            tags = {"Patient"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient record data to be registered",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientRecordData.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patient registered successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content
                    )
            }
    )
    public void registerPatient(@RequestBody @Valid PatientRecordData data) {
        repository.save(new Patient(data));
    }

    @GetMapping
    @Operation(
            summary = "List active Patients",
            description = "Retrieves a paginated list of active patients.",
            tags = {"Patient"},
            parameters = {
                    @Parameter(name = "page", description = "Page number to retrieve (0-based)", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "Number of records per page", required = false, schema = @Schema(type = "integer", defaultValue = "10")),
                    @Parameter(name = "sort", description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", required = false)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of active patients",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No active patients found",
                            content = @Content
                    )
            }
    )
    public ResponseEntity <Page<DataListPatient>> listPatient(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
            Page<DataListPatient> patientsPage = repository.findAllByActiveTrue(pageable).map(DataListPatient::new);

        if (patientsPage.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no hay pacientes activos
        }

        return ResponseEntity.ok(patientsPage); // Retorna 200 con la lista de pacientes
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Update an existing patient",
            description = "Updates the details of an existing patient identified by the given ID.",
            tags = {"Patient"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the patient to be updated", required = true, schema = @Schema(type = "integer"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated data for the patient",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataUpdatePatient.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patient updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PatientResponseData.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patient not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content
                    )
            }
    )
    public void updatePatient(@RequestBody @Valid DataUpdatePatient data, @PathVariable Long id) {
        var patient = repository.getReferenceById(id);
        patient.updateData(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Delete a patient",
            description = "Deactivates a patient record identified by the given ID.",
            tags = {"Patient"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the patient to be deleted", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Patient successfully deactivated"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patient not found",
                            content = @Content
                    )
            }
    )
    public void deletePatient(@PathVariable Long id) {
        var patient = repository.getReferenceById(id);
        patient.deactivate();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve patient data",
            description = "Returns the data of a patient identified by the given ID.",
            tags = {"Patient"},
            parameters = {
                    @Parameter(name = "id", description = "ID of the patient whose data is to be retrieved", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of patient data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PatientResponseData.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patient not found",
                            content = @Content
                    )
            }
    )
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
