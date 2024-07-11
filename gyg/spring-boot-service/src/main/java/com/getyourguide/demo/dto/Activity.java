package com.getyourguide.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    private Long id;
    private String title;
    private double price;
    private String currency;
    private double rating;
    private boolean specialOffer;
    private String supplierName;
    private String location;
}
