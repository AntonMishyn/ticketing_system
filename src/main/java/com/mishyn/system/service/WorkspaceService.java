package com.mishyn.system.service;

import com.mishyn.system.dto.WorkspaceDTO;
import com.mishyn.system.entity.WorkSpaceEntity;

import java.util.List;

public interface WorkspaceService {

    List<WorkspaceDTO> findAll();

    WorkSpaceEntity saveNewWorkspace(String name);

    WorkSpaceEntity getById(Long id);

    WorkspaceDTO getDTOById(Long id);

    void deleteWorkspaceById(Long id);

    WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDTO);

    WorkspaceDTO updateWorkspace(WorkspaceDTO workspaceDTO);
}
