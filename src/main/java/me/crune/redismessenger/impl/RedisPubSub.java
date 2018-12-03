package me.crune.redismessenger.impl;

import me.crune.redismessenger.Messenger;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

public class RedisPubSub extends JedisPubSub {

    private final Messenger messenger;

    public RedisPubSub(Messenger messenger) {
        this.messenger = messenger;
    }

    @Override
    public void onMessage(String channel, String message) {
        CompletableFuture.runAsync(() -> {
            try {
                messenger.handle(channel, message);
            } catch (IOException | InvocationTargetException | IllegalAccessException e) {
                System.out.println("(Redis Messenger) Error whilst trying to handle message. [Channel = " + channel + "]");
                e.printStackTrace();
            }
        });
    }
}
