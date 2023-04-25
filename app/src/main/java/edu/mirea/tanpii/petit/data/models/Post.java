package edu.mirea.tanpii.petit.data.models;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class Post {
    private String uuid;
    private String petUUID;
    private String text;
    private String date;
    private long time;
    private String mediaURL;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPetUUID() {
        return petUUID;
    }

    public void setPetUUID(String petUUID) {
        this.petUUID = petUUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Post(String uuid, String peyUUID, String date, long time, String text, String mediaURL) {
        this.uuid = uuid;
        this.petUUID = peyUUID;
        this.text = text;
        this.date = date;
        this.time = time;
        this.mediaURL = mediaURL;
    }

    public Post (String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return this.uuid.equals(((Post)o).uuid);
    }
}
