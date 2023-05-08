package com.example.sustmedicalcenter.model;

import java.net.URL;

public class InboxPerson {

    private String personImageUrl;
    private String personUserName;
    private String lastMessage;
    private long lastMessageDate;
    private String personUid;

    public InboxPerson() {
        //required for database
    }

    public InboxPerson(String personImageUrl, String personUserName, String lastMessage, long lastMessageDate, String personUid) {
        this.personImageUrl = personImageUrl;
        this.personUserName = personUserName;
        this.lastMessage = lastMessage;
        this.lastMessageDate = lastMessageDate;
        this.personUid = personUid;
    }

    public String getPersonImageUrl() {
        return personImageUrl;
    }

    public void setPersonImageUrl(String personImageUrl) {
        this.personImageUrl = personImageUrl;
    }

    public String getPersonUserName() {
        return personUserName;
    }

    public void setPersonUserName(String personUserName) {
        this.personUserName = personUserName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getPersonUid() {
        return personUid;
    }

    public void setPersonUid(String personUid) {
        this.personUid = personUid;
    }
}
