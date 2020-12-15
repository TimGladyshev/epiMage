package com.example.finalbackend.repositories;

import com.example.finalbackend.models.Global;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlobalsRepository extends JpaRepository<Global, Long> {
    Optional<Global> getFirstById(Long id);
}
