package com.service;

import com.client.RideClient;
import com.dto.UserDTO;
import com.dto.UsuarioViajeCountDTO;
import com.entity.User;
import com.utils.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    private final RideClient rideClient;

    public List<UsuarioViajeCountDTO> rankingPorPeriodoYRol(Date desde, Date hasta, String rol) {

        Roles rolEnum = Roles.valueOf(rol.toUpperCase());

        List<UsuarioViajeCountDTO> rankingViajes = rideClient.obtenerUsuariosCantViajes(desde, hasta);

        List<Long> ids = rankingViajes.stream()
                .map(UsuarioViajeCountDTO::getId)
                .toList();

        List<User> usuarios = repo.findAllById(ids);

        // Filtrar por rol correctamente
        List<User> usuariosFiltrados = usuarios.stream()
                .filter(u -> u.getRol().equals(rolEnum))
                .toList();

        return usuariosFiltrados.stream().map(u -> {
            Long cantidadViajes = rankingViajes.stream()
                    .filter(r -> r.getId().equals(u.getId()))
                    .findFirst()
                    .map(UsuarioViajeCountDTO::getCantidadViajes)
                    .orElse(0L);

            return new UsuarioViajeCountDTO(
                    u.getId(),
                    u.getName(),
                    u.getSurname(),
                    u.getRol(),  // convertir enum a String
                    cantidadViajes
            );
        }).toList();
    }




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
    public List<User> getAllDebug() {
        return repo.findAll();
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

    public List<Long> getOtherUsers(Long userId) {
        return repo.findOtherUsers(userId);
    }

    public void linkAccount(Long userId, Long accountId) {
        repo.linkAccount(userId, accountId);
    }

}
