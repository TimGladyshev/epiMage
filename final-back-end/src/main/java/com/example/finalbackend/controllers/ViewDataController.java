package com.example.finalbackend.controllers;

import com.example.finalbackend.models.User;
import com.example.finalbackend.payloads.response.MessageResponse;
import com.example.finalbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/epiMage/data/")
public class ViewDataController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public MessageResponse allAccess() {
        String message = "This data is availalbe to public.";
        return new MessageResponse(message);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public MessageResponse userAccess() {
        String message = "This Data is available to all users with accounts.";
        return new MessageResponse(message);
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public MessageResponse moderatorAccess() {

        String message = "Moderator Data.";
        return new MessageResponse(message);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse adminAccess() {
        String message = "Admin Data.";
        return new MessageResponse(message);
    }

    @GetMapping("/contributor")
    @PreAuthorize("hasRole('CONTRIBUTOR')")
    public MessageResponse contributorAccess() {
        String message = "This Data is Available to contributors";
        return new MessageResponse(message);
    }

    @GetMapping("/user/{uid}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUser(@PathVariable("uid") String userId) {
        Optional<User> user = userRepository.findById(Long.parseLong(userId));
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}