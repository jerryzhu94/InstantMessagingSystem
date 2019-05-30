package com.example.myapplication;

//domain object class
public class Chat {

    //constructor
    public Chat(String chatId) {
        this.chatId = chatId;
    }

    //getter method
    public String getChatId() { return chatId; }

    //private field
    private String chatId;
}
