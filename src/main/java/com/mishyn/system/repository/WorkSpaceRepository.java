package com.mishyn.system.repository;

import com.mishyn.system.entity.WorkSpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpaceEntity, Long> {
}
