package com.example.template.service;

import com.example.template.model.Model;
import com.example.template.repository.ModelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceImpl implements DatabaseService<Model> {
    private final ModelRepository repository;

    public ServiceImpl(ModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Model> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Model> getAll() {
        return repository.findAll(Sort.by("id"));
    }

    @Override
    public Model create(Model model) {
        return repository.save(model);
    }

    @Override
    public Model update(Model model, Long id) {
        repository.findById(id)
                .map(updated -> {
                    updated.setName(model.getName());

                    return repository.save(updated);
                });
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
    }
}
