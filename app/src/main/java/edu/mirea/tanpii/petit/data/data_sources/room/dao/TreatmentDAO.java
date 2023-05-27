package edu.mirea.tanpii.petit.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.mirea.tanpii.petit.data.data_sources.room.entities.TreatmentEntity;

@Dao
public interface TreatmentDAO {
    @Query("SELECT * FROM TreatmentEntity WHERE petUUID = :petUUID ORDER BY time DESC")
    LiveData<List<TreatmentEntity>> getTreatmentByPetUUID(String petUUID);

    @Insert
    void addNewTreatment(TreatmentEntity treatment);

    @Update
    void update(TreatmentEntity todo);

    @Delete
    void deleteTodo(TreatmentEntity treatment);

    @Query("DELETE from TreatmentEntity WHERE uuid = :uuid")
    void deleteTreatmentByUUID(String uuid);
}
