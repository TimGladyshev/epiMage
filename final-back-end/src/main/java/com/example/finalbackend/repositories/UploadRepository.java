package com.example.finalbackend.repositories;

import com.example.finalbackend.models.Batch;
import com.example.finalbackend.models.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    Optional<Upload> findByCompleted(Long completed);
    Optional<Upload> findByCreated(Long created);
    boolean existsByName(String name);
    Optional<Upload> findByName(String name);
}
