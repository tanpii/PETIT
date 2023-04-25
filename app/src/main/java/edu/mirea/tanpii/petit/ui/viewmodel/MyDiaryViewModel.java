package edu.mirea.tanpii.petit.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.data.repositories.PetsRepository;

public class MyDiaryViewModel extends AndroidViewModel {
    private PetsRepository repo;

    private LiveData<List<Pet>> mPets;
    private LiveData<List<Post>> mPosts;

    public MyDiaryViewModel(Application application) {
        super(application);
        this.repo = new PetsRepository(application);
        mPets = repo.getDatabaseDataPet();
        mPosts = repo.getDatabaseDataAllPost();
    }

    public LiveData<List<Pet>> getPets() {
        return mPets;
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }

    public LiveData<List<Post>> getPostByPetUUID(Pet pet) {
        return repo.getDatabaseDataPost(pet.getUUID());
    }

    public void deletePost(String uuid) {
        repo.deletePost(uuid);
    }

    public int getPosition(List<Post> data, String petUUID, String uuid) {
        List<Post> posts = data.stream().filter(new Predicate<Post>() {
            @Override
            public boolean test(Post post) {
                return post.getPetUUID().equals(petUUID);
            }
        }).collect(Collectors.toList());
        return posts.indexOf(new Post(uuid));
    }

    public int getPetPosition(List<Pet> pets, String petUUID) {
        Pet pet;
        pet = pets.stream().filter(new Predicate<Pet>() {
            @Override
            public boolean test(Pet pet) {
                return pet.getUUID().equals(petUUID);
            }
        }).findAny().get();
        return mPets.getValue().indexOf(pet);
    }
}
