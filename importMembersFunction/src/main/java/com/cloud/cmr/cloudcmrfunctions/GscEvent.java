package com.cloud.cmr.cloudcmrfunctions;

import java.util.Date;

public class GscEvent {
    // Cloud Functions uses GSON to populate this object.
    // Field types/names are specified by Cloud Functions
    // Changing them may break your code!
    private String bucket;
    private String name;
    private String metageneration;
    private Date timeCreated;
    private Date updated;
    private String selfLink;
    private String mediaLink;
    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetageneration() {
        return metageneration;
    }

    public void setMetageneration(String metageneration) {
        this.metageneration = metageneration;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
