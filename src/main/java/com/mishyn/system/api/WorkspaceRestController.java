package com.mishyn.system.api;

import com.mishyn.system.dto.WorkspaceDTO;
import com.mishyn.system.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mishyn.system.api.ApplicationAPI.*;

@RestController
@RequestMapping(API + WORKSPACES)
@RequiredArgsConstructor
public class WorkspaceRestController {

    private final WorkspaceService workspaceService;

    @GetMapping
    public ResponseEntity<List<WorkspaceDTO>> getAllWorkspaces() {
        List<WorkspaceDTO> ticketDTOS = workspaceService.findAll();
        return new ResponseEntity<>(
                ticketDTOS,
                ticketDTOS.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @GetMapping(GET + "/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        WorkspaceDTO WorkspaceDTO = workspaceService.getDTOById(id);
        return new ResponseEntity<>(
                WorkspaceDTO,
                WorkspaceDTO == null ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @PutMapping(UPDATE)
    public ResponseEntity<WorkspaceDTO> updateWorkspaceById(@RequestBody WorkspaceDTO WorkspaceDTO) {
        WorkspaceDTO updatedWorkspaceDTO = workspaceService.updateWorkspace(WorkspaceDTO);
        return new ResponseEntity<>(updatedWorkspaceDTO, updatedWorkspaceDTO == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping(CREATE)
    public ResponseEntity<WorkspaceDTO> createWorkspace(@RequestBody WorkspaceDTO WorkspaceDTO) {
        try {
            workspaceService.createWorkspace(WorkspaceDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<?> deleteWorkspaceById(@PathVariable Long id) {
        try {
            workspaceService.deleteWorkspaceById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
