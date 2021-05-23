package com.mishyn.system.service.impl;

import com.mishyn.system.dto.WorkspaceDTO;
import com.mishyn.system.entity.WorkSpaceEntity;
import com.mishyn.system.mapper.WorkspaceMapper;
import com.mishyn.system.repository.WorkSpaceRepository;
import com.mishyn.system.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkSpaceRepository workSpaceRepository;
    private final WorkspaceMapper workspaceMapper;

    @Override
    public List<WorkspaceDTO> findAll() {
        return workSpaceRepository.findAll().stream()
                .map(workspaceMapper::toWorkspaceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WorkSpaceEntity saveNewWorkspace(String name) {
        return workSpaceRepository.save(new WorkSpaceEntity(name));
    }

    @Override
    public WorkSpaceEntity getById(Long id) {
        return workSpaceRepository.findById(id).orElse(null);
    }

    @Override
    public WorkspaceDTO getDTOById(Long id) {
        return workspaceMapper.toWorkspaceDTO(
                workSpaceRepository.findById(id).orElse(null)
        );
    }

    @Override
    public void deleteWorkspaceById(Long id) {
        workSpaceRepository.deleteById(id);
    }

    @Override
    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO) {
        workSpaceRepository.save(workspaceMapper.toWorkSpaceEntity(workspaceDTO, new WorkSpaceEntity()));
        return workspaceDTO;
    }

    @Override
    public WorkspaceDTO updateWorkspace(WorkspaceDTO workspaceDTO) {
        WorkSpaceEntity ticketEntity = workSpaceRepository.findById(workspaceDTO.getId()).orElse(null);
        if (ticketEntity == null) {
            return null;
        } else {
            WorkSpaceEntity newEntity = workSpaceRepository.save(workspaceMapper.toWorkSpaceEntity(workspaceDTO, ticketEntity));
            return workspaceMapper.toWorkspaceDTO(newEntity);
        }
    }
}
