package com.getyourguide.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.getyourguide.demo.model.SupplierEntity;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawSupplier {
    private Long id;
    private String name;
    private String address;
    private String zip;
    private String city;
    private String country;

    public SupplierEntity toSupplierEntity() {
        return SupplierEntity.builder()
                .id(id)
                .name(name)
                .address(address)
                .zip(zip)
                .city(city)
                .country(country)
                .build();
    }
}
