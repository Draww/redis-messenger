package me.crune.redismessenger.impl;

import me.crune.redismessenger.Message;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class JsonMessage implements Message {

    private final Map<String, Object> map;

    JsonMessage() {
        this.map = new HashMap<>();
    }

    @Override
    public <T> T get(String key) {
        Object o = map.get(key);
        return o == null ? null : (T) o;
    }

    @Override
    public <T> T put(String key, T object) {
        return (T) map.put(key, object);
    }

    Map<String, Object> getMap() {
        return map;
    }
}
