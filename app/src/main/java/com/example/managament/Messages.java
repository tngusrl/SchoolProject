package com.example.managament;

public class Messages {

    private String message, type;
    private String time;
    private String from;

    public Messages(String message, String time, String type, String from) {
        this.message = message;
        this.time = time;
        this.type = type;
        this.from = from;
    }

    public Messages() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
