package me.crune.redismessenger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Messenger {

    <T> void registerListener(T listener);

    <T> void unregisterListener(T listener);

    void send(String channel, Message message);

    void handle(String channel, String message) throws IOException, InvocationTargetException, IllegalAccessException;

}
