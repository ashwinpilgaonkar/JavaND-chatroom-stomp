package edu.udacity.java.nano.chat;

/**
 * WebSocket message model
 */
public class Message {

    private String message;
    private String type;
    private String username;

    private int onlineCount = 0;

    public Message(String message, String type, String username, int onlineCount) {
        this.message = message;
        this.type = type;
        this.username = username;
        this.onlineCount = onlineCount;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getOnlineCount() {
        return onlineCount;
    }
}
