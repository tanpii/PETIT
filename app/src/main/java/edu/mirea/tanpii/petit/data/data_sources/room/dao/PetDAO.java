package edu.mirea.tanpii.petit.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.mirea.tanpii.petit.data.data_sources.room.entities.PetEntity;
import edu.mirea.tanpii.petit.data.models.Pet;

@Dao
public interface PetDAO {
    @Query("SELECT * FROM PetEntity")
    LiveData<List<PetEntity>> getAllPets();

//    @Query("SELECT * FROM PetEntity WHERE uuid = :petUUID")
//    <PetEntity> getByUUID(String petUUID);

    @Insert
    void addPet(PetEntity pet);

    @Update
    void update(PetEntity pet);

    @Query("UPDATE PetEntity SET name = :name, birthday = :birthday, photoURL = :photoURL WHERE uuid = :uuid")
    void updatePet(String uuid, String name, String birthday, String photoURL);

    @Delete
    void deletePet(PetEntity pet);

    @Query("DELETE from PetEntity WHERE uuid = :uuid")
    void deletePetByUUID(String uuid);
}
