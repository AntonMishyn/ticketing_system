package com.mishyn.system.api;

import com.mishyn.system.dto.TicketDTO;
import com.mishyn.system.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.mishyn.system.api.ApplicationAPI.*;

@RestController
@RequestMapping(API + TICKETS)
@RequiredArgsConstructor
public class TicketRestController {

    private final TicketService ticketService;

    @GetMapping(WORKSPACES + "/{id}")
    public ResponseEntity<Set<TicketDTO>> getAllTicketsByWorkspaceId(@PathVariable Long id) {
        Set<TicketDTO> ticketDTOS = ticketService.findTicketsByWorkspaceId(id);
        return new ResponseEntity<>(
                ticketDTOS,
                ticketDTOS.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> ticketDTOS = ticketService.findAll();
        return new ResponseEntity<>(
                ticketDTOS,
                ticketDTOS.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @GetMapping(GET + "/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        TicketDTO ticketDTO = ticketService.getById(id);
        return new ResponseEntity<>(
                ticketDTO,
                ticketDTO == null ? HttpStatus.NO_CONTENT : HttpStatus.OK
        );
    }

    @PutMapping(UPDATE)
    public ResponseEntity<TicketDTO> updateTicketById(@RequestBody TicketDTO ticketDTO) {
        TicketDTO updatedTicketDTO = ticketService.updateTicket(ticketDTO);
        return new ResponseEntity<>(updatedTicketDTO, updatedTicketDTO == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @PostMapping(CREATE)
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            ticketService.createTicket(ticketDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(DELETE + "/{id}")
    public ResponseEntity<?> deleteTicketById(@PathVariable Long id) {
        try {
            ticketService.deleteTicketById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
