package com.github.xtutran.detection;

import com.github.xtutran.ScheduleAction;
import com.github.xtutran.TaskFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class Message extends ScheduleAction<MessageTask> {

    public static int IDLE = 0, TIME_WARNING = 1, STOP = 2, DETECT = 3,
            RESUME = 4, SCREENSHOT = 5;
    static LinkedList<String> allMessages = new LinkedList<String>();
    static LinkedList<Integer> temporaryStoredMessages = new LinkedList<Integer>();
    private static String[] messages = {"idle",
            "timer will stop in " + TaskFactory.timeToPause + " seconds if no movement is detected",
            "timer stopped because of " + TaskFactory.timeToPause + " seconds idle time",
            "movement detected", "timer resumed", "screenshot"};

    public Message(int seconds) {
        super(seconds, MessageTask.class);
        // TODO Auto-generated constructor stub
    }

    public static void addMessage(int type) {
        if (type == 5) {
            System.out.println("add message screenshot");
        }
        temporaryStoredMessages.add(new Integer(type));
    }

    private static void addMessagePermanently(int type, String prefix) {
        allMessages.add(prefix + " - " + messages[type]);
    }

    public static void addMessageBatch() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH-mm-ss a");
        // get current date time with Calendar()
        Calendar currentTime = Calendar.getInstance();
        String prefix = dateFormat.format(currentTime.getTime());
        for (int i = 0; i < temporaryStoredMessages.size(); i++) {
            addMessagePermanently(temporaryStoredMessages.get(i).intValue(),
                    prefix);
        }
        temporaryStoredMessages = new LinkedList<Integer>();
    }

    public static String getLogRepresentation() {
        StringBuilder sb = new StringBuilder();
        System.out.println("size" + allMessages.size());
        for (int i = 0; i < allMessages.size(); i++) {
            sb.append(allMessages.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }
}

