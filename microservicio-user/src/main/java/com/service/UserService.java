package com.service;

import com.client.RideClient;
import com.dto.ReporteTarifaDTO;
import com.dto.UserDTO;
import com.dto.UsuarioViajeCountDTO;
import com.entity.User;
import com.utils.PasswordUtils;
import com.utils.Roles;
import com.utils.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final RideClient rideClient;

    public void save(UserDTO dto) {

        // 1. VALIDACIÓN PREVENTIVA: ¿Ya existe el usuario?
        // Buscamos si hay alguien con ese nombre. Si devuelve algo distinto de null, es que ya existe.
        if (repo.findByUsername(dto.getUsername()) != null) {
            // Lanzamos un error 409 CONFLICT con el mensaje personalizado
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El nombre de usuario '" + dto.getUsername() + "' ya existe. Por favor elija otro.");
        }

        // 2. Si no existe, procedemos con la creación normal
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setSurname(dto.getSurname());
        user.setMobile(dto.getMobile());
        user.setRol(dto.getRol());
        user.setTipo(dto.getTipo());

        // 3. Encriptación (Como ya lo tenías)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String passHasheada = PasswordUtils.hashPassword(dto.getPassword());
            user.setPassword(passHasheada);
        } else {
            // 400 BAD REQUEST si falta la contraseña
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }

        repo.save(user);
    }

    // ... el resto de tus métodos ...



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
                    u.getUsername(),
                    u.getSurname(),
                    u.getRol(),  // convertir enum a String
                    cantidadViajes
            );
        }).toList();
    }

    public ReporteTarifaDTO getReporteTarifas(Date desde, Date hasta) {

        return rideClient.reporteTarifas(desde, hasta);
    }


    public void saveDebug(User user) {
        repo.save(user);
    }


    public void delete(Long id) {
        User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repo.delete(user);
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
                .orElseThrow(() -> new UserNotFoundException(id));

        if(updatedUser.getUsername() != null) existingUser.setName(updatedUser.getUsername());
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
