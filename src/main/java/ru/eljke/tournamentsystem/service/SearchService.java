package ru.eljke.tournamentsystem.service;

import java.util.List;

public interface SearchService<T> {
    List<T> search(String param, String keyword);
}
