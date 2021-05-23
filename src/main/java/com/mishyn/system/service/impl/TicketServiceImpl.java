package com.mishyn.system.service.impl;

import com.atlassian.asap.api.Jwt;
import com.atlassian.asap.core.exception.JwtParseException;
import com.atlassian.asap.core.exception.UnsupportedAlgorithmException;
import com.atlassian.asap.core.parser.JwtParser;
import com.mishyn.system.dto.TicketDTO;
import com.mishyn.system.dto.UserDTO;
import com.mishyn.system.entity.CommentEntity;
import com.mishyn.system.entity.TicketEntity;
import com.mishyn.system.entity.UserEntity;
import com.mishyn.system.mapper.TicketMapper;
import com.mishyn.system.repository.TicketRepository;
import com.mishyn.system.repository.UserRepository;
import com.mishyn.system.repository.WorkSpaceRepository;
import com.mishyn.system.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final TicketMapper ticketMapper;

    private final JwtParser jwtParser;

    @Override
    public List<TicketDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<TicketDTO> findTicketsByWorkspaceId(Long id) {
        return ticketRepository.findAllByWorkspace_Id(id).stream()
                .map(ticketMapper::toTicketDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<TicketEntity> findTicketsEntitiesByWorkspaceId(Long id) {
        return new HashSet<>(ticketRepository.findAllByWorkspace_Id(id));
    }

    @Override
    public TicketDTO getById(Long id) {
        return ticketMapper.toTicketDTO(
                ticketRepository.findById(id).orElse(null)
        );
    }

    @Override
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) throws IllegalArgumentException {
        TicketEntity ticketEntity = ticketMapper.toTicketEntity(ticketDTO, new TicketEntity());

        UserDTO assignee = ticketDTO.getAssignee();
        ticketEntity.setAssignee(assignee == null ? null : userRepository.findById(assignee.getId()).orElse(null));
        ticketEntity.setWorkspace(workSpaceRepository.findById(ticketDTO.getWorkspaceId()).orElseThrow(IllegalArgumentException::new));
        ticketEntity.setCreatedBy(getUserFromAuthToken());
        ticketEntity.setCreatedAt(LocalDateTime.now());
        ticketEntity.setModifiedAt(LocalDateTime.now());

        ticketRepository.save(ticketEntity);
        return ticketDTO;
    }

    @Override
    public TicketDTO updateTicket(TicketDTO ticketDTO) {
        TicketEntity ticketEntity = ticketRepository.findById(ticketDTO.getId()).orElse(null);
        if (ticketEntity == null) {
            return null;
        } else {
            TicketEntity newEntity = ticketMapper.toTicketEntity(ticketDTO, ticketEntity);
            newEntity.setModifiedAt(LocalDateTime.now());
            return ticketMapper.toTicketDTO(
                    ticketRepository.save(newEntity)
            );
        }
    }

    @Override
    public TicketEntity saveTicketEntity(TicketEntity ticketEntity) {
        return ticketRepository.save(ticketEntity);
    }

    private UserEntity getUserFromAuthToken() {
        try {
            Jwt validJwt = jwtParser.parse(
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                            .getHeader("Authorization")
                            .substring("Bearer ".length())
            );
            return userRepository.findByUsername(validJwt.getClaims().getSubject().orElseThrow(NullPointerException::new));
        } catch (NullPointerException | JwtParseException | UnsupportedAlgorithmException e) {
            return null;
        }
    }
}
