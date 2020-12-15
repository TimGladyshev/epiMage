package com.example.finalbackend.controllers;

import com.example.finalbackend.models.Global;
import com.example.finalbackend.repositories.GlobalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/stats")
public class GlobalsController {
    @Autowired
    GlobalsRepository globalsRepository;

    @GetMapping("/")
    public ResponseEntity<?> getGlobalStats() {
        Optional<Global> glob = globalsRepository.getFirstById(Long.parseLong("1"));
        if (glob.isPresent()) {
            return ResponseEntity.ok(glob.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
