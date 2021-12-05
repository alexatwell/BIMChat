package com.example.myapplication.messages;

public class MessagesList {

    private String name, mobile, lastMessage, profilePic;

    private int unseenMessages;

    public MessagesList(String name, String mobile, String lastMessage, String profilePic, int unseenMessages) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
    }

    public MessagesList() {

    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

}