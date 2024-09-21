package com.anchietaalbano.trabalho;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {

    private int messageType;
    private int requestId;
    private String methodId;

    private JsonObject arguments;


    public Message(String request){
        JsonObject json = JsonParser.parseString(request).getAsJsonObject();
        messageType = json.get("messageType").getAsInt();
        requestId = json.get("requestId").getAsInt();
        methodId = json.get("methodId").getAsString();
        arguments = json.get("arguments").getAsJsonObject();

    }

    public Message() {
    }

    public void setArguments(JsonObject arguments) {
        this.arguments = arguments;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public JsonObject getParams() {
        return arguments;
    }

    public String toString(){
        String s = "";
        s = ("messageType: " + getMessageType() + ", requestId: " + getRequestId() +
                ", methodId: " + getMethodId() + ", arguments: " + getParams());
        return s;
    }
}