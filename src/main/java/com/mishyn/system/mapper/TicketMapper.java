package com.mishyn.system.mapper;

import com.mishyn.system.dto.TicketDTO;
import com.mishyn.system.entity.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TicketMapper {

    @Mappings({
            @Mapping(target = "workspaceId", source = "workspace.id"),
    })
    TicketDTO toTicketDTO(TicketEntity ticketEntity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "assignee", ignore = true),
            @Mapping(target = "comments", ignore = true)
    })
    TicketEntity toTicketEntity(TicketDTO ticketDTO, @MappingTarget TicketEntity ticketEntity);
}
