package com.example.template.service;

import com.example.template.model.Model;

import java.util.List;
import java.util.Optional;

public interface DatabaseService<T> {
    Optional<T> getById(Long id);

    List<T> getAll();

    Model create(T t);

    Model update(T t, Long id);

    void delete(Long id);
}
