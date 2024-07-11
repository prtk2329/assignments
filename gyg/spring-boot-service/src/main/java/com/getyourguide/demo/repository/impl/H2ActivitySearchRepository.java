package com.getyourguide.demo.repository.impl;

import com.getyourguide.demo.converter.ActivityConverter;
import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.repository.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class H2ActivitySearchRepository implements SearchRepository {

    private final ActivityRepository activityRepository;

    public H2ActivitySearchRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public PaginatedResponse<Activity> search(SearchCriteria searchCriteria) {
        Specification<ActivityEntity> specification = ActivitySpecification.buildSearchSpecification(searchCriteria);
        Pageable pageable = PageRequest.of(searchCriteria.getPage() - 1, searchCriteria.getSize());

        Page<ActivityEntity> activityPage = activityRepository.findAll(specification, pageable);

        List<Activity> activities = activityPage.getContent().stream()
                .map(ActivityConverter::toActivity)
                .toList();

        return new PaginatedResponse<>(
                activities,
                activityPage.getTotalElements(),
                activityPage.getTotalPages(),
                activityPage.getNumber() + 1,
                activityPage.getNumberOfElements(),
                activityPage.hasPrevious(),
                activityPage.hasNext()
        );
    }
}
