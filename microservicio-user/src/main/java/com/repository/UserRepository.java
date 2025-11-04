package com.repository;

import com.dto.UserDTO;
import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // Devolver listado DTO de todos los usuarios
    @Query("SELECT new com.dto.UserDTO(u.name, u.surname, u.mobile) FROM User u")
    List<UserDTO> findAllDTO();

    // Devuelve DTO del usuario cuyo id coincida
    @Query("SELECT new com.dto.UserDTO(u.name, u.surname, u.mobile) FROM User u WHERE u.id = :id")
    Optional<UserDTO> findDTOByUserId(@Param("id") Long id);
}
