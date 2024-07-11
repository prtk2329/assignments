package com.getyourguide.demo.service;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.getyourguide.demo.utils.TestUtils.getActivities;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest {
    private ActivityService activityService;
    @Mock
    private SearchRepository searchRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        activityService = new ActivityService(searchRepository);
    }

    @Test
    public void testSearchActivities() {
        List<Activity> expectedActivities = getActivities();

        PaginatedResponse<Activity> response = new PaginatedResponse<>(expectedActivities, 2L, 1,
                1, 2, false, false);
        SearchCriteria searchCriteria = new SearchCriteria("tour", 1, 10);

        when(searchRepository.search(searchCriteria)).thenReturn(response);

        PaginatedResponse<Activity> page = activityService.searchActivities(searchCriteria);

        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
        assertEquals(expectedActivities, page.getContent());
        assertEquals(2L, page.getTotalHits());
        assertEquals(1, page.getTotalPages());
        assertEquals(1, page.getCurrentPage());
        assertEquals(2, page.getPageSize());
        assertFalse(page.isHasPrevious());
        assertFalse(page.isHasNext());

        verify(searchRepository, times(1)).search(searchCriteria);
    }
}
