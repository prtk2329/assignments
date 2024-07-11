package com.getyourguide.demo.utils;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;

import java.util.List;

public class TestUtils {

    public static Activity createActivity(Long id, String title, double price, String currency, double rating,
                                          boolean specialOffer, String supplierName, String location) {
        return Activity.builder()
                .id(id)
                .title(title)
                .price(price)
                .currency(currency)
                .rating(rating)
                .specialOffer(specialOffer)
                .supplierName(supplierName)
                .location(location)
                .build();
    }

    public static SupplierEntity createSupplierEntity(Long id, String name, String address, String zip, String city,
                                                      String country) {
        return SupplierEntity.builder()
                .id(id)
                .name(name)
                .address(address)
                .zip(zip)
                .city(city)
                .country(country)
                .build();
    }

    public static ActivityEntity createActivityEntity(Long id, String title, double price, String currency, double rating, boolean specialOffer, SupplierEntity supplierEntity) {
        return ActivityEntity.builder()
                .id(id)
                .title(title)
                .price(price)
                .currency(currency)
                .rating(rating)
                .specialOffer(specialOffer)
                .supplier(supplierEntity)
                .build();
    }

    public static List<Activity> getActivities() {
        Activity activity1 = createActivity(1L, "German Tour", 14.0, "$", 4.8,
                false, "ABC GmbH", "1234 ,Berlin, Germany");
        Activity activity2 = createActivity(2L, "French Tour", 10.0, "€", 4.5,
                true, "XYZ GmbH", "213, Paris, France");
        return List.of(activity1, activity2);
    }
}
