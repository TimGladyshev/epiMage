package com.example.finalbackend.repositories;

import com.example.finalbackend.models.Upload;
import com.example.finalbackend.models.UploadShared;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SharedRepository extends JpaRepository<UploadShared, Long> {
    Optional<UploadShared> findByName(String name);
}
