package edu.mirea.tanpii.petit.data.data_sources.room.root;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.mirea.tanpii.petit.data.data_sources.room.dao.DocumentDAO;
import edu.mirea.tanpii.petit.data.data_sources.room.dao.PetDAO;
import edu.mirea.tanpii.petit.data.data_sources.room.dao.PostDAO;
import edu.mirea.tanpii.petit.data.data_sources.room.dao.TodoDAO;
import edu.mirea.tanpii.petit.data.data_sources.room.dao.TreatmentDAO;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.DocumentEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.PetEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.PostEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.TodoEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.TreatmentEntity;

@Database(entities = {PetEntity.class, TodoEntity.class, PostEntity.class, TreatmentEntity.class, DocumentEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PetDAO petDAO();
    public abstract TodoDAO todoDAO();
    public abstract PostDAO postDAO();
    public abstract TreatmentDAO treatmentDAO();
    public abstract DocumentDAO documentDAO();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
