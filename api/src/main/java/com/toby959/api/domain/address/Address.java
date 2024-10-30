package com.toby959.api.domain.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String street;

    private String number;

    private String addition;

    private String district;

    private String city;


    public Address(DataAddress address) {
        this.street = address.street();
        this.number = address.number();
        this.district = address.district();
        this.addition = address.addition();
        this.city = address.city();
    }

    public Address updateData(DataAddress address) {
        this.street = address.street();
        this.number = address.number();
        this.district = address.district();
        this.addition = address.addition();
        this.city = address.city();
        return this;
    }
}
