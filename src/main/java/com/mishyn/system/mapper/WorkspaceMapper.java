package com.mishyn.system.mapper;

import com.mishyn.system.dto.WorkspaceDTO;
import com.mishyn.system.entity.WorkSpaceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class, TicketMapper.class})
public interface WorkspaceMapper {

    WorkspaceDTO toWorkspaceDTO(WorkSpaceEntity workSpaceEntity);

    WorkSpaceEntity toWorkSpaceEntity(WorkspaceDTO workspaceDTO, @MappingTarget WorkSpaceEntity workSpaceEntity);
}
