package me.crune.redismessenger;

import me.crune.redismessenger.annotation.MessageHandler;

public class TestListener {

    @MessageHandler("user-join")
    public void onMessage(Message message) {
        String user = message.get("user");
    }

}
