package com.getyourguide.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "activities", indexes = {
        @Index(name = "idx_title", columnList = "title")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityEntity {
    @Id
    private Long id;
    private String title;
    private double price;
    private String currency;
    private double rating;
    private boolean specialOffer;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;
}
