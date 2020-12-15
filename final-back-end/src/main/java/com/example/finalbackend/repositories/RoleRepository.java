package com.example.finalbackend.repositories;

import java.util.Optional;

import com.example.finalbackend.models.ERole;
import com.example.finalbackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
