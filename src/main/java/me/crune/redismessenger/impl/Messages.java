package me.crune.redismessenger.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.crune.redismessenger.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    public static Message create() {
        return new JsonMessage();
    }

    public static String serialize(Message message) throws JsonProcessingException {
        JsonMessage jsonMessage = (JsonMessage) message;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMessage.getMap());
    }

    public static Message deserialize(String string) throws IOException {
        JsonMessage message = new JsonMessage();
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> map = mapper.readValue(string, typeRef);
        message.getMap().putAll(map);
        return message;
    }

}
