package me.crune.redismessenger.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.crune.redismessenger.Message;
import me.crune.redismessenger.Messenger;
import me.crune.redismessenger.annotation.MessageHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class RedisMessenger implements Messenger {

    private static final String REDIS_CHANNEL = "redis:messenger";
    private static final String CHANNEL_KEY = "secret_channel_1337";

    private final Set<Object> set;
    private final JedisPool pool;
    private final RedisPubSub pubSub;

    public RedisMessenger(String host, int port) {
        this(new JedisPool(host, port));
    }

    public RedisMessenger(JedisPool pool) {
        this.pool = pool;
        this.set = new HashSet<>();
        this.pubSub = new RedisPubSub(this);

        start();

        try {
            TimeUnit.SECONDS.sleep(2); /* Sleep to make sure it subscribes in time */
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("(Redis Messenger) Successfully started.");
    }

    private void start() {
        System.out.println("(Redis Messenger) Starting.");

        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(pubSub, REDIS_CHANNEL);
            }
        });
    }

    @Override
    public <T> void registerListener(T listener) {
        set.add(listener);
    }

    @Override
    public <T> void unregisterListener(T listener) {
        set.remove(listener);
    }

    @Override
    public void send(String channel, Message message) {
        try (Jedis jedis = pool.getResource()) {
            message.put(CHANNEL_KEY, channel);
            jedis.publish(REDIS_CHANNEL, Messages.serialize(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(String channel, String message) throws IOException, InvocationTargetException, IllegalAccessException {
        Message msg = Messages.deserialize(message);

        for (Object listener : set) {
            for (Method method : getMethods(listener.getClass())) {
                MessageHandler handler = method.getAnnotation(MessageHandler.class);
                if (!handler.value().equalsIgnoreCase(msg.get(CHANNEL_KEY))) return;
                method.invoke(listener, msg);
            }
        }
    }

    private List<Method> getMethods(Class<?> aClass) {
        List<Method> list = new ArrayList<>();

        for (Method method : aClass.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(MessageHandler.class)) list.add(method);
        }

        return list;
    }
}
