package com.getyourguide.demo.repository.impl;

import com.getyourguide.demo.model.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long>, JpaSpecificationExecutor {
}
