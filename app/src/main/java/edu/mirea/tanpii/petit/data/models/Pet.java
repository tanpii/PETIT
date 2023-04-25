package edu.mirea.tanpii.petit.data.models;

public class Pet {
    private String uuid;
    private String name;
    private String birthday;
    private String photoURL;

    public String getUUID() {
        return uuid;
    }
    public String getName() {
        return name;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getPhotoURL() {return photoURL;}

    public Pet(String uuid, String name, String birthday, String photoURL) {
        this.uuid = uuid;
        this.name = name;
        this.birthday = birthday;
        this.photoURL = photoURL;
    }
}
