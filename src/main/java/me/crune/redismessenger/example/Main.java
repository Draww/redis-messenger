package me.crune.redismessenger.example;

import me.crune.redismessenger.Message;
import me.crune.redismessenger.Messenger;
import me.crune.redismessenger.impl.Messages;
import me.crune.redismessenger.impl.RedisMessenger;

import java.util.UUID;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Messenger messenger = new RedisMessenger("127.0.0.1", 6379);
        Application application = new Application();

        messenger.registerListener(new Listener(application)); /* Register the listener */

        User bob = new User(UUID.randomUUID(), "Crune");

        Message message = Messages.create(); /* Create a new message and set values */
        message.put("id", bob.getUuid().toString());
        message.put("name", "Jesper");

        messenger.send("user-name-change", message); /* Send the message to the specified channel */

        while (true) {
            /* Wait for response */
        }
    }

}
