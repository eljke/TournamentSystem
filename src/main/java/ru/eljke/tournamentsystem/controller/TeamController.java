package ru.eljke.tournamentsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eljke.tournamentsystem.dto.TeamDTO;
import ru.eljke.tournamentsystem.entity.Team;
import ru.eljke.tournamentsystem.service.TeamService;


@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Operations with teams")
public class TeamController {
    private final TeamService service;

    @Operation(summary = "Get all teams by pages", description = "Returns all teams pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Teams not found")
    })
    @GetMapping("")
    public ResponseEntity<Page<TeamDTO>> getAll(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "sort order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order) {
        Pageable pageable;
        if (order.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        } else if (order.equals("reverse")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).reverse());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        }

        if (service.getAllTeams(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.getAllTeams(pageable));
        }
    }

    @Operation(summary = "Get team by ID", description = "Returns a single team by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getById(@Parameter(name = "id", description = "Team id", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getTeamById(id));
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create team", description = "Create team with given body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PostMapping("/create")
    public ResponseEntity<TeamDTO> create(@Parameter(name = "team", description = "Team object", required = true) @RequestBody Team team) {
        return ResponseEntity.ok(service.createTeam(team));
    }

    @Operation(summary = "Update team by ID", description = "Update a single team by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update(@Parameter(name = "id", description = "Team id", required = true) @PathVariable Long id,
                                             @Parameter(name = "team", description = "Team object", required = true) @RequestBody Team team) {

        try {
            return ResponseEntity.ok(service.updateTeamById(team, id));
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete team by ID", description = "Deletes team by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(name = "id", description = "Team id", required = true) @PathVariable Long id) {
        try {
            service.deleteTeamById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully!");
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }
}
