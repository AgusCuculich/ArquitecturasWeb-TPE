package com.controller;

import com.dto.LoginInfo;
import com.dto.Token;
import com.entity.User;
import com.repository.UserRepository;
import com.utils.PasswordUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Token login(@RequestBody LoginInfo login) {
        // 1. Buscamos el usuario en tu repositorio por su nombre
        User u = userRepository.findByUsername(login.getUsername());

        // 2. Verificamos si existe y si la contraseña coincide
        // Nota: u.getPassword() funcionará una vez agregues el campo a la entidad
        if (u == null || !PasswordUtils.authenticate(login.getPassword(), u.getPassword())) {
            return new Token("", "Usuario o contraseña incorrectos");
        }

        // 3. Si es válido, generamos el token incluyendo su ROL
        // Convertimos el Enum Roles a String para el token
        String rolUsuario = "ROLE_" + u.getRol().name();

        return new Token(getJWTToken(u.getUsername(), rolUsuario), "");
    }

    private String getJWTToken(String username, String role) {
        String secretKey = "mySecretKey"; // ¡Misma clave que en el Gateway!

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(role);

        String token = Jwts.builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10 minutos
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();

        return "Bearer " + token;
    }

}
