package com.agilne.usersservice;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @Autowired
    private CustomUserRepository repo;

    @GetMapping("/users-service/users")
    public List<CustomUser> getAllUsers() {
        return repo.findAll();
    }

    @GetMapping("/users-service/users/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email) {
        return ResponseEntity.status(200).body(repo.existsByEmailAndRole(email, "USER"));
    }

    @PostMapping("/users-service/users")
    public ResponseEntity<?> createUser(@RequestBody CustomUser user, @RequestHeader("Authorization") String authorization) {
        String email = getEmail(authorization);

        if (!user.getRole().equals("OWNER") && !user.getRole().equals("USER") && !user.getRole().equals("ADMIN"))
            return ResponseEntity.status(400).body("Allowed values for role are USER, ADMIN and OWNER");

        if (repo.existsByEmail(user.getEmail()))
            return ResponseEntity.status(400).body("Already exists user with email " + user.getEmail());

        if (repo.existsByEmailAndRole(email, "ADMIN") && !user.getRole().equalsIgnoreCase("USER"))
            return ResponseEntity.status(403).body("You can only add user with USER role");

        if (user.getRole().equals("OWNER")) {
            boolean ownerExists = repo.existsByRole("OWNER");
            if (ownerExists) {
                return ResponseEntity.status(409).body("Already exists user with OWNER role");
            }
        }
        CustomUser createdUser = repo.save(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    @PutMapping("/users-service/users")
    public ResponseEntity<?> editUser(@RequestBody CustomUser user, @RequestHeader("Authorization") String authorization) {
        
        if (repo.existsById(user.getId())) {
            String email = getEmail(authorization);
        
            if (!user.getRole().equals("OWNER") && !user.getRole().equals("USER") && !user.getRole().equals("ADMIN"))
                return ResponseEntity.status(400).body("Allowed values for role are USER, ADMIN and OWNER");

            if (repo.existsByEmail(user.getEmail())) {
                if (repo.findByEmail(user.getEmail()).getId() != user.getId())
                    return ResponseEntity.status(400).body("Already exists user with email " + user.getEmail());
            }
                
            if (repo.existsByEmailAndRole(email, "ADMIN") && !user.getRole().equalsIgnoreCase("USER"))
                return ResponseEntity.status(403).body("You can only update user with USER role");
                
            if (user.getRole().equals("OWNER") && user.getId() != repo.findByRole("OWNER").getId()) {
                return ResponseEntity.status(409).body("Already exists user with OWNER role");
            }
            repo.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(204).body("User with id " + user.getId() + " doesn't exist");
    }

    @DeleteMapping("/users-service/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                
                return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted user");
            }
            return ResponseEntity.status(204).body("User with id " + id + " doesn't exist");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private String getEmail(String authorization) {
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);
        String[] emailPassword = credentials.split(":", 2);
        return emailPassword[0];
    }
}
