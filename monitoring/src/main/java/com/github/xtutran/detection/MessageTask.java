package com.github.xtutran.detection;

import java.util.TimerTask;

public class MessageTask extends TimerTask {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Message.addMessageBatch();");
        Message.addMessageBatch();
    }

}
