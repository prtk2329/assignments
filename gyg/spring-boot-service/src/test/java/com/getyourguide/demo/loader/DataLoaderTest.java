package com.getyourguide.demo.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.demo.model.ActivityEntity;
import com.getyourguide.demo.model.SupplierEntity;
import com.getyourguide.demo.repository.impl.ActivityRepository;
import com.getyourguide.demo.repository.impl.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
class DataLoaderTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testLoadDataSuccessfully() throws Exception {
        DataLoader loader = new DataLoader(activityRepository, supplierRepository, new ObjectMapper());
        loader.loadData();

        List<ActivityEntity> activityEntities = activityRepository.findAll();
        List<SupplierEntity> supplierEntities = supplierRepository.findAll();

        assertEquals(14, activityEntities.size());
        assertEquals(4, supplierEntities.size());
    }


    @Test
    public void testLoadDataShouldThrowNoException() throws IOException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenThrow(IOException.class);

        DataLoader loader = new DataLoader(activityRepository, supplierRepository, objectMapper);
        assertDoesNotThrow(() -> loader.loadData());

        verify(objectMapper, times(1)).readValue(any(InputStream.class), any(TypeReference.class));
    }

}
