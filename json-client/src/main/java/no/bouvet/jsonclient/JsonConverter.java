package no.bouvet.jsonclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonConverter {

    private final String CONTENT_TYPE_JSON = "application/json";

    private ObjectMapper objectMapper;

    public JsonConverter() {
        objectMapper = new JsonClientObjectMapper();
    }

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerModule(Module module) {
        objectMapper.registerModule(module);
    }

    public <T> T toObject(HttpEntity entity, Class<T> clz) {
        try {
            String entityStr = EntityUtils.toString(entity);
            if(entityStr != null && !entityStr.isEmpty()) {
                return toObject(entityStr, clz);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(getToObjectError(entity, clz, "object"), e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    public <T> List<T> toList(HttpEntity entity, Class<T> clz) {
        try {
            String entityStr = EntityUtils.toString(entity);
            if(entityStr != null && !entityStr.isEmpty()) {
                return toList(entityStr, clz);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(getToObjectError(entity, clz, "list"), e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    public <T> List<List<T>> toListOfList(HttpEntity entity, Class<T> clz) {
        try {
            String entityStr = EntityUtils.toString(entity);
            if(entityStr != null && !entityStr.isEmpty()) {
                return toListOfList(entityStr, clz);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(getToObjectError(entity, clz, "list of list"), e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    public <T> Map<String, T> toMap(HttpEntity entity, Class<T> clz) {
        try {
            String entityStr = EntityUtils.toString(entity);
            if(entityStr != null && !entityStr.isEmpty()) {
                return toMap(entityStr, clz);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(getToObjectError(entity, clz, "map"), e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error when converting " + object.getClass() + " to json", e);
        }
    }

    public <T> T toObject(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, clz);
        } catch (IOException e) {
            throw new RuntimeException("Error when converting json to " + clz, e);
        }
    }

    public <T> List<T> toList(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, getTypeFactory().constructParametricType(List.class, clz));
        } catch (IOException e) {
            throw new RuntimeException("Error when converting json to List<" + clz + ">", e);
        }
    }

    public <T> List<List<T>> toListOfList(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, getTypeFactory().constructParametricType(List.class, getTypeFactory().constructParametricType(List.class, clz)));
        } catch (IOException e) {
            throw new RuntimeException("Error when converting json to List<" + clz + ">", e);
        }
    }

    public <T> Map<String, T> toMap(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, T>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Error when converting json to Map<String, " + clz + ">", e);
        }
    }

    private TypeFactory getTypeFactory() {
        return objectMapper.getTypeFactory();
    }

    private <T> String getToObjectError(HttpEntity entity, Class<T> clz, String type) {
        String error = "Error when parsing response entity to " + type + " of " + clz + ". ";
        if (!isContentTypeJson(entity)) {
            error += "Content Type should be '" + CONTENT_TYPE_JSON + "' but was '" + getContentType(entity) + "'";
        }
        return error;
    }

    private boolean isContentTypeJson(HttpEntity entity) {
        if (getContentType(entity).contains(CONTENT_TYPE_JSON)) {
            return true;
        }
        return false;
    }

    private String getContentType(HttpEntity entity) {
        return entity.getContentType().getValue();
    }
}
