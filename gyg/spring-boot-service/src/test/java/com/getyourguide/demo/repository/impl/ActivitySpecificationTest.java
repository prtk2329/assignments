package com.getyourguide.demo.repository.impl;

import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.model.ActivityEntity;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ActivitySpecificationTest {
    private Root<ActivityEntity> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;

    @BeforeEach
    public void setup() {
        criteriaBuilder = mock(CriteriaBuilder.class);
        query = mock(CriteriaQuery.class);
        root = mock(Root.class);
    }

    @Test
    public void testBuildSearchSpecification_WithQuery() {
        SearchCriteria searchCriteria = new SearchCriteria("tour", 1, 10);

        Path titlePathMock = mock(Path.class);
        when(root.get("title")).thenReturn(titlePathMock);

        Expression titleToLowerExpressionMock = mock(Expression.class);
        when(criteriaBuilder.lower(titlePathMock)).thenReturn(titleToLowerExpressionMock);

        Predicate titleIsLikePredicateMock = mock(Predicate.class);
        when(criteriaBuilder.like(titleToLowerExpressionMock, "%tour%")).thenReturn(titleIsLikePredicateMock);

        Predicate titleIsLikeAndPredicateMock = Mockito.mock(Predicate.class);
        when(criteriaBuilder.and(titleIsLikePredicateMock)).thenReturn(titleIsLikeAndPredicateMock);

        Specification<ActivityEntity> specification = ActivitySpecification.buildSearchSpecification(searchCriteria);

        Predicate actualPredicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(root, times(1)).get("title");
        verifyNoMoreInteractions(root);

        verify(criteriaBuilder, times(1)).lower(titlePathMock);
        verify(criteriaBuilder, times(1)).like(titleToLowerExpressionMock, "%tour%");
        verify(criteriaBuilder, times(1)).and(titleIsLikePredicateMock);
        verifyNoMoreInteractions(criteriaBuilder);

        verifyNoInteractions(query, titlePathMock, titleToLowerExpressionMock, titleIsLikePredicateMock, titleIsLikeAndPredicateMock);

        assertEquals(titleIsLikeAndPredicateMock, actualPredicate);
    }

    @Test
    public void testBuildSearchSpecification_WithEmptyQuery() {
        SearchCriteria searchCriteria = new SearchCriteria("", 1, 10);

        Predicate andPredicateMock = Mockito.mock(Predicate.class);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(andPredicateMock);

        Specification<ActivityEntity> specification = ActivitySpecification.buildSearchSpecification(searchCriteria);
        Predicate actualPredicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder, times(1)).and(any(Predicate[].class));
        verifyNoMoreInteractions(criteriaBuilder);

        verifyNoInteractions(query, root);

        assertEquals(andPredicateMock, actualPredicate);
    }

}
