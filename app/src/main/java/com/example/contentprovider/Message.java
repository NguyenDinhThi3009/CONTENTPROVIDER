package com.example.contentprovider;

public class Message {
    private String address;
    private String body;

    public Message(String address, String body) {
        this.address = address;
        this.body = body;
    }

    public String getAddress() { return address; }
    public String getBody() { return body; }
}
