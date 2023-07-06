package ru.eljke.tournamentsystem.controller;

import ru.eljke.tournamentsystem.model.Member;
import ru.eljke.tournamentsystem.service.MemberServiceImpl;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Manages operations with members")
public class MemberController {
    private final MemberServiceImpl service;

    @Operation(summary = "Get all members", description = "Returns all members", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Members not found")
    })
    @GetMapping("")
    public ResponseEntity<List<Member>> getAll() {
        if (service.getAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.getAll());
        }
    }

    @Operation(summary = "Get all members by pages", description = "Returns all members pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Members not found")
    })
    @GetMapping("/pages")
    public ResponseEntity<Page<Member>> findAllPageable(
                                        @Parameter(name = "page", description = "Page", required = true) @RequestParam(defaultValue = "0") int page,
                                        @Parameter(name = "size", description = "Page size", required = true) @RequestParam(defaultValue = "10") int size,
                                        @Parameter(name = "sort", description = "Page sort", required = true) @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (service.findAllPageable(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.findAllPageable(pageable));
        }
    }

    @Operation(summary = "Get member by ID", description = "Returns a single member by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Member> getById(@Parameter(name = "id", description = "Member id", required = true) @PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            return ResponseEntity.ok(service.getById(id).orElse(null));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create member", description = "Create member with given body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PostMapping("/create")
    public ResponseEntity<Member> create(@Parameter(name = "member", description = "Member object", required = true) @RequestBody Member member) {
        return ResponseEntity.ok(service.create(member));
    }

    @Operation(summary = "Update member by ID", description = "Update a single member by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Member> update(@Parameter(name = "id", description = "Member id", required = true) @PathVariable Long id,
                                         @Parameter(name = "member", description = "Member object", required = true) @RequestBody Member member) {
        if (service.getById(id).isPresent()) {
            return ResponseEntity.ok(service.update(member, id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Get member by ID", description = "Returns a single member by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(name = "id", description = "Member id", required = true) @PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
            return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body("Успешно!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
