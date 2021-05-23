package com.mishyn.system.service;

import com.mishyn.system.dto.TicketDTO;
import com.mishyn.system.entity.TicketEntity;

import java.util.List;
import java.util.Set;

public interface TicketService {

    Set<TicketDTO> findTicketsByWorkspaceId(Long id);

    List<TicketDTO> findAll();

    Set<TicketEntity> findTicketsEntitiesByWorkspaceId(Long id);

    TicketDTO getById(Long id);

    void deleteTicketById(Long id);

    TicketDTO createTicket(TicketDTO ticketDTO);

    TicketDTO updateTicket(TicketDTO ticketDTO);

    TicketEntity saveTicketEntity(TicketEntity ticketEntity);
}
