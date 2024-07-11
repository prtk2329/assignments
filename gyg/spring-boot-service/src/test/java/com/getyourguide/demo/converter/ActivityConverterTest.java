package com.getyourguide.demo.converter;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;
import org.junit.jupiter.api.Test;

import static com.getyourguide.demo.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityConverterTest {

    @Test
    public void shouldConvertToActivity() {
        SupplierEntity supplierEntity = createSupplierEntity(1L, "Test Supplier", "1234 street",
                "123401", "Berlin", "Germany");

        ActivityEntity activityEntity = createActivityEntity(100L, "Test Activity", 10.01,
                "$", 4.1, false, supplierEntity);

        Activity expectedActivity = createActivity(100L, "Test Activity", 10.01,
                "$", 4.1, false, "Test Supplier",
                "1234 street, Berlin, 123401, Germany");


        Activity actualActivity = ActivityConverter.toActivity(activityEntity);

        assertEquals(actualActivity, expectedActivity);
    }

}
