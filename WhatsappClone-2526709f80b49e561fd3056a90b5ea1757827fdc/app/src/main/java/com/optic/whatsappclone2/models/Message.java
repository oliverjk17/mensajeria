package com.optic.whatsappclone2.models;

public class Message {

    private String id;
    private String idSender;
    private String idReceiver;
    private String idChat;
    private String message;
    private String status;
    private String url;
    private String type;
    private String fileName;
    private long timestamp;


    public Message() {
    }

    public Message(String id, String idSender, String idReceiver, String idChat, String message, String status, String url, String type, String fileName, long timestamp) {
        this.id = id;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.idChat = idChat;
        this.message = message;
        this.status = status;
        this.url = url;
        this.type = type;
        this.fileName = fileName;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "idChat='" + idChat + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
