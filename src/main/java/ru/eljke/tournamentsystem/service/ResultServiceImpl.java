package ru.eljke.tournamentsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.eljke.tournamentsystem.entity.Result;
import ru.eljke.tournamentsystem.repository.ResultRepository;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository repository;

    @Override
    public Result getResultById(Long resultId) {
        return repository.findById(resultId)
                .orElseThrow(() -> new IllegalArgumentException("Result not found"));

    }

    @Override
    public Result createResult(Result result) {
        return repository.save(result);
    }
}
