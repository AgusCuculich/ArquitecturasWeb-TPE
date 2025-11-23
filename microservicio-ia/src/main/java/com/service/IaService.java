package com.service;

import com.client.GroqClient;
import com.dto.RespuestaApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IaService {

    @Autowired
    private GroqClient groqChatClient;

    @Autowired
    private RestTemplate restTemplate;

    private final String CONTEXTO_SQL;
    private static final Logger log = LoggerFactory.getLogger(IaService.class);

    // Solo permitir SELECT para lectura
    private static final Pattern SQL_ALLOWED =
            Pattern.compile("(?is)\\b(SELECT)\\b[\\s\\S]*?;");

    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE|INSERT|UPDATE|DELETE)\\b");

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
            // Generar SQL con la IA
            String promptFinal = """
                    Este es el esquema de mi base de datos MySQL:
                    %s
                    
                    Basándote exclusivamente en este esquema, devolveme ÚNICAMENTE una sentencia SQL
                    MySQL completa y VÁLIDA (sin texto adicional, sin markdown, sin comentarios) que
                    termine con punto y coma. La sentencia DEBE ser SOLO de tipo SELECT (consulta de lectura).
                    
                    %s
                    """.formatted(CONTEXTO_SQL, promptUsuario);

            log.info("==== PROMPT ENVIADO A LA IA ====\n{}", promptFinal);

            String respuestaIa = groqChatClient.preguntar(promptFinal);
            log.info("==== RESPUESTA IA ====\n{}", respuestaIa);

            // Extraer y validar SQL
            String sql = extraerConsultaSQL(respuestaIa);
            if (sql == null || sql.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi<>(false,
                                "No se encontró una sentencia SQL válida en la respuesta de la IA.",
                                null));
            }

            log.info("==== SQL EXTRAÍDA ====\n{}", sql);

            // Detectar microservicio destino
            String microservicio;
            try {
                microservicio = detectarMicroservicio(sql);
                log.info("==== MICROSERVICIO DETECTADO ====\n{}", microservicio);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi<>(false, e.getMessage(), null));
            }

            // Obtener URL del microservicio (localhost)
            String url = detectarUrl(microservicio);
            log.info("==== ENVIANDO REQUEST A ====\n{}", url);

            // Preparar request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("sql", sql);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<RespuestaApi> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        RespuestaApi.class
                );

                log.info("==== RESPUESTA DEL MICROSERVICIO ====\nStatus: {}\nBody: {}",
                        response.getStatusCode(), response.getBody());

                return response;

            } catch (Exception e) {
                log.error("Error al comunicarse con el microservicio {}: {}", microservicio, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new RespuestaApi<>(false,
                                "Error al comunicarse con el microservicio: " + e.getMessage(),
                                null));
            }

        } catch (Exception e) {
            log.error("Fallo al procesar prompt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi<>(false,
                            "Error al procesar el prompt: " + e.getMessage(),
                            null));
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
            log.warn("Sentencia bloqueada por contener operación no permitida: {}", sql);
            return null;
        }

        return sql;
    }

    private String detectarMicroservicio(String sql) {
        String sqlUpper = sql.toUpperCase();

        // Detectar por tablas del esquema de monopatín
        if (sqlUpper.contains("MONOPATIN") ||
                sqlUpper.contains("PARADA") ||
                sqlUpper.contains("MANTENIMIENTO")) {
            return "monopatin";
        }

        // Detectar por tablas del esquema de usuarios
        if (sqlUpper.contains("USERS") ||
                sqlUpper.contains("ACCOUNT") ||
                sqlUpper.contains("USUARIO_ACCOUNT")) {
            return "user";
        }

        // Si no se detecta ninguna tabla conocida
        throw new IllegalArgumentException(
                "No se pudo identificar el microservicio destino. " +
                        "La consulta no contiene tablas conocidas (monopatin, parada, mantenimiento, users, account)."
        );
    }

    private String detectarUrl(String microservicio) {
        return switch(microservicio) {
            case "monopatin" -> "http://localhost:8082/execute-sql"; // Puerto del microservicio monopatin
            case "user" -> "http://localhost:8088/users/execute-sql";      // Puerto del microservicio user
            default -> throw new IllegalArgumentException("Microservicio desconocido: " + microservicio);
        };
    }
}