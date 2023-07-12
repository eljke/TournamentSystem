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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.eljke.tournamentsystem.dto.MatchDTO;
import ru.eljke.tournamentsystem.entity.*;
import ru.eljke.tournamentsystem.service.MatchService;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Operations with matches")
public class MatchController {
    private final MatchService matchService;

    @Operation(summary = "Get all matches", description = "Returns all matches by pages")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Matches not found")
    })
    @GetMapping("/matches")
    public ResponseEntity<Page<MatchDTO>> getAll(
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

        if (matchService.getAllMatches(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(matchService.getAllMatches(pageable));
        }
    }

    @Operation(summary = "Get match by ID", description = "Returns a single match by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @GetMapping("/{tournamentId}/matches/{id}")
    public ResponseEntity<MatchDTO> getById(@Parameter(name = "tournament id", description = "Tournament ID", required = true) @PathVariable Long tournamentId,
                                         @Parameter(name = "match id", description = "Match ID", required = true) @PathVariable Long id) {
        if (matchService.getMatchById(id) != null) {
            return ResponseEntity.ok(matchService.getMatchById(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create match", description = "Create match with given body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Forbidden")
    })
    @PostMapping("/{tournamentId}/matches/create")
    public ResponseEntity<MatchDTO> create(@Parameter(name = "tournament id", description = "Tournament ID", required = true) @PathVariable Long tournamentId,
                                        @Parameter(name = "match", description = "Match object", required = true) @RequestBody Match match,
                                             @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {
        System.out.println(tournamentId);
        MatchDTO matchToCreateDTO = matchService.createMatch(match, tournamentId, auth);
        if (matchToCreateDTO == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(matchToCreateDTO);
    }

    @Operation(summary = "Update match by ID", description = "Update a single match by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @PutMapping("/{tournamentId}/matches/{id}")
    public ResponseEntity<MatchDTO> update(@Parameter(name = "tournament id", description = "Tournament ID", required = true) @PathVariable Long tournamentId,
                                        @Parameter(name = "match id", description = "Match ID", required = true) @PathVariable Long id,
                                             @Parameter(name = "match", description = "Match object", required = true) @RequestBody Match match,
                                             @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {
        MatchDTO matchToUpdateDTO = matchService.updateMatchById(match, id, tournamentId, auth);
        if (matchToUpdateDTO == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(matchToUpdateDTO);
    }

    @Operation(summary = "Delete match by ID", description = "Deletes match by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @DeleteMapping("/{tournamentId}/matches/{id}")
    public ResponseEntity<String> delete(@Parameter(name = "tournament id", description = "Tournament ID", required = true) @PathVariable Long tournamentId,
                                         @Parameter(name = "match id", description = "Match ID", required = true) @PathVariable Long id,
                                         @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {
        matchService.deleteMatchById(id, tournamentId, auth);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted match with id = " + id);
    }
}
