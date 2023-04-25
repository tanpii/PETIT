package edu.mirea.tanpii.petit.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.mirea.tanpii.petit.data.data_sources.room.entities.TodoEntity;

@Dao
public interface TodoDAO {
    @Query("SELECT * FROM TodoEntity ORDER BY time")
    LiveData<List<TodoEntity>> getAllTodo();

    @Query("SELECT * FROM TodoEntity WHERE petUUID = :petUUID ORDER BY time")
    LiveData<List<TodoEntity>> getByPetUUID(String petUUID);

    @Insert
    void addNewTodo(TodoEntity todo);

    @Update
    void update(TodoEntity todo);

    @Delete
    void deleteTodo(TodoEntity todo);

    @Query("DELETE from TodoEntity WHERE uuid = :uuid")
    void deleteTodoByUUID(String uuid);
}
