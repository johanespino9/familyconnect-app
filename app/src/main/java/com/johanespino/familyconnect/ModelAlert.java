package com.johanespino.familyconnect;

public class ModelAlert {
    String uid;
    String groupId;
    String timemStamp;

    public ModelAlert(String uid, String groupId, String timemStamp) {
        this.uid = uid;
        this.groupId = groupId;
        this.timemStamp = timemStamp;
    }

    public ModelAlert() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTimemStamp() {
        return timemStamp;
    }

    public void setTimemStamp(String timemStamp) {
        this.timemStamp = timemStamp;
    }
}
