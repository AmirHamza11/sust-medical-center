package com.example.sustmedicalcenter.model;

public class Message {

    private int messageType;
    private String messageText;
    private Long sentTimeInMillies;


    public Message() {
        //empty constructor for firebase
    }

    public Message(int messageType, String messageText, Long sentTimeInMillies) {
        this.messageType = messageType;
        this.messageText = messageText;
        this.sentTimeInMillies = sentTimeInMillies;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Long getSentTimeInMillies() {
        return sentTimeInMillies;
    }

    public void setSentTimeInMillies(Long sentTimeInMillies) {
        this.sentTimeInMillies = sentTimeInMillies;
    }



}
