package edu.mirea.tanpii.petit.data.data_sources.room.dao;

import androidx.room.Delete;
import androidx.room.Insert;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.mirea.tanpii.petit.data.data_sources.room.entities.PostEntity;


@Dao
public interface PostDAO {
    @Query("SELECT * FROM PostEntity ORDER BY time DESC")
    LiveData<List<PostEntity>> getAllPosts();

    @Query("SELECT * FROM PostEntity WHERE petUUID = :petUUID ORDER BY time DESC")
    LiveData<List<PostEntity>> getByPetUUID(String petUUID);

    @Insert
    void addNewPost(PostEntity post);

    @Update
    void update(PostEntity post);

    @Query("UPDATE PostEntity SET text = :text, mediaURL = :mediaURL WHERE uuid = :uuid")
    void updatePost(String uuid, String text, String mediaURL);

    @Delete
    void deletePost(PostEntity post);

    @Query("DELETE from PostEntity WHERE uuid = :uuid")
    void deletePostByUUID(String uuid);
}
