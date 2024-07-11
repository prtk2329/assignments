package com.getyourguide.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String query;

    @Min(value = 1, message = "Page number should not be less than 1")
    private int page = 1; // By default, page starts from 1
    private int size = 10; // By default, page size is 10
}
