package me.crune.redismessenger.example;

import me.crune.redismessenger.Message;
import me.crune.redismessenger.annotation.MessageHandler;

import java.util.UUID;

public class Listener {

    private final Application application;

    public Listener(Application application) {
        this.application = application;
    }

    @MessageHandler("user-name-change") /* The channel to listen for */
    public void onUserNameChangeMessage(Message message) {
        UUID uuid = UUID.fromString(message.get("id"));
        String name = message.get("name");

        /* Get user and create new one if it's not present in the user map */
        User user = application.getUserMap().get(uuid);
        if (user == null) {
            user = new User(uuid, name);
            application.getUserMap().put(uuid, user);
        }

        /* Change the name */
        user.setName(name);
        System.out.println("(Example) Changed name for user: " + name);
    }
}
