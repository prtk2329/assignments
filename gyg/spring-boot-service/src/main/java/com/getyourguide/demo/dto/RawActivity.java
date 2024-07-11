package com.getyourguide.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawActivity {
    private Long id;
    private String title;
    private double price;
    private String currency;
    private double rating;
    private boolean specialOffer;
    private Long supplierId;

    public ActivityEntity toActivityEntity(SupplierEntity supplier) {
        return ActivityEntity.builder()
                .id(id)
                .title(title)
                .price(price)
                .currency(currency)
                .rating(rating)
                .specialOffer(specialOffer)
                .supplier(supplier)
                .build();
    }
}
