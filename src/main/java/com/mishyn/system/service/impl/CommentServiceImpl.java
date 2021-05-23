package com.mishyn.system.service.impl;

import com.atlassian.asap.api.Jwt;
import com.atlassian.asap.core.exception.JwtParseException;
import com.atlassian.asap.core.exception.UnsupportedAlgorithmException;
import com.atlassian.asap.core.parser.JwtParser;
import com.mishyn.system.dto.CommentDTO;
import com.mishyn.system.entity.CommentEntity;
import com.mishyn.system.entity.UserEntity;
import com.mishyn.system.mapper.CommentMapper;
import com.mishyn.system.repository.CommentRepository;
import com.mishyn.system.repository.TicketRepository;
import com.mishyn.system.repository.UserRepository;
import com.mishyn.system.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    private final JwtParser jwtParser;

    public List<CommentDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<CommentEntity> findByTicketId(Long ticketId) {
        return commentRepository.findAllByTicket_Id(ticketId);
    }

    public CommentDTO findCommentById(Long id) {
        return commentRepository.findById(id).map(commentMapper::toCommentDTO).orElse(null);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentMapper.toCommentEntity(commentDTO, new CommentEntity());
        commentEntity.setCreatedBy(getUserFromAuthToken());
        commentEntity.setTicket(ticketRepository.getOne(commentDTO.getTicketId()));
        return commentMapper.toCommentDTO(
                commentRepository.save(commentEntity)
        );
    }

    @Override
    public CommentDTO updateComment(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentDTO.getId()).orElse(null);
        if (commentEntity == null) {
            return null;
        } else {
            CommentEntity newEntity = commentRepository.save(commentMapper.toCommentEntity(commentDTO, commentEntity));
            return commentMapper.toCommentDTO(newEntity);
        }
    }

    @Override
    public CommentEntity saveCommentEntity(CommentEntity commentEntity) {
        return commentRepository.save(commentEntity);
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
