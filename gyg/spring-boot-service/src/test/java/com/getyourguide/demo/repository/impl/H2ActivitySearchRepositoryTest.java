package com.getyourguide.demo.repository.impl;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.dto.PaginatedResponse;
import com.getyourguide.demo.dto.SearchCriteria;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.getyourguide.demo.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class H2ActivitySearchRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private H2ActivitySearchRepository h2ActivitySearchRepository;

    @BeforeEach
    public void setup() {
        h2ActivitySearchRepository = new H2ActivitySearchRepository(activityRepository);

        SupplierEntity supplierEntity = createSupplierEntity(1L, "ABC GmbH", "1234 street",
                "123401", "Berlin", "Germany");

        ActivityEntity activityEntity1 = createActivityEntity(100L, "German Tour", 14.5,
                "$", 4.1, false, supplierEntity);

        ActivityEntity activityEntity2 = createActivityEntity(101L, "French Tour", 10.90,
                "€", 4.9, true, supplierEntity);

        List<ActivityEntity> activityEntities = List.of(activityEntity1, activityEntity2);

        supplierRepository.save(supplierEntity);
        activityRepository.saveAll(activityEntities);
    }

    @Test
    public void testSearchActivity_WithEmptyQuery() {
        Activity expectedActivity1 = createActivity(100L, "German Tour", 14.5, "$", 4.1,
                false, "ABC GmbH", "1234 street, Berlin, 123401, Germany");
        Activity expectedActivity2 = createActivity(101L, "French Tour", 10.90, "€", 4.9,
                true, "ABC GmbH", "1234 street, Berlin, 123401, Germany");

        PaginatedResponse<Activity> activityPage = h2ActivitySearchRepository.search(new SearchCriteria("", 1, 10));

        assertNotNull(activityPage);
        assertEquals(2, activityPage.getContent().size());
        assertEquals(expectedActivity1, activityPage.getContent().get(0));
        assertEquals(expectedActivity2, activityPage.getContent().get(1));
        assertEquals(2, activityPage.getTotalHits());
        assertEquals(1, activityPage.getTotalPages());
        assertEquals(1, activityPage.getCurrentPage());
        assertEquals(2, activityPage.getPageSize());
        assertEquals(false, activityPage.isHasPrevious());
        assertEquals(false, activityPage.isHasNext());

    }

    @Test
    public void testSearchActivity_WithQuery() {
        Activity expectedActivity = createActivity(100L, "German Tour", 14.5, "$", 4.1,
                false, "ABC GmbH", "1234 street, Berlin, 123401, Germany");

        PaginatedResponse<Activity> activityPage = h2ActivitySearchRepository.search(new SearchCriteria("German", 1, 10));

        assertNotNull(activityPage);
        assertEquals(1, activityPage.getContent().size());
        assertEquals(expectedActivity, activityPage.getContent().get(0));
        assertEquals(1, activityPage.getTotalHits());
        assertEquals(1, activityPage.getTotalPages());
        assertEquals(1, activityPage.getCurrentPage());
        assertEquals(1, activityPage.getPageSize());
        assertEquals(false, activityPage.isHasPrevious());
        assertEquals(false, activityPage.isHasNext());
    }

    @Test
    public void testSearchActivity_WithPagination() {
        Activity expectedActivity = createActivity(100L, "German Tour", 14.5, "$", 4.1,
                false, "ABC GmbH", "1234 street, Berlin, 123401, Germany");

        PaginatedResponse<Activity> activityPage = h2ActivitySearchRepository.search(new SearchCriteria("tour", 1, 1));

        assertNotNull(activityPage);
        assertEquals(1, activityPage.getContent().size());
        assertEquals(expectedActivity, activityPage.getContent().get(0));
        assertEquals(2, activityPage.getTotalHits());
        assertEquals(2, activityPage.getTotalPages());
        assertEquals(1, activityPage.getCurrentPage());
        assertEquals(1, activityPage.getPageSize());
        assertEquals(false, activityPage.isHasPrevious());
        assertEquals(true, activityPage.isHasNext());
    }

    @Test
    public void testSearchActivity_WithNextPage() {
        Activity expectedActivity = createActivity(101L, "French Tour", 10.90, "€", 4.9,
                true, "ABC GmbH", "1234 street, Berlin, 123401, Germany");

        PaginatedResponse<Activity> activityPage = h2ActivitySearchRepository.search(new SearchCriteria("tour", 2, 1));

        assertNotNull(activityPage);
        assertEquals(1, activityPage.getContent().size());
        assertEquals(expectedActivity, activityPage.getContent().get(0));
        assertEquals(2, activityPage.getTotalHits());
        assertEquals(2, activityPage.getTotalPages());
        assertEquals(2, activityPage.getCurrentPage());
        assertEquals(1, activityPage.getPageSize());
        assertEquals(true, activityPage.isHasPrevious());
        assertEquals(false, activityPage.isHasNext());
    }

}
