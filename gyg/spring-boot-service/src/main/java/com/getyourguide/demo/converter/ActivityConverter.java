package com.getyourguide.demo.converter;

import com.getyourguide.demo.dto.Activity;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;

public class ActivityConverter {

    public static Activity toActivity(ActivityEntity activityEntity) {
        SupplierEntity supplier = activityEntity.getSupplier();

        return Activity.builder()
                .id(activityEntity.getId())
                .title(activityEntity.getTitle())
                .price(activityEntity.getPrice())
                .currency(activityEntity.getCurrency())
                .rating(activityEntity.getRating())
                .specialOffer(activityEntity.isSpecialOffer())
                .supplierName(supplier.getName())
                .location(supplier.getLocation())
                .build();
    }
}
