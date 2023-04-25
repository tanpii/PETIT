package edu.mirea.tanpii.petit.data.data_sources.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

import edu.mirea.tanpii.petit.data.models.Post;

@Entity(indices = {@Index(value = {"uuid"}, unique = true)},
        foreignKeys = @ForeignKey(entity = PetEntity.class, parentColumns = "uuid",
                childColumns = "petUUID", onDelete = ForeignKey.CASCADE))
public class PostEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String uuid;

    @ColumnInfo
    public String petUUID;

    @ColumnInfo
    public String date;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public String mediaURL;

    public PostEntity() {}

    public PostEntity(String uuid, String petUUID, String date, long time, String text, String mediaURL) {
        this.uuid = uuid;
        this.petUUID = petUUID;
        this.date = date;
        this.time = time;
        this.text = text;
        this.mediaURL = mediaURL;
    }

    public Post toDomainModel() {
        return new Post(uuid, petUUID, date, time, text, mediaURL);
    }
}
