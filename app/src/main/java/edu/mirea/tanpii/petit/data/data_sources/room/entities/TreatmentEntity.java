package edu.mirea.tanpii.petit.data.data_sources.room.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import edu.mirea.tanpii.petit.data.models.Treatment;

@Entity(foreignKeys = @ForeignKey(entity = PetEntity.class, parentColumns = "uuid",
                childColumns = "petUUID", onDelete = ForeignKey.CASCADE))
public class TreatmentEntity {

    @NonNull
    @PrimaryKey
    public String uuid;

    @ColumnInfo
    public String petUUID;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String note;

    @ColumnInfo
    public int icon;

    public TreatmentEntity() {
    }

    public TreatmentEntity(String uuid, String petUUID, long time, String title, String note, int icon) {
        this.uuid = uuid;
        this.petUUID = petUUID;
        this.time = time;
        this.title = title;
        this.note = note;
        this.icon = icon;
    }

    public Treatment toDomainModel() {
        return new Treatment(uuid, petUUID, time, title, note, icon);
    }
}
