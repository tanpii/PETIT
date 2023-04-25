package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.function.Predicate;

import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class EditPetViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Pet>> mItems;

    public EditPetViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mItems = repo.getDatabaseDataPet();
    }

    public void editPet(String uuid, String name, String birthday, String photoURL) {
        repo.editPet(uuid, name, birthday, photoURL);
    }
}
