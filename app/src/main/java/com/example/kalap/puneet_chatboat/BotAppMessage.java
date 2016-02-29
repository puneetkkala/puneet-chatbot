package com.example.kalap.puneet_chatboat;

public class BotAppMessage {
    private String type;
    private String content;

    public BotAppMessage(String content, String type){
        this.content = content;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
