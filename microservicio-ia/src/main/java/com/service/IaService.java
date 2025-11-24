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
    private final String CONTEXTO_MONGO;

    private static final Logger log = LoggerFactory.getLogger(IaService.class);

    // ---------------- SQL VALIDATION ----------------

    private static final Pattern SQL_ALLOWED =
            Pattern.compile("(?is)\\bSELECT\\b[\\s\\S]*?;");

    private static final Pattern SQL_FORBIDDEN =
            Pattern.compile("(?i)\\b(DROP|TRUNCATE|ALTER|CREATE|GRANT|REVOKE|INSERT|UPDATE|DELETE)\\b");


    // ---------------- MONGO VALIDATION ----------------

    private static final Pattern MONGO_ALLOWED =
            Pattern.compile("(?s)\\{.*\\}"); // obtener cualquier JSON

    private static final Pattern MONGO_FORBIDDEN =
            Pattern.compile("(?i)(insert|update|delete|remove|drop|aggregate\\s*:\\s*\\{\\s*\\$out)");



    // ------------------------------------------------------------
    // CONSTRUCTOR — carga esquemas
    // ------------------------------------------------------------
    public IaService() {
        this.CONTEXTO_SQL   = cargarArchivo("esquema_completo.sql");
        this.CONTEXTO_MONGO = cargarArchivo("mongo_schema.json");
    }

    private String cargarArchivo(String archivo) {
        try (InputStream in = new ClassPathResource(archivo).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer " + archivo + ": " + e.getMessage(), e);
        }
    }



    // ------------------------------------------------------------
    // DETECTAR SI EL PROMPT ES MONGO
    // ------------------------------------------------------------
    private boolean esConsultaMongo(String prompt) {
        String p = prompt.toLowerCase();

        return p.contains("mongo")
                || p.contains("mongodb")
                || p.contains("colección")
                || p.contains("collection")
                || p.contains("ride")
                || p.contains("rides")
                || p.contains("viaje")
                || p.contains("viajes")
                || p.contains("tarifa")
                || p.contains("tarifas")
                || p.contains("fee")
                || p.contains("fees")
                || p.contains("filter")
                || p.contains("find")
                || p.contains("json");
    }



    // ------------------------------------------------------------
    // PROMPTS
    // ------------------------------------------------------------
    private String promptSQL(String userPrompt) {
        return """
            Este es el esquema de mi base de datos MySQL:
            %s
            
            VALORES VÁLIDOS PARA ENUMS DE USER:
            
            Campo "tipo" (almacenado como TINYINT):
            - 0 = PREMIUM
            - 1 = ESTANDAR
            
            Campo "rol" (almacenado como TINYINT):
            - 0 = ADMINISTRADOR
            - 1 = USUARIO
            - 2 = MANTENIMIENTO
            
            VALORES VÁLIDOS PARA ENUMS DE MONOPATIN:
            
            Campo "estado" (almacenado como TINYINT):
            - 0 = DISPONIBLE
            - 1 = OCUPADO
            - 2 = MANTENIMIENTO

            Generá SOLO una sentencia SQL tipo SELECT,
            válida para MySQL, sin texto adicional, sin markdown
            y terminada en punto y coma.

            %s
            """.formatted(CONTEXTO_SQL, userPrompt);
    }

    private String promptMongo(String userPrompt) {
        return """
            Este es el esquema de mi base MongoDB:
            %s

            Generá SOLO una consulta MongoDB válida en formato JSON,
            sin texto adicional ni markdown.

            IMPORTANTE:
            - NO uses ISODate().
            - NO uses ObjectId().
            - NO uses funciones que no sean JSON puro.
            - Todas las fechas deben ser strings ISO-8601: "2024-01-01T00:00:00.000Z".
            - Los ObjectId deben ser strings: "677f8bbbcf32d94fb9a5fd01".

            Ejemplo válido:
            {
              "find": "ride",
              "filter": { "id_user": 10 },
              "limit": 1
            }

            %s
            """.formatted(CONTEXTO_MONGO, userPrompt);
    }




    // ------------------------------------------------------------
    // EXTRACCIÓN SQL
    // ------------------------------------------------------------
    private String extraerSQL(String respuesta) {
        if (respuesta == null) return null;
        Matcher m = SQL_ALLOWED.matcher(respuesta);
        if (!m.find()) return null;

        String sql = m.group().trim();
        int idx = sql.indexOf(';');
        if (idx > -1) sql = sql.substring(0, idx + 1);

        if (SQL_FORBIDDEN.matcher(sql).find()) {
            log.warn("SQL bloqueada por operación peligrosa: {}", sql);
            return null;
        }

        return sql;
    }



    // ------------------------------------------------------------
    // EXTRACCIÓN MONGO
    // ------------------------------------------------------------
    private String extraerMongo(String respuesta) {
        if (respuesta == null) return null;

        Matcher m = MONGO_ALLOWED.matcher(respuesta);
        if (!m.find()) return null;

        String json = m.group().trim();

        if (MONGO_FORBIDDEN.matcher(json).find()) {
            log.warn("Mongo bloqueado por operación de escritura: {}", json);
            return null;
        }

        return json;
    }



    // ------------------------------------------------------------
    // DETECCIÓN DE MICROSERVICIO
    // ------------------------------------------------------------
    private String detectarMicroservicio(String consulta, boolean esMongo) {

        String c = consulta.toUpperCase();

        // -------- MONGO --------
        if (esMongo) {
            if (c.contains("RIDE") || c.contains("RIDES") ||
                    c.contains("VIAJE") || c.contains("VIAJES") ||
                    c.contains("FEE") || c.contains("TARIFA") ||
                    c.contains("\"FIND\"")) {

                return "mongo"; // microservicio de IA/Rides
            }

            throw new IllegalArgumentException("No se detectó una colección Mongo válida.");
        }


        // -------- SQL --------

        // mantenimiento → microservicio dedicado
        if (c.contains("MANTENIMIENTO")) {
            return "mantenimiento";
        }

        // monopatin / parada
        if (c.contains("MONOPATIN") || c.contains("PARADA")) {
            return "monopatin";
        }

        // usuarios
        if (c.contains("USERS") ||
                c.contains("ACCOUNT") ||
                c.contains("USUARIO_ACCOUNT")) {
            return "user";
        }

        throw new IllegalArgumentException(
                "No se pudo identificar el microservicio destino. Tablas desconocidas."
        );
    }



    // ------------------------------------------------------------
    // URL POR MICROSERVICIO
    // ------------------------------------------------------------
    private String urlMicro(String micro) {
        return switch (micro) {
            case "monopatin"      -> "http://localhost:8081/monopatines/execute-sql";
            case "mantenimiento"  -> "http://localhost:8082/mantenimientos/execute-sql";
            case "user"           -> "http://localhost:8088/users/execute-sql";
            case "mongo"          -> "http://localhost:8089/rides/execute-mongo";
            default -> throw new IllegalArgumentException("Microservicio desconocido: " + micro);
        };
    }



    private HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }



    // ------------------------------------------------------------
    // MÉTODO PRINCIPAL
    // ------------------------------------------------------------
    @Transactional
    public ResponseEntity<?> procesarPrompt(String promptUsuario) {
        try {

            boolean usarMongo = esConsultaMongo(promptUsuario);

            String promptFinal = usarMongo
                    ? promptMongo(promptUsuario)
                    : promptSQL(promptUsuario);

            log.info("=== PROMPT A IA ===\n{}", promptFinal);

            String respIa = groqChatClient.preguntar(promptFinal);

            log.info("=== RESPUESTA IA ===\n{}", respIa);

            // Extraer la consulta
            String consulta = usarMongo
                    ? extraerMongo(respIa)
                    : extraerSQL(respIa);

            if (consulta == null) {
                return ResponseEntity.badRequest()
                        .body(new RespuestaApi<>(false, "Consulta inválida producida por IA.", null));
            }


            // Detectar microservicio
            String micro = detectarMicroservicio(consulta, usarMongo);

            log.info("=== MICROSERVICIO DETECTADO === {}", micro);

            // Obtener URL
            String url = urlMicro(micro);

            // Body
            Map<String, String> body = new HashMap<>();
            if (usarMongo)
                body.put("mongoQuery", consulta);
            else
                body.put("sql", consulta);

            // Llamar al microservicio
            ResponseEntity<RespuestaApi> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(body, jsonHeaders()),
                    RespuestaApi.class
            );

            log.info("=== RESPUESTA MICROSERVICIO === {}", response.getBody());

            return response;

        } catch (Exception e) {
            log.error("Error procesando prompt", e);
            return ResponseEntity.internalServerError()
                    .body(new RespuestaApi<>(false, e.getMessage(), null));
        }
    }
}