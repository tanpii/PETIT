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

public class ChangeProfileViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public ChangeProfileViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authAppRepository.logOut();
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
