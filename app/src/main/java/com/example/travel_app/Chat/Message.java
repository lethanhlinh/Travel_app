package com.example.travel_app.Chat;

public class Message {
    private String role;    // "system", "user", hoặc "assistant"
    private String content; // Nội dung tin nhắn

    // Constructor
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // Getters và Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

