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

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<UserDTO> getAll() {
        return repo.getAll();
    }

    public Optional<UserDTO> fetchById(Long id) {
        return repo.fetchById(id);
    }

    public void update(Long id, User user) {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el usuario con id: " + id));

        if (user.getName() != null) existingUser.setName(user.getName());
        if (user.getSurname() != null) existingUser.setSurname(user.getSurname());
        if (user.getMobile() != null) existingUser.setMobile(user.getMobile());
        if (user.getRol() != null) existingUser.setRol(user.getRol());
        if (user.getTipo() != null) existingUser.setTipo(user.getTipo());

        repo.save(existingUser);
    }
}
