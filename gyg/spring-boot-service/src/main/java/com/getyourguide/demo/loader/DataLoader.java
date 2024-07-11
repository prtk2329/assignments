package com.getyourguide.demo.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.demo.dto.RawActivity;
import com.getyourguide.demo.dto.RawSupplier;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;
import com.getyourguide.demo.repository.impl.ActivityRepository;
import com.getyourguide.demo.repository.impl.SupplierRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataLoader {
    private final ActivityRepository activityRepository;
    private final SupplierRepository supplierRepository;
    private final ObjectMapper om;

    public DataLoader(ActivityRepository activityRepository, SupplierRepository supplierRepository, ObjectMapper objectMapper) {
        this.activityRepository = activityRepository;
        this.supplierRepository = supplierRepository;
        this.om = objectMapper;
    }

    @PostConstruct
    public void loadData() {
        try {
            List<SupplierEntity> supplierEntities = loadSuppliers();
            loadActivities(supplierEntities);
        } catch (IOException e) {
            log.error("Error loading data into database", e);
        }
    }

    private List<SupplierEntity> loadSuppliers() throws IOException {
        List<RawSupplier> suppliers = readFile("static/suppliers.json", new TypeReference<>() {
        });
        List<SupplierEntity> supplierEntities = toSupplierEntities(suppliers);

        //TODO: Save in chunks instead of save all
        return supplierRepository.saveAll(supplierEntities);
    }

    private List<ActivityEntity> loadActivities(List<SupplierEntity> supplierEntities) throws IOException {
        List<RawActivity> activities = readFile("static/activities.json", new TypeReference<>() {
        });

        Map<Long, SupplierEntity> supplierEntityById = supplierEntities.stream()
                .collect(Collectors.toMap(SupplierEntity::getId, s -> s));

        List<ActivityEntity> activityEntities = activities.stream()
                .map(activity -> {
                    SupplierEntity supplierEntity = supplierEntityById.get(activity.getSupplierId());
                    return activity.toActivityEntity(supplierEntity);
                })
                .toList();

        //TODO: Save in chunks
        return activityRepository.saveAll(activityEntities);
    }

    private <T> List<T> readFile(String fileName, TypeReference<List<T>> typeReference) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        return om.readValue(is, typeReference);
    }

    private List<SupplierEntity> toSupplierEntities(List<RawSupplier> suppliers) {
        return suppliers.stream()
                .map(RawSupplier::toSupplierEntity)
                .toList();
    }
}
