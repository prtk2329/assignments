package com.getyourguide.demo;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<Activity>> search(@Valid @ModelAttribute SearchCriteria searchCriteria) {
        PaginatedResponse<Activity> response = activityService.searchActivities(searchCriteria);
        return ResponseEntity.ok(response);
    }
}
