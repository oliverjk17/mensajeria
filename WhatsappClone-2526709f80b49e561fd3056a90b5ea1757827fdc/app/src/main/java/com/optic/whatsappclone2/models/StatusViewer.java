package com.optic.whatsappclone2.models;

public class StatusViewer {

    private String id;
    private String idUser;
    private String idStatus;
    private long timestamp;

    public StatusViewer() {

    }

    public StatusViewer(String id, String idUser, String idStatus, long timestamp) {
        this.id = id;
        this.idUser = idUser;
        this.idStatus = idStatus;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
