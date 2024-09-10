package com.anchietaalbano.trabalho;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {

    private int messageType;
    private int requestId;
    private String methodId;

    private JsonObject parameters;


    public Message(String request){

        JsonObject json = JsonParser.parseString(request).getAsJsonObject();
        messageType = json.get("messageType").getAsInt();
        requestId = json.get("requestId").getAsInt();
        methodId = json.get("methodId").getAsString();
        parameters = json.get("arguments").getAsJsonObject();

    }

    public Message(int messageType, int requestId, String methodId, JsonObject params, int status) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.methodId = methodId;
        this.parameters = new JsonObject();
        this.parameters.add("result", params);
        this.parameters.addProperty("status", status);
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
        return parameters;
    }

    public String toString(){
        String s = "";
        s = ("messageType: " + getMessageType() + ", requestId: " + getRequestId() +
                ", methodId: " + getMethodId() + ", arguments: " + getParams());
        return s;
    }
}
