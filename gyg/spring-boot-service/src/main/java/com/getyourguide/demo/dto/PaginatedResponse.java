package com.getyourguide.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> content;
    private Long totalHits;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasPrevious;
    private boolean hasNext;
}
