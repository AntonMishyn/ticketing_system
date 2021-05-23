package com.mishyn.system.repository;

import com.mishyn.system.entity.RoleEntity;
import com.mishyn.system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
