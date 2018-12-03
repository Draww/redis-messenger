package me.crune.redismessenger;

@SuppressWarnings("unchecked")
public interface Message {

    <T> T get(String key);

    <T> T put(String key, T object);

}
