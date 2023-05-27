package edu.mirea.tanpii.petit.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.mirea.tanpii.petit.data.data_sources.room.entities.DocumentEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.PetEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.PostEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.TodoEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.entities.TreatmentEntity;
import edu.mirea.tanpii.petit.data.data_sources.room.root.AppDatabase;
import edu.mirea.tanpii.petit.data.models.Document;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.data.models.Treatment;
import kotlin.jvm.functions.Function1;

public class PetsRepository {
    private AppDatabase databaseSource;

    public PetsRepository(Application application) {
        this.databaseSource = AppDatabase.getDatabase(application);
    }

    public LiveData<List<Pet>> getDatabaseDataPet() {
        return Transformations.map(
                databaseSource.petDAO().getAllPets(),
                (values) -> values.stream().map(PetEntity::toDomainModel).collect(Collectors.toList())
        );
    }

    public LiveData<List<Todo>> getDatabaseDataAllTodo() {
        return Transformations.map(
                databaseSource.todoDAO().getAllTodo(),
                (values) -> values.stream().map(TodoEntity::toDomainModel).collect(Collectors.toList())
        );
    }

    public LiveData<List<Todo>> getDatabaseDataTodo(String id) {
        return Transformations.map(
                databaseSource.todoDAO().getByPetUUID(id),
                (values) -> values.stream().map(TodoEntity::toDomainModel).collect(Collectors.toList())
        );
    }

    public LiveData<List<Post>> getDatabaseDataAllPost() {
        return Transformations.map(
                databaseSource.postDAO().getAllPosts(),
                (values) -> values.stream().map(PostEntity::toDomainModel).collect(Collectors.toList())
        );
    }

    public LiveData<List<Post>> getDatabaseDataPost(String id) {
        return Transformations.map(
                databaseSource.postDAO().getByPetUUID(id),
                (values) -> values.stream().map(PostEntity::toDomainModel).collect(Collectors.toList())
        );
    }

    public LiveData<List<Treatment>> getDatabaseDataTreatment(String id) {
        return Transformations.map(
                databaseSource.treatmentDAO().getTreatmentByPetUUID(id),
                (values) -> values.stream().map(TreatmentEntity::toDomainModel).collect(Collectors.toList())
        );
    }

    public void addPet(Pet newPet) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.petDAO().addPet(new PetEntity(newPet.getUUID(), newPet.getName(), newPet.getBirthday(), newPet.getPhotoURL()));
        });
    }

    public void addTodo(Todo newTodo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.todoDAO().addNewTodo(new TodoEntity(newTodo.getUuid(), newTodo.getPetUUID(), newTodo.getDate(), newTodo.getText(), newTodo.getIcon()));
        });
    }

    public void addPost(Post newPost) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.postDAO().addNewPost(new PostEntity(newPost.getUuid(), newPost.getPetUUID(), newPost.getDate(), newPost.getTime(), newPost.getText(), newPost.getMediaURL()));
        });
    }

    public void addTreatment(Treatment newTreatment) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.treatmentDAO().addNewTreatment(new TreatmentEntity(newTreatment.getUuid(), newTreatment.getPetUUID(), newTreatment.getDate(), newTreatment.getTitle(), newTreatment.getNote(), newTreatment.getIcon()));
        });
    }

    public void deletePet(String uuid) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.petDAO().deletePetByUUID(uuid);
        });
    }

    public void deleteTodo(String uuid) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.todoDAO().deleteTodoByUUID(uuid);
        });
    }

    public void deleteTreatment(String uuid) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.treatmentDAO().deleteTreatmentByUUID(uuid);
        });
    }

    public void editPet(String uuid, String name, String birthday, String photoURL) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.petDAO().updatePet(uuid, name, birthday, photoURL);
        });
    }

    public void deletePost(String uuid) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.postDAO().deletePostByUUID(uuid);
        });
    }

    public void editPost(String uuid, String text, String mediaURL) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.postDAO().updatePost(uuid, text, mediaURL);
        });
    }

    public void deleteDocument(String uuid) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.documentDAO().deleteDocumentByUUID(uuid);
        });
    }

    public void addDocument(Document newDocument) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            databaseSource.documentDAO().addNewDocument(new DocumentEntity(newDocument.getUuid(), newDocument.getPetUUID(), newDocument.getTitle(), newDocument.getDocumentURL()));
        });
    }

    public LiveData<List<Document>> getDatabaseDataDocument(String id) {
        return Transformations.map(
                databaseSource.documentDAO().getDocumentByPetUUID(id), documentEntities -> documentEntities.stream().map(DocumentEntity::toDomainModel).collect(Collectors.toList())
        );
    }
}
