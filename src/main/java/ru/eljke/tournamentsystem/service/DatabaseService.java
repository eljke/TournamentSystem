package ru.eljke.tournamentsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DatabaseService<T> {
    Optional<T> getById(Long id);
    Optional<T> getByParam(String param);

    Page<T> getAll(Pageable pageable);

    T create(T t);

    T update(T t, Long id);

    void delete(Long id);
}
