package edu.mirea.tanpii.petit.data.data_sources.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.mirea.tanpii.petit.data.models.Todo;

@Entity (indices = {@Index(value = {"uuid"}, unique = true)},
        foreignKeys = @ForeignKey(entity = PetEntity.class, parentColumns = "uuid",
                childColumns = "petUUID", onDelete = ForeignKey.CASCADE))
public class TodoEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String uuid;

    @ColumnInfo
    public String petUUID;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public int icon;

    public TodoEntity() {
    }

    public TodoEntity(String uuid, String petUUID, long time, String text, int icon) {
        this.uuid = uuid;
        this.petUUID = petUUID;
        this.time = time;
        this.text = text;
        this.icon = icon;
    }

    public Todo toDomainModel() {
        return new Todo(uuid, petUUID, time, text, icon);
    }
}
