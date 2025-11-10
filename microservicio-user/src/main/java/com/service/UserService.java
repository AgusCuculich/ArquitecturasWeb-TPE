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

    public void saveDebug(User user) {
        repo.save(user);
    }

    public void save(UserDTO dto){
        User user = new User();

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setMobile(dto.getMobile());
        user.setRol(dto.getRol());
        user.setTipo(dto.getTipo());

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

    public void update(Long id, UserDTO updatedUser) {
        User existingUser = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el usuario con el id: " + id));

        if(updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if(updatedUser.getSurname() != null) existingUser.setSurname(updatedUser.getSurname());
        if(updatedUser.getMobile() != null) existingUser.setMobile(updatedUser.getMobile());
        if(updatedUser.getRol() != null) existingUser.setRol(updatedUser.getRol());
        if(updatedUser.getTipo() != null) existingUser.setTipo(updatedUser.getTipo());

        repo.save(existingUser);
    }

    public void disableUser(Long id){
        User user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con id: " + id));
        user.setDisabled(true);
        System.out.println(user);
        repo.save(user);
    }
}
