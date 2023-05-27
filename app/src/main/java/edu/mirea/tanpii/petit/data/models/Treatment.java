package edu.mirea.tanpii.petit.data.models;

public class Treatment {
    public final static int medicine_icon = 0;
    public final static int search_icon = 1;
    public final static int clinic_icon = 2;
    public final static int tablet_icon = 3;

    private String uuid;
    private String petUUID;
    private long date;
    private String title;
    private String note;
    private int icon;

    public String getUuid() {
        return uuid;
    }

    public long getDate() {
        return date;
    }

    public String getPetUUID() {
        return petUUID;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getIcon() {
        return icon;
    }

    public Treatment(String uuid, String petUUID, long date, String title, String note, int icon) {
        this.uuid = uuid;
        this.date = date;
        this.petUUID = petUUID;
        this.title = title;
        this.note = note;
        this.icon = icon;
    }
}
