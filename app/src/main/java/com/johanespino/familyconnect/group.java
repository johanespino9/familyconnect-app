package com.johanespino.familyconnect;

public class group {
    String createdBy,groupId,groupTitle;

    public group(String createdBy, String groupId, String groupTitle) {
        this.createdBy = createdBy;
        this.groupId = groupId;
        this.groupTitle = groupTitle;
    }

    public group() {
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}

