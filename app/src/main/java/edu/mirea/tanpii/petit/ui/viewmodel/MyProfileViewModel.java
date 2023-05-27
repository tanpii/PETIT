package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.repositories.AuthAppRepository;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class MyProfileViewModel extends AndroidViewModel {
    private PetsRepository repo;
    private LiveData<List<Pet>> mPets;

    public MyProfileViewModel(@NonNull Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mPets = repo.getDatabaseDataPet();
    }

    public LiveData<List<Pet>> getPets() {
        return mPets;
    }

    public void deletePet(String uuid) {
        repo.deletePet(uuid);
    }
}
