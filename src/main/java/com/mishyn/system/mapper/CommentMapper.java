package com.mishyn.system.mapper;

import com.mishyn.system.dto.CommentDTO;
import com.mishyn.system.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "ticketId", source = "commentEntity.ticket.id")
    @Mapping(target = "createdById", source = "commentEntity.createdBy.id")
    CommentDTO toCommentDTO(CommentEntity commentEntity);

    CommentEntity toCommentEntity(CommentDTO commentDTO, @MappingTarget CommentEntity commentEntity);
}
