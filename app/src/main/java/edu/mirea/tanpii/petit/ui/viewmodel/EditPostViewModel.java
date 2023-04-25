package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class EditPostViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Post>> mItems;

    public EditPostViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mItems = repo.getDatabaseDataAllPost();
    }

    public void editPost(String uuid, String text, String mediaURL) {
        repo.editPost(uuid, text, mediaURL);
    }
}