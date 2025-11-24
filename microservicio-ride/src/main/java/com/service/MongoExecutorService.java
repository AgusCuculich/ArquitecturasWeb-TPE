package com.service;

import com.dto.RespuestaApi;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MongoExecutorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public RespuestaApi<?> executeMongoQuery(String jsonQuery) {
        try {
            log.info("=== Recibido JSON Mongo Query === {}", jsonQuery);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonQuery);

            // ----------- COLLECTION (find) -----------
            if (!root.has("find")) {
                return new RespuestaApi<>(false, "Falta el campo 'find'.", null);
            }
            String collection = root.get("find").asText();

            Query query = new Query();

            // ----------- FILTER -----------
            if (root.has("filter")) {
                JsonNode filterNode = root.get("filter");
                buildFilter(filterNode, query);
            }

            // ----------- SORT -----------
            if (root.has("sort")) {
                JsonNode sortNode = root.get("sort");
                Iterator<String> fieldNames = sortNode.fieldNames();

                while (fieldNames.hasNext()) {
                    String field = fieldNames.next();
                    int direction = sortNode.get(field).asInt();
                    query.with(Sort.by(direction == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, field));
                }
            }

            // ----------- LIMIT -----------
            if (root.has("limit")) {
                int limit = root.get("limit").asInt();
                query.limit(limit);
            }

            // ----------- PROJECTION -----------
            if (root.has("projection")) {
                JsonNode projectionNode = root.get("projection");
                Iterator<Map.Entry<String, JsonNode>> fields = projectionNode.fields();

                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    if (field.getValue().asInt() == 1) {
                        query.fields().include(field.getKey());
                    } else {
                        query.fields().exclude(field.getKey());
                    }
                }
            }

            // ----------- EXECUTE -----------
            List<Document> result = mongoTemplate.find(query, Document.class, collection);

            log.info("=== Query MongoDB generada: {} ===", query);
            log.info("=== Resultado Mongo: {} documentos ===", result.size());

            return new RespuestaApi<>(true, "OK", result);

        } catch (Exception e) {
            log.error("Error ejecutando Mongo: {}", e.getMessage(), e);
            return new RespuestaApi<>(false, "Error ejecutando MongoDB: " + e.getMessage(), null);
        }
    }

    /**
     * Construye los criterios de filtro soportando operadores MongoDB
     */
    private void buildFilter(JsonNode filterNode, Query query) {
        Iterator<Map.Entry<String, JsonNode>> fields = filterNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode valueNode = field.getValue();

            // Si el valor es un objeto, puede contener operadores MongoDB ($gte, $lt, etc.)
            if (valueNode.isObject() && !valueNode.isEmpty()) {
                Criteria criteria = Criteria.where(fieldName);
                boolean hasOperator = false;

                Iterator<Map.Entry<String, JsonNode>> operators = valueNode.fields();
                while (operators.hasNext()) {
                    Map.Entry<String, JsonNode> op = operators.next();
                    String operator = op.getKey();
                    JsonNode opValue = op.getValue();

                    // Convertir el valor (fechas, números, strings)
                    Object value = parseValue(opValue);

                    switch (operator) {
                        case "$eq" -> criteria.is(value);
                        case "$ne" -> criteria.ne(value);
                        case "$gt" -> criteria.gt(value);
                        case "$gte" -> criteria.gte(value);
                        case "$lt" -> criteria.lt(value);
                        case "$lte" -> criteria.lte(value);
                        case "$in" -> {
                            List<?> list = new ObjectMapper().convertValue(opValue, List.class);
                            criteria.in(list);
                        }
                        case "$nin" -> {
                            List<?> list = new ObjectMapper().convertValue(opValue, List.class);
                            criteria.nin(list);
                        }
                        case "$regex" -> {
                            String pattern = opValue.asText();
                            criteria.regex(pattern);
                        }
                        case "$exists" -> criteria.exists(opValue.asBoolean());
                        default -> {
                            log.warn("Operador no reconocido: {}", operator);
                            continue;
                        }
                    }
                    hasOperator = true;
                }

                if (hasOperator) {
                    query.addCriteria(criteria);
                    continue;
                }
            }

            // Valores simples (sin operadores) - igualdad directa con flexibilidad
            if (valueNode.isNumber()) {
                addFlexibleNumericCriteria(query, fieldName, valueNode);
            }
            else if (valueNode.isTextual()) {
                addFlexibleTextCriteria(query, fieldName, valueNode.asText());
            }
            else if (valueNode.isBoolean()) {
                query.addCriteria(Criteria.where(fieldName).is(valueNode.asBoolean()));
            }
            else if (valueNode.isNull()) {
                query.addCriteria(Criteria.where(fieldName).is(null));
            }
        }
    }

    /**
     * Parsea valores intentando convertir strings de fecha a Date
     */
    private Object parseValue(JsonNode node) {
        if (node.isTextual()) {
            String text = node.asText();
            // Intentar parsear como fecha ISO 8601
            if (text.matches("\\d{4}-\\d{2}-\\d{2}T.*")) {
                try {
                    return Date.from(java.time.Instant.parse(text));
                } catch (Exception e) {
                    log.warn("No se pudo parsear fecha: {}", text);
                    return text;
                }
            }
            return text;
        } else if (node.isNumber()) {
            if (node.isInt()) return node.asInt();
            if (node.isLong()) return node.asLong();
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        }
        return new ObjectMapper().convertValue(node, Object.class);
    }

    /**
     * Para valores numéricos, busca tanto como número como string
     * Ejemplo: "id_user": 10 → busca 10 O "10"
     */
    private void addFlexibleNumericCriteria(Query query, String fieldName, JsonNode valueNode) {
        if (valueNode.isInt()) {
            int intVal = valueNode.asInt();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(fieldName).is(intVal),
                    Criteria.where(fieldName).is(String.valueOf(intVal))
            ));
        }
        else if (valueNode.isLong()) {
            long longVal = valueNode.asLong();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(fieldName).is(longVal),
                    Criteria.where(fieldName).is(String.valueOf(longVal))
            ));
        }
        else if (valueNode.isDouble() || valueNode.isFloat()) {
            double doubleVal = valueNode.asDouble();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(fieldName).is(doubleVal),
                    Criteria.where(fieldName).is(String.valueOf(doubleVal))
            ));
        }
    }

    /**
     * Para campos de texto, busca con case-insensitive (útil para enums)
     * Ejemplo: "premium" encuentra "PREMIUM", "Premium", "premium"
     */
    private void addFlexibleTextCriteria(Query query, String fieldName, String textValue) {
        // Buscar con regex case-insensitive para mayor flexibilidad
        query.addCriteria(Criteria.where(fieldName).regex("^" + textValue + "$", "i"));
    }
}
