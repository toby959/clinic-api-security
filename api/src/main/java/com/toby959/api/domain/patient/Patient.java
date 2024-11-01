package com.toby959.api.domain.patient;


import com.toby959.api.domain.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Patient")
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String document;

    @Embedded
    private Address address;

    private Boolean active;

//    @Enumerated(EnumType.STRING)
//    private Role role;

//    public static enum Role {
//        ROL_ADMIN,
//        ROL_USER,
//        ROL_INVITED
//    }

    public Patient(PatientRecordData data) {
        this.active = true;
        this.name = data.name();
        this.email = data.email();
        this.phone = data.phone();
        this.document = data.document();
        this.address = new Address(data.address());
    }

    public void updateData(DataUpdatePatient data) {
        if (data.name() != null) {
            this.name = data.name();
        }
        if (data.phone() != null) {
            this.phone = data.phone();
        }
        if (data.address() != null) {
            this.address.updateData(data.address());
        }
    }


    public void deactivate() {
        this.active = false;
    }
}




