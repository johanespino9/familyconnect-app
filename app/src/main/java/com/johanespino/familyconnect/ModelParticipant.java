package com.johanespino.familyconnect;

public class ModelParticipant {
    String uid;
    String role;
    String timeStamp;

    public ModelParticipant() {
    }

    public ModelParticipant(String uid, String role, String timeStamp) {
        this.uid = uid;
        this.role = role;
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
