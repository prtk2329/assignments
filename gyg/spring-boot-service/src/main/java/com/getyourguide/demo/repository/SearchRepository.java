package com.getyourguide.demo.repository;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;

public interface SearchRepository {
    PaginatedResponse<Activity> search(SearchCriteria searchCriteria);
}
