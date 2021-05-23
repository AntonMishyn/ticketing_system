package com.mishyn.system.api;

import com.mishyn.system.dto.CommentDTO;
import com.mishyn.system.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mishyn.system.api.ApplicationAPI.*;

@RestController
@RequestMapping(API + COMMENTS)
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> commentEntities = commentService.findAll();
        return new ResponseEntity<>(
                commentEntities,
                commentEntities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @GetMapping(GET + "/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.findCommentById(id);
        return new ResponseEntity<>(
                commentDTO,
                commentDTO == null ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @PutMapping(UPDATE)
    public ResponseEntity<CommentDTO> updateCommentById(@RequestBody CommentDTO commentDTO) {
        CommentDTO updatedCommentDTO = commentService.updateComment(commentDTO);
        return new ResponseEntity<>(updatedCommentDTO, updatedCommentDTO == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping(CREATE)
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        try {
            commentService.createComment(commentDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        try {
            commentService.deleteCommentById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
