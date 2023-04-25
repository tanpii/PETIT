package edu.mirea.tanpii.petit.data.data_sources.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.mirea.tanpii.petit.data.models.Pet;

@Entity(indices = {@Index(value = {"uuid"}, unique = true)})
public class PetEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String uuid;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String birthday;

    @ColumnInfo
    public String photoURL;

    public PetEntity() {
    }

    public PetEntity(String uuid, String name, String birthday, String photoURL) {
        this.uuid = uuid;
        this.name = name;
        this.birthday = birthday;
        this.photoURL = photoURL;
    }

    public Pet toDomainModel() {
        return new Pet(uuid, name, birthday, photoURL);
    }
}
