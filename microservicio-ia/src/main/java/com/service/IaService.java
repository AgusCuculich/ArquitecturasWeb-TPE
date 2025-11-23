package com.service;

import com.client.GroqClient;
import com.dto.RespuestaApi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GroqClient groqChatClient;

    private final String CONTEXTO_SQL;
    private static final Logger log = LoggerFactory.getLogger(IaService.class);

    private static final Pattern SQL_ALLOWED =
            Pattern.compile("(?is)\\b(SELECT|INSERT|UPDATE|DELETE)\\b[\\s\\S]*?;");

    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE)\\b");

    public IaService() {
        this.CONTEXTO_SQL = cargarEsquemaSQL("esquema_completo.sql");
    }

    private String cargarEsquemaSQL(String nombreArchivo) {
        try (InputStream inputStream = new ClassPathResource(nombreArchivo).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo SQL desde resources: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ResponseEntity<?> procesarPrompt(String promptUsuario) {
        try {

            String promptFinal = """
                    Este es el esquema de mi base de datos MySQL:
                    %s
                    
                    Basándote exclusivamente en este esquema, devolveme ÚNICAMENTE una sentencia SQL
                    MySQL completa y VÁLIDA (sin texto adicional, sin markdown, sin comentarios) que
                    termine con punto y coma. La sentencia puede ser SELECT/INSERT/UPDATE/DELETE.
                    
                    %s
                    """.formatted(CONTEXTO_SQL, promptUsuario);

            log.info("==== PROMPT ENVIADO A LA IA ====\n{}", promptFinal);

            String respuestaIa = groqChatClient.preguntar(promptFinal);
            log.info("==== RESPUESTA IA ====\n{}", respuestaIa);

            String sql = extraerConsultaSQL(respuestaIa);
            if (sql == null || sql.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi<>(false,
                                "No se encontró una sentencia SQL válida en la respuesta de la IA.",
                                null));
            }

            log.info("==== SQL EXTRAÍDA ====\n{}", sql);

            String sqlToExecute = sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql;

            try {
                Object data;

                if (sql.trim().toUpperCase().startsWith("SELECT")) {
                    @SuppressWarnings("unchecked")
                    List<Object[]> resultados =
                            entityManager.createNativeQuery(sqlToExecute).getResultList();

                    data = resultados;

                    return ResponseEntity.ok(
                            new RespuestaApi<>(true, "Consulta SELECT ejecutada con éxito", data)
                    );

                } else {
                    int rows = entityManager.createNativeQuery(sqlToExecute).executeUpdate();
                    data = rows;

                    return ResponseEntity.ok(
                            new RespuestaApi<>(true, "Sentencia DML ejecutada con éxito", data)
                    );
                }

            } catch (Exception e) {
                log.warn("Error al ejecutar SQL: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new RespuestaApi<>(false,
                                "Error al ejecutar la sentencia: " + e.getMessage(), null));
            }

        } catch (Exception e) {
            log.error("Fallo al procesar prompt", e);
            return new ResponseEntity<>(
                    new RespuestaApi<>(false, "Error al procesar el prompt: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private String extraerConsultaSQL(String respuesta) {
        if (respuesta == null) return null;

        Matcher m = SQL_ALLOWED.matcher(respuesta);
        if (!m.find()) return null;

        String sql = m.group().trim();

        int first = sql.indexOf(';');
        if (first > -1) {
            sql = sql.substring(0, first + 1);
        }

        if (SQL_FORBIDDEN.matcher(sql).find()) {
            log.warn("Sentencia bloqueada por contener DDL prohibido: {}", sql);
            return null;
        }

        return sql;
    }
}
