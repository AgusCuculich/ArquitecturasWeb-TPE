package com.service;

import com.dto.UserDTO;
import com.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    public void save(User user) {
        repo.save(user);
    }

    public void delete(User user) {
        repo.delete(user);
    }

    public List<UserDTO> findAll() {
        return repo.findAllDTO();
    }

    public Optional<UserDTO> findById(Long id) {
        return repo.findDTOByUserId(id);
    }

    public void update(User user) {
        repo.save(user);
    }
}
