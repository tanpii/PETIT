package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.function.Predicate;

import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class MyPetsViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Pet>> mPets;
    private LiveData<List<Todo>> mTodos;

    public MyPetsViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mPets = repo.getDatabaseDataPet();
        mTodos = repo.getDatabaseDataAllTodo();
    }

    public Pet getPetByUUID(String UUID) {
        if (mPets.getValue() != null)
            return mPets.getValue().stream().filter(new Predicate<Pet>() {
                @Override
                public boolean test(Pet pet) {
                    return (pet.getUUID()).equals(UUID);
                }
            }).findAny().orElse(null);
        return null;
    }

    public LiveData<List<Pet>> getPets() {
        return mPets;
    }

    public LiveData<List<Todo>> getTodoByPetUUID(@NonNull Pet pet) {
        return repo.getDatabaseDataTodo(pet.getUUID());
    }

    public LiveData<List<Todo>> getTodos() {
        return mTodos;
    }

    public void deletePet(String uuid) {
        repo.deletePet(uuid);
    }

    public void deleteTodo(String uuid) {
        repo.deleteTodo(uuid);
    }
}