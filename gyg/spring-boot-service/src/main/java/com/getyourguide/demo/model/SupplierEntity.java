package com.getyourguide.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "suppliers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierEntity {
    @Id
    private Long id;
    private String name;
    private String address;
    private String zip;
    private String city;
    private String country;

    public String getLocation() {
        return String.format("%s, %s, %s, %s", address, city, zip, country);
    }
}
