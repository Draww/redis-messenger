package me.crune.redismessenger.example;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application {

    private final Map<UUID, User> userMap;

    public Application() {
        this.userMap = new HashMap<>();
    }

    public Map<UUID, User> getUserMap() {
        return userMap;
    }
}
