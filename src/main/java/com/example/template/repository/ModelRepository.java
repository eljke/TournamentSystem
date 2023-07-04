package com.example.template.repository;

import com.example.template.model.Model;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
