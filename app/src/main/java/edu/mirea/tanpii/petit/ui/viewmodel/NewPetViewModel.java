package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class NewPetViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Pet>> mItems;

    public NewPetViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mItems = repo.getDatabaseDataPet();
    }

    public void addPet(String uuid, String name, String birthday, String photoURL) {
        repo.addPet(new Pet(uuid, name, birthday, photoURL));
    }
}
