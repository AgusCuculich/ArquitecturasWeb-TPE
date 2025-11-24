package com.service;


import com.dto.RespuestaApi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class SqlExecutorService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger log = LoggerFactory.getLogger(SqlExecutorService.class);

    // Patrones de seguridad
    private static final Pattern SQL_SELECT_PATTERN =
            Pattern.compile("^\\s*SELECT\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE|INSERT|UPDATE|DELETE|EXEC|EXECUTE)\\b",
                    Pattern.CASE_INSENSITIVE);

    @Transactional(readOnly = true)
    public ResponseEntity<RespuestaApi> executeQuery(String sql) {
        // Validación de entrada
        if (sql == null || sql.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RespuestaApi<>(false,
                            "La consulta SQL no puede estar vacía",
                            null));
        }

        String sqlTrimmed = sql.trim();
        log.info("==== SQL RECIBIDA PARA EJECUTAR ====\n{}", sqlTrimmed);

        // Validar que sea SELECT
        if (!SQL_SELECT_PATTERN.matcher(sqlTrimmed).find()) {
            log.warn("Intento de ejecutar consulta no-SELECT: {}", sqlTrimmed);
            return ResponseEntity.badRequest()
                    .body(new RespuestaApi<>(false,
                            "Solo se permiten consultas SELECT",
                            null));
        }

        // Validar operaciones prohibidas
        if (SQL_FORBIDDEN.matcher(sqlTrimmed).find()) {
            log.warn("Intento de ejecutar operación prohibida: {}", sqlTrimmed);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RespuestaApi<>(false,
                            "La consulta contiene operaciones no permitidas",
                            null));
        }

        try {
            // Remover punto y coma final si existe
            String sqlToExecute = sqlTrimmed.endsWith(";")
                    ? sqlTrimmed.substring(0, sqlTrimmed.length() - 1)
                    : sqlTrimmed;

            // Ejecutar consulta
            @SuppressWarnings("unchecked")
            List<Object[]> resultados = entityManager
                    .createNativeQuery(sqlToExecute)
                    .getResultList();

            log.info("Consulta ejecutada exitosamente. Filas retornadas: {}", resultados.size());

            return ResponseEntity.ok(
                    new RespuestaApi<>(true,
                            "Consulta ejecutada con éxito",
                            resultados)
            );

        } catch (Exception e) {
            log.error("Error al ejecutar consulta SQL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi<>(false,
                            "Error al ejecutar la consulta: " + e.getMessage(),
                            null));
        }
    }
}