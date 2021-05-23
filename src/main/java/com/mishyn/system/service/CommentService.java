package com.mishyn.system.service;

import com.mishyn.system.dto.CommentDTO;
import com.mishyn.system.entity.CommentEntity;

import java.util.List;
import java.util.Set;

public interface CommentService {

    List<CommentDTO> findAll();

    Set<CommentEntity> findByTicketId(Long ticketId);

    CommentDTO findCommentById(Long id);

    void deleteCommentById(Long id);

    CommentDTO updateComment(CommentDTO commentDTO);

    CommentDTO createComment(CommentDTO commentDTO);

    CommentEntity saveCommentEntity(CommentEntity commentEntity);
}
