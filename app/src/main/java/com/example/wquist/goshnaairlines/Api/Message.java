package com.example.wquist.goshnaairlines.Api;

public class Message {
    public String body;
    public int time;

    public Message(String body) {
        this.body = body;
        this.time = (int)(System.currentTimeMillis() / 1000L);
    }

    public Message(String body, int time) {
        this.body = body;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "body='" + body + '\'' +
                ", time=" + time +
                '}';
    }
}
