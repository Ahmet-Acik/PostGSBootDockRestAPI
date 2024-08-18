package org.ahmet.sbpostgdocrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ahmet.sbpostgdocrest.exeptions.ResourceNotFoundException;
import org.ahmet.sbpostgdocrest.participant.Participant;
import org.ahmet.sbpostgdocrest.repository.ParticipantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/participant")
@Tag(name = "Participant", description = "API for managing participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    public ParticipantController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @GetMapping
    @Operation(summary = "Get all participants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Iterable<Participant> getParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a participant by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved participant"),
        @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public Participant getParticipant(@PathVariable Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
    }

    @PostMapping
    @Operation(summary = "Create a new participant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created participant"),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public Participant createParticipant(@RequestBody Participant participant) {
        validateParticipant(participant);
        if (participantRepository.existsByEmail(participant.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        return participantRepository.save(participant);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing participant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated participant"),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists"),
        @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public Participant updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        validateParticipant(participant);
        Participant existingParticipant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
        if (!existingParticipant.getEmail().equals(participant.getEmail()) && participantRepository.existsByEmail(participant.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        existingParticipant.setName(participant.getName());
        existingParticipant.setEmail(participant.getEmail());
        existingParticipant.setRole(participant.getRole());
        existingParticipant.setActive(participant.getActive());
        return participantRepository.save(existingParticipant);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a participant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated participant"),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists"),
        @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public Participant patchParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        validateParticipant(participant);
        Participant existingParticipant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
        if (participant.getName() != null) {
            existingParticipant.setName(participant.getName());
        }
        if (participant.getEmail() != null) {
            if (!existingParticipant.getEmail().equals(participant.getEmail()) && participantRepository.existsByEmail(participant.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
            }
            existingParticipant.setEmail(participant.getEmail());
        }
        if (participant.getRole() != null) {
            existingParticipant.setRole(participant.getRole());
        }
        if (participant.getActive() != null) {
            existingParticipant.setActive(participant.getActive());
        }
        return participantRepository.save(existingParticipant);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a participant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted participant"),
        @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public void deleteParticipant(@PathVariable Long id) {
        participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
        participantRepository.deleteById(id);
    }

    private void validateParticipant(Participant participant) {
        if (participant.getName() != null && participant.getName().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be longer than 255 characters");
        }
        if (participant.getRole() != null && !isValidRole(participant.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");
        }
    }

    private boolean isValidRole(String role) {
        return role.equals("BA") || role.equals("Dev") || role.equals("DevOps") || role.equals("SDET") || role.equals("ScrumMaster") || role.equals("PO");
    }
}