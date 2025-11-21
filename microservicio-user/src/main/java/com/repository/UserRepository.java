package com.repository;

import com.dto.UserDTO;
import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // Devolver listado DTO de todos los usuarios
    @Query("SELECT new com.dto.UserDTO(u.username, u.surname, u.mobile, u.rol, u.tipo) FROM User u")
    List<UserDTO> getAll();

    // Devuelve DTO del usuario cuyo id coincida
    @Query("SELECT new com.dto.UserDTO(u.username, u.surname, u.mobile, u.rol, u.tipo) FROM User u WHERE u.id = :id")
    Optional<UserDTO> fetchById(@Param("id") Long id);


    @Query(value = """
        SELECT ua2.usuario_id
        FROM usuario_account ua1
        JOIN usuario_account ua2 ON ua1.account_id = ua2.account_id
        WHERE ua1.usuario_id = :userId
          AND ua2.usuario_id <> :userId
    """, nativeQuery = true)
    List<Long> findOtherUsers(@Param("userId") Long userId);



    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usuario_account (usuario_id, account_id) VALUES (:userId, :accountId)", nativeQuery = true)
    void linkAccount(@Param("userId") Long userId, @Param("accountId") Long accountId);


    User findByUsername(String username);
}
