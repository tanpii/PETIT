package edu.mirea.tanpii.petit.data.models;

public class Todo {
    public final static int standard_icon = 0;
    public final static int walk_icon = 1;
    public final static int play_icon = 2;
    public final static int eat_icon = 3;
    public final static int medicine_icon = 4;
    public final static int birthday_icon = 5;
    public final static int grooming_icon = 6;
    public final static int cleaning_icon = 7;

    private String uuid;
    private String petUUID;
    private long date;
    private String text;
    private int icon;

    public String getUuid() {
        return uuid;
    }
    public String getPetUUID() {
        return petUUID;
    }
    public long getDate() {
        return date;
    }
    public String getText() {
        return text;
    }
    public int getIcon() {return icon;}

    public Todo(String uuid, String petUUID, long date, String text, int icon) {
        this.uuid = uuid;
        this.petUUID = petUUID;
        this.date = date;
        this.text = text;
        this.icon = icon;
    }
}
