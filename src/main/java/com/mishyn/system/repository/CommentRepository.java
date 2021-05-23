package com.mishyn.system.repository;

import com.mishyn.system.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Set<CommentEntity> findAllByTicket_Id(Long Ticket_id);
}
