package com.getyourguide.demo.repository.impl;

import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.model.ActivityEntity;
import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ActivitySpecification {

    public static Specification<ActivityEntity> buildSearchSpecification(SearchCriteria searchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Strings.isNotEmpty(searchCriteria.getQuery())) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                        "%" + searchCriteria.getQuery().toLowerCase() + "%");
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
