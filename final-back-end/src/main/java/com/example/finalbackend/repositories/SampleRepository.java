package com.example.finalbackend.repositories;

import com.example.finalbackend.models.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
}
