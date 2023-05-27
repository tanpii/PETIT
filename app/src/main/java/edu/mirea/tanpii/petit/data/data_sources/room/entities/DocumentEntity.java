package edu.mirea.tanpii.petit.data.data_sources.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import edu.mirea.tanpii.petit.data.models.Document;


@Entity(foreignKeys = @ForeignKey(entity = PetEntity.class, parentColumns = "uuid",
        childColumns = "petUUID", onDelete = ForeignKey.CASCADE))
public class DocumentEntity {

    @NonNull
    @PrimaryKey
    public String uuid;

    @ColumnInfo
    public String petUUID;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String documentURL;

    public DocumentEntity() {
    }

    public DocumentEntity(String uuid, String petUUID, String title, String documentURL) {
        this.uuid = uuid;
        this.petUUID = petUUID;
        this.title = title;
        this.documentURL = documentURL;
    }

    public Document toDomainModel() {
        return new Document(uuid, petUUID, title, documentURL);
    }
}
