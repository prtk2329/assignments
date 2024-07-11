package com.getyourguide.demo.service;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.repository.SearchRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    private final SearchRepository searchRepository;

    public ActivityService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public PaginatedResponse<Activity> searchActivities(SearchCriteria searchCriteria) {
        return searchRepository.search(searchCriteria);
    }
}
