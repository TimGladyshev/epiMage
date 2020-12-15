package com.example.finalbackend.controllers;


import com.example.finalbackend.models.*;
import com.example.finalbackend.payloads.response.MessageResponse;
import com.example.finalbackend.repositories.BatchRepository;
import com.example.finalbackend.repositories.GlobalsRepository;
import com.example.finalbackend.repositories.UploadRepository;
import com.example.finalbackend.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/upload")
public class BatchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    UploadRepository uploadRepository;

    @Autowired
    GlobalsRepository globalsRepository;

    @GetMapping("/{user}/init")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> initBatch(@PathVariable("user") String userID) {
        Optional<User> foundUser = userRepository.findById(Long.parseLong(userID));
        if (foundUser.isPresent()) {
            User u = foundUser.get();
            Upload batch = new Upload();
            if (u.getBatchCount() == null) {
                u.setBatchCount(1);
            } else {
                u.setBatchCount(u.getBatchCount() + 1);
            }
            batch.setName(u.getUsername() + u.getBatchCount().toString());
            batch.setUser(u);
            u.addUpload(batch);
            userRepository.save(u);
            return ResponseEntity.ok(batch);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(new MessageResponse("This user couldn't be found"));
        }
    }

    @PostMapping("/{bid}/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addSample(@PathVariable("bid") String batchID,
                    @RequestBody Sample sample) {
        Optional<Batch> foundBatch = batchRepository.findByBatchID(batchID);
        if (foundBatch.isPresent()) {
            Batch b = foundBatch.get();
            Hibernate.initialize(b.getSampleNames());
            b.addSample(sample);
            batchRepository.save(b);
            return ResponseEntity.ok(b.getSampleNames());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Batch not found"));
        }
    }

    @PostMapping("/{uid}/share")
    @PreAuthorize("hasRole('CONTRIBUTOR')")
    public ResponseEntity<?> shareUpload(@PathVariable("uid") String upLoadId) {
        Optional<Upload> upload = uploadRepository.findById(Long.parseLong("uid"));
        if (upload.isPresent()) {
            Upload u = upload.get();
            u.setContributing(true);
            uploadRepository.save(u);
            Optional<Global> global = globalsRepository.getFirstById(Long.parseLong("1"));
            if (global.isPresent()) {
                Global g = global.get();
                if (g.getTotalShared() == null) {
                    g.setTotalShared(Long.parseLong("1"));
                } else {
                    g.setTotalShared(g.getTotalShared() + 1);
                }
            }
            return ResponseEntity.ok(u);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
