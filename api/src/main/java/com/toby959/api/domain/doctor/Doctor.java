package com.toby959.api.domain.doctor;

import com.toby959.api.domain.address.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "doctor")
@Table(name = "doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String document;

    private Boolean active;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;


    @Embedded
    private Address address;


    public Doctor(MedicalRecordData medicalRecordData) {
        this.name = medicalRecordData.name();
        this.email = medicalRecordData.email();
        this.phone = medicalRecordData.phone();
        this.document = medicalRecordData.document();
        this.active = true;
        this.specialty = medicalRecordData.specialty();
        this.address = new Address(medicalRecordData.address());
    }

    public void updateData(DataUpDateDoctor dataUpDateDoctor) {
        if (dataUpDateDoctor.name() != null) {
            this.name = dataUpDateDoctor.name();
        }
        if (dataUpDateDoctor.document() != null) {
            this.document = dataUpDateDoctor.document();
        }
        if (dataUpDateDoctor.email() != null) {
            this.email = dataUpDateDoctor.email();
        }
        if (dataUpDateDoctor.address() != null) {
            this.address = address.updateData(dataUpDateDoctor.address());
        }
    }

    public void deactivate() {

        this.active = false;
    }
}
