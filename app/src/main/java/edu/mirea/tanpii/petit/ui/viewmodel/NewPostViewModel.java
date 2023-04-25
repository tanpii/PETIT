package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class NewPostViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Post>> mItems;

    public NewPostViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mItems = repo.getDatabaseDataAllPost();
    }

    public LiveData<List<Post>> getPosts() {
        return mItems;
    }

    public void addPost(String uuid, String petUUID, String date, long time, String text, String mediaURL) {
        repo.addPost(new Post(uuid, petUUID, date, time, text, mediaURL));
    }
}
