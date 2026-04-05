package com.junaid.finance_backend.service;

import com.junaid.finance_backend.exception.ResourceNotFoundException;
import com.junaid.finance_backend.model.User;
import com.junaid.finance_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: id=" + id));
    }

    public User update(Long id, User incoming) {
        User existing = findById(id);
        if (!existing.getEmail().equalsIgnoreCase(incoming.getEmail())
                && userRepo.existsByEmail(incoming.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + incoming.getEmail());
        }
        existing.setName(incoming.getName());
        existing.setEmail(incoming.getEmail());
        if (incoming.getPassword() != null && !incoming.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(incoming.getPassword()));
        }
        existing.setRole(incoming.getRole());
        existing.setStatus(incoming.getStatus());
        return userRepo.save(existing);
    }

    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found: id=" + id);
        }
        userRepo.deleteById(id);
    }
}
