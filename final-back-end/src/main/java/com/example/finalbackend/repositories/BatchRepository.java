package com.example.finalbackend.repositories;

import com.example.finalbackend.models.Batch;
import com.example.finalbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Optional<Batch> findByCompleted(Long completed);
    Optional<Batch> findByCreated(Long created);
    boolean existsByBatchID(String batchID);
    Optional<Batch> findByBatchID(String batchID);
}
