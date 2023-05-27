package edu.mirea.tanpii.petit.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.mirea.tanpii.petit.data.data_sources.room.entities.DocumentEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.TreatmentEntity;

@Dao
public interface DocumentDAO {
    @Query("SELECT * FROM DocumentEntity WHERE petUUID = :petUUID")
    LiveData<List<DocumentEntity>> getDocumentByPetUUID(String petUUID);

    @Insert
    void addNewDocument(DocumentEntity treatment);

    @Update
    void update(DocumentEntity todo);

    @Delete
    void deleteDocument(DocumentEntity treatment);

    @Query("DELETE from DocumentEntity WHERE uuid = :uuid")
    void deleteDocumentByUUID(String uuid);
}
