package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class NewTodoViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Todo>> mItems;

    public NewTodoViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mItems = repo.getDatabaseDataAllTodo();
    }

    public LiveData<List<Todo>> getPets() {
        return mItems;
    }

    public void addTodo(String uuid, String petUUID, String petImageURL, long date, String text, int icon) {
        repo.addTodo(new Todo(uuid, petUUID, petImageURL, date, text, icon));
    }
}
