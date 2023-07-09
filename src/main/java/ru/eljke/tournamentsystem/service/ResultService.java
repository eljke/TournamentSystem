package ru.eljke.tournamentsystem.service;

import ru.eljke.tournamentsystem.model.Result;

public interface ResultService {
    Result getResultById(Long resultId);
    Result createResult(Result result);
}
