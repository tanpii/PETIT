package edu.mirea.tanpii.petit.data.models;

public class Document {
    private String uuid;
    private String petUUID;
    private String title;
    private String documentURL;

    public String getUuid() {
        return uuid;
    }

    public String getPetUUID() {
        return petUUID;
    }

    public String getTitle() {
        return title;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public Document(String uuid, String petUUID, String title, String documentURL) {
        this.uuid = uuid;
        this.petUUID = petUUID;
        this.title = title;
        this.documentURL = documentURL;
    }
}
