package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import edu.mirea.tanpii.petit.data.models.Treatment;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class NewTreatmentViewModel extends AndroidViewModel {

    private PetsRepository repo;

    public NewTreatmentViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
    }

    public void addTreatment(String uuid, String petUUID, long date, String title, String note, int icon) {
        repo.addTreatment(new Treatment(uuid, petUUID, date, title, note, icon));
    }
}
