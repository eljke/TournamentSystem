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
import ru.eljke.tournamentsystem.dto.TournamentDTO;
import ru.eljke.tournamentsystem.entity.Tournament;
import ru.eljke.tournamentsystem.service.TournamentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Operations with tournaments")
public class TournamentController {
    private final TournamentService service;

    @Operation(summary = "Get all tournaments by pages", description = "Returns all tournaments pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("")
    public ResponseEntity<Page<TournamentDTO>> getAll(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.getAllTournaments(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Page<TournamentDTO> tournamentsDTO = service.getAllTournaments(pageable);
            return ResponseEntity.ok(tournamentsDTO);
        }
    }

    @Operation(summary = "Get tournament by ID", description = "Returns a single tournament by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TournamentDTO> getById(@Parameter(name = "id", description = "Tournament id", required = true) @PathVariable Long id) {
        TournamentDTO tournamentDTO = service.getTournamentById(id);
        if (tournamentDTO != null) {
            return ResponseEntity.ok(tournamentDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create tournament", description = "Create tournament with given body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/create")
    public ResponseEntity<TournamentDTO> create(@Parameter(name = "tournament", description = "Tournament object", required = true) @RequestBody Tournament tournament,
                                             @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {
        TournamentDTO tournamentToCreateDTO = service.createTournament(tournament, auth);
        if (tournamentToCreateDTO == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(tournamentToCreateDTO);
    }

    @Operation(summary = "Update tournament by ID", description = "Update a single tournament by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournament not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TournamentDTO> update(@Parameter(name = "id", description = "Tournament id", required = true) @PathVariable Long id,
                                             @Parameter(name = "tournament", description = "Tournament object", required = true) @RequestBody Tournament tournament,
                                             @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {
        TournamentDTO tournamentToUpdateDTO = service.updateTournament(tournament, id, auth);
        if (tournamentToUpdateDTO == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(tournamentToUpdateDTO);
    }

    @Operation(summary = "Cancel tournament by ID", description = "Cancels tournament by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Tournament is already canceled"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@Parameter(name = "id", description = "Tournament id", required = true) @PathVariable Long id,
                                         @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {
        service.cancelTournamentById(id, auth);
        return ResponseEntity.ok("Successfully canceled tournament with id = " + id);
    }

    @Operation(summary = "Delete tournament by ID", description = "Deletes tournament by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(name = "id", description = "Tournament id", required = true) @PathVariable Long id,
                                         @Parameter(name = "auth", description = "User's authentication", required = true) Authentication auth) {

            service.deleteTournamentById(id, auth);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted tournament with id = " + id);
    }

    @Operation(summary = "Get past tournaments", description = "Returns past tournaments pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/past")
    public ResponseEntity<Page<TournamentDTO>> findPastTournaments(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "sort order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findPastTournaments(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findPastTournaments(pageable));
        }
    }

    @Operation(summary = "Get current tournaments", description = "Returns current tournaments pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/current")
    public ResponseEntity<Page<TournamentDTO>> findCurrentTournaments(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findCurrentTournaments(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findCurrentTournaments(pageable));
        }
    }

    @Operation(summary = "Get upcoming tournaments", description = "Returns upcoming tournaments pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/upcoming")
    public ResponseEntity<Page<TournamentDTO>> findUpcomingTournaments(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findUpcomingTournaments(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findUpcomingTournaments(pageable));
        }
    }

    @Operation(summary = "Get tournaments between dates", description = "Returns tournaments between 2 given dates pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/between")
    public ResponseEntity<Page<TournamentDTO>> findTournamentsBetweenDates(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "start date", description = "Start date", required = true) @RequestParam(name = "start", defaultValue = "01/01/2023 00:00") String startDateStr,
            @Parameter(name = "end date", description = "End date", required = true) @RequestParam(name = "end", defaultValue = "31/12/2023 23:59") String endDateStr) {
        Pageable pageable = createPageable(order, page, size, sort);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        if (service.findTournamentsBetweenDates(pageable, startDate, endDate).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findTournamentsBetweenDates(pageable, startDate, endDate));
        }
    }

    @Operation(summary = "Get past tournaments by user ID", description = "Returns past tournaments by user ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/past/by-user")
    public ResponseEntity<Page<TournamentDTO>> findPastTournamentsByUserId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "sort order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "user id", description = "User ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long userId) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findPastTournamentsByUserId(pageable, userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findPastTournamentsByUserId(pageable, userId));
        }
    }

    @Operation(summary = "Get current tournaments by user ID", description = "Returns current tournaments by user ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/current/by-user")
    public ResponseEntity<Page<TournamentDTO>> findCurrentTournamentsByUserId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "user id", description = "User ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long userId) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findCurrentTournamentsByUserId(pageable, userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findCurrentTournamentsByUserId(pageable, userId));
        }
    }

    @Operation(summary = "Get upcoming tournaments by user ID", description = "Returns upcoming tournaments by user ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/upcoming/by-user")
    public ResponseEntity<Page<TournamentDTO>> findUpcomingTournamentsByUserId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "user id", description = "User ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long userId) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findUpcomingTournamentsByUserId(pageable, userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findUpcomingTournamentsByUserId(pageable, userId));
        }
    }

    @Operation(summary = "Get tournaments between dates by user ID", description = "Returns tournaments between 2 given dates by user ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/between/by-user")
    public ResponseEntity<Page<TournamentDTO>> findTournamentsBetweenDatesByUserId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "start date", description = "Start date", required = true) @RequestParam(name = "start", defaultValue = "01/01/2023 00:00") String startDateStr,
            @Parameter(name = "end date", description = "End date", required = true) @RequestParam(name = "end", defaultValue = "31/12/2023 23:59") String endDateStr,
            @Parameter(name = "user id", description = "User ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long userId) {
        Pageable pageable = createPageable(order, page, size, sort);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        if (service.findTournamentsBetweenDatesByUserId(pageable, startDate, endDate, userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findTournamentsBetweenDatesByUserId(pageable, startDate, endDate, userId));
        }
    }

    @Operation(summary = "Get past tournaments by team ID", description = "Returns past tournaments by team ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/past/by-team")
    public ResponseEntity<Page<TournamentDTO>> findPastTournamentsByTeamId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "sort order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "team id", description = "Team ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long teamId) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findPastTournamentsByTeamId(pageable, teamId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findPastTournamentsByTeamId(pageable, teamId));
        }
    }

    @Operation(summary = "Get current tournaments by team ID", description = "Returns current tournaments by team ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/current/by-team")
    public ResponseEntity<Page<TournamentDTO>> findCurrentTournamentsByTeamId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "team id", description = "Team ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long teamId) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findCurrentTournamentsByTeamId(pageable, teamId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findCurrentTournamentsByTeamId(pageable, teamId));
        }
    }

    @Operation(summary = "Get upcoming tournaments by team ID", description = "Returns upcoming tournaments by team ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/upcoming/by-team")
    public ResponseEntity<Page<TournamentDTO>> findUpcomingTournamentsByTeamId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "team id", description = "Team ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long teamId) {
        Pageable pageable = createPageable(order, page, size, sort);

        if (service.findUpcomingTournamentsByTeamId(pageable, teamId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findUpcomingTournamentsByTeamId(pageable, teamId));
        }
    }

    @Operation(summary = "Get tournaments between dates by team ID", description = "Returns tournaments between 2 given dates by team ID pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Tournaments not found")
    })
    @GetMapping("/between/by-team")
    public ResponseEntity<Page<TournamentDTO>> findTournamentsBetweenDatesByTeamId(
            @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") Integer size,
            @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort,
            @Parameter(name = "order", description = "Page sort order", required = true) @RequestParam(defaultValue = "asc") String order,
            @Parameter(name = "start date", description = "Start date", required = true) @RequestParam(name = "start", defaultValue = "01/01/2023 00:00") String startDateStr,
            @Parameter(name = "end date", description = "End date", required = true) @RequestParam(name = "end", defaultValue = "31/12/2023 23:59") String endDateStr,
            @Parameter(name = "team id", description = "Team ID", required = true) @RequestParam(name = "id", defaultValue = "1") Long teamId) {
        Pageable pageable = createPageable(order, page, size, sort);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        if (service.findTournamentsBetweenDatesByTeamId(pageable, startDate, endDate, teamId).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findTournamentsBetweenDatesByTeamId(pageable, startDate, endDate, teamId));
        }
    }

    private Pageable createPageable(String order, int page, int size, String sort) {
        Sort.Direction direction;
        if (order.equals("desc")) {
            direction = Sort.Direction.DESC;
        } else if (order.equals("reverse")) {
            direction = Sort.Direction.DESC;
        } else {
            direction = Sort.Direction.ASC;
        }

        return PageRequest.of(page, size, Sort.by(direction, sort));
    }
}
