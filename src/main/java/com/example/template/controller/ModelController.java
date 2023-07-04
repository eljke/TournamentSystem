package com.example.template.controller;

import com.example.template.model.Model;
import com.example.template.service.ServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("models")
public class ModelController {
    private final ServiceImpl service;

    public ModelController(ServiceImpl service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Model>> getAll() {
        if (service.getAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(service.getAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getById(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            return ResponseEntity.ok(service.getById(id).orElse(null));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Model> create(@RequestBody Model model) {
        return ResponseEntity.ok(service.create(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> update(@PathVariable Long id, @RequestBody Model model) {
        if (service.getById(id).isPresent()) {
            return ResponseEntity.ok(service.update(model, id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.ok("Успешно!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
