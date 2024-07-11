package com.getyourguide.demo;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.exception.RestExceptionHandler;
import com.getyourguide.demo.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.getyourguide.demo.utils.TestUtils.getActivities;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ActivityControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ActivityService activityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ActivityController(activityService))
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void testSearchShouldReturnActivities() throws Exception {
        List<Activity> expectedActivities = getActivities();

        PaginatedResponse<Activity> response = new PaginatedResponse(expectedActivities, 2L, 1, 1, 2, false, false);
        SearchCriteria searchCriteria = new SearchCriteria("Tour", 1, 10);

        when(activityService.searchActivities(searchCriteria)).thenReturn(response);

        mockMvc.perform(get("/activity/search")
                        .param("query", "Tour")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(expectedActivities.get(0).getId()))
                .andExpect(jsonPath("$.content[0].title").value(expectedActivities.get(0).getTitle()))
                .andExpect(jsonPath("$.content[1].id").value(expectedActivities.get(1).getId()))
                .andExpect(jsonPath("$.content[1].title").value(expectedActivities.get(1).getTitle()))
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalHits").value(2))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));
    }

    @Test
    public void testSearch_NoResults() throws Exception {
        PaginatedResponse<Activity> response = new PaginatedResponse(List.of(), 0L, 0, 1, 0, false, false);

        when(activityService.searchActivities(any(SearchCriteria.class))).thenReturn(response);

        mockMvc.perform(get("/activity/search")
                        .param("query", "Tour")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalHits").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.pageSize").value(0))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));
    }

    @Test
    public void testSearch_InvalidParameters() throws Exception {
        mockMvc.perform(get("/activity/search")
                        .param("page", "-1") // Invalid page number
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.page").value("Page number should not be less than 1"));
    }
}

