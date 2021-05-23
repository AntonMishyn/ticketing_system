package com.mishyn.system.repository;

import com.mishyn.system.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    Set<TicketEntity> findAllByWorkspace_Id(Long id);
}
