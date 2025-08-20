package com.example.cs;

public class Message {
    private String senderId;
    private String receiverId;
    private String senderName;
    private String messageText;
    private String timestamp;

    // Default constructor required for Firebase
    public Message() {}

    // Constructor to initialize all fields
    public Message(String senderId, String receiverId, String senderName, String messageText, String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getters
    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
