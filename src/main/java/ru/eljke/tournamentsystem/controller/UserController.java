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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eljke.tournamentsystem.dto.UserDTO;
import ru.eljke.tournamentsystem.entity.User;
import ru.eljke.tournamentsystem.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations with users")
public class UserController {
    private final UserService service;

    @Operation(summary = "Get all users by pages", description = "Returns all users pageable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("")
    public ResponseEntity<Page<UserDTO>> findAllPageable(
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

        if (service.getAll(pageable).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Page<UserDTO> users = service.getAll(pageable);
            return ResponseEntity.ok(users);
        }
    }

    @Operation(summary = "Search users", description = "Search users by param and keyword")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchMembers(
            @Parameter(name = "param", description = "Parameter for search") @RequestParam(required = false) String param,
            @Parameter(name = "keyword", description = "Keyword for search") @RequestParam(required = false) String keyword) {

        List<UserDTO> userDTOs;

        if (param == null) {
            userDTOs = service.searchEverywhere(keyword);
        } else {
            userDTOs = service.searchByParam(param, keyword);
        }

        if (userDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userDTOs);
        }
    }

    @Operation(summary = "Get member by ID", description = "Returns a single member by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@Parameter(name = "id", description = "User id", required = true) @PathVariable Long id) {
        UserDTO userDTO = service.getById(id);

        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create user", description = "Create user with given body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PostMapping("/create")
    public ResponseEntity<UserDTO> create(@Parameter(name = "user", description = "User object", required = true) @RequestBody User user) {
        return ResponseEntity.ok(service.create(user));
    }

    @Operation(summary = "Update user by ID", description = "Update a single user by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@Parameter(name = "id", description = "User id", required = true) @PathVariable Long id,
                                       @Parameter(name = "user", description = "User object", required = true) @RequestBody User user) {
        if (service.getById(id) != null) {
            return ResponseEntity.ok(service.update(user, id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Delete member by ID", description = "Deletes member by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(name = "id", description = "User id", required = true) @PathVariable Long id) {
        if (service.getById(id) != null) {
            service.delete(id);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
            return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body("Успешно!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
