package com.example.travel_app.Chat;

import java.util.List;

public class ChatRequest {
    private String model;
    private List<Message> messages;

    // Constructor
    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    // Getters và Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

