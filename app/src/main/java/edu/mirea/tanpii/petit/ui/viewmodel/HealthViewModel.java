package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.mirea.tanpii.petit.data.models.Document;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.data.models.Treatment;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class HealthViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Pet>> mPets;

    public HealthViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mPets = repo.getDatabaseDataPet();
    }

    public LiveData<List<Pet>> getPets() {
        return mPets;
    }

    public LiveData<List<Treatment>> getTreatments(Pet pet) {
        return repo.getDatabaseDataTreatment(pet.getUUID());
    }

    public LiveData<List<Document>> getDocuments(Pet pet) {
        return repo.getDatabaseDataDocument(pet.getUUID());
    }

    public void deleteTreatment(String uuid) {
        repo.deleteTreatment(uuid);
    }

    public void deleteDocument(String uuid) {
        repo.deleteDocument(uuid);
    }

    public void addDocument(String uuid, String petUUID, String title, String URL) {
        repo.addDocument(new Document(uuid, petUUID, title, URL));
    }
}
