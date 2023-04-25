package edu.mirea.tanpii.petit.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.databinding.FragmentMydiaryBinding;
import edu.mirea.tanpii.petit.ui.adapters.AllDiaryRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.adapters.DiaryRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.adapters.PetHorizontalRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.adapters.PostAdapter;
import edu.mirea.tanpii.petit.ui.viewmodel.MyDiaryViewModel;

public class MyDiaryFragment extends Fragment {
    int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private MyDiaryViewModel mViewModel;
    FragmentMydiaryBinding mBinding;

    ArrayList<Pet> data = new ArrayList<>();
    PetHorizontalRecyclerAdapter petHorizontalRecyclerAdapter;

    DiaryRecyclerAdapter.OnDeletePostClickListener onDeletePostClickListener = new DiaryRecyclerAdapter.OnDeletePostClickListener() {
        @Override
        public void onDeletePostClick(String uuid) {
            mViewModel.deletePost(uuid);
        }
    };

    DiaryRecyclerAdapter.OnNewPostClickListener onNewPostClickListener = new DiaryRecyclerAdapter.OnNewPostClickListener() {
        @Override
        public void onNewPostClick(View view, String petUUID) {
            Bundle bundle = new Bundle();
            bundle.putString("petUUID", petUUID);
            Navigation.findNavController(view).navigate(R.id.action_global_newPostFragment, bundle);
        }
    };

    DiaryRecyclerAdapter.OnEditPostClickListener onEditPostClickListener = new DiaryRecyclerAdapter.OnEditPostClickListener() {
        @Override
        public void onEditPostClick(View view, String uuid, String text, String mediaURL) {
            Bundle bundle = new Bundle();
            bundle.putString("uuid", uuid);
            bundle.putString("text", text);
            bundle.putString("mediaURL", mediaURL);
            Navigation.findNavController(view).navigate(R.id.action_global_editPostFragment, bundle);
        }
    };

    AllDiaryRecyclerAdapter.OnGoToPostClickListener onGoToPostClickListener = new AllDiaryRecyclerAdapter.OnGoToPostClickListener() {
        @Override
        public void onGoToPostClick(List<Post> data, String petUUID, String uuid) {
            List<Pet> pets = petHorizontalRecyclerAdapter.getData();
            int position = mViewModel.getPetPosition(pets, petUUID);
            mBinding.recyclerViewPets.smoothScrollToPosition(position+1);
            onPetClickListener.onPetClick(pets.get(position));
            mBinding.recyclerViewDiary.smoothScrollToPosition(mViewModel.getPosition(data, petUUID, uuid)+1);
        }
    };

    PetHorizontalRecyclerAdapter.OnPetClickListener onPetClickListener = new PetHorizontalRecyclerAdapter.OnPetClickListener() {
        @Override
        public void onPetClick(Pet pet) {
            if (pet == null) {
                mBinding.recyclerViewDiary.setLayoutManager(new GridLayoutManager(getContext(), 3));
                mBinding.recyclerViewDiary.setAdapter(new AllDiaryRecyclerAdapter(onGoToPostClickListener));
                mViewModel.getPosts().observe(getViewLifecycleOwner(), (Observer<List<Post>>) posts -> {
                    ((PostAdapter) mBinding.recyclerViewDiary.getAdapter()).updateData(posts);
                });
            }
            else {
                mBinding.recyclerViewDiary.setLayoutManager(new LinearLayoutManager(getContext()));
                mBinding.recyclerViewDiary.setAdapter(new DiaryRecyclerAdapter(pet, onDeletePostClickListener, onNewPostClickListener, onEditPostClickListener));
                mViewModel.getPostByPetUUID(pet).observe(getViewLifecycleOwner(), (value) -> {
                    ((PostAdapter) mBinding.recyclerViewDiary.getAdapter()).updateData(value);
                });
            }
        }
    };

    PetHorizontalRecyclerAdapter.OnNewPetClickListener onNewPetClickListener = new PetHorizontalRecyclerAdapter.OnNewPetClickListener() {
        @Override
        public void onNewPetClick(View view) {
            Navigation.findNavController(view).navigate(R.id.action_global_newPetFragment);
        }
    };

    public static MyDiaryFragment newInstance() {
        return new MyDiaryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMydiaryBinding .inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MyDiaryViewModel.class);

        int permissionStatus = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
        }
        else {
            mBinding.recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.recyclerViewDiary.setLayoutManager(new GridLayoutManager(getContext(), 3));

            mBinding.recyclerViewDiary.setAdapter(new AllDiaryRecyclerAdapter(onGoToPostClickListener));
            mViewModel.getPosts().observe(getViewLifecycleOwner(), (Observer<List<Post>>) posts -> {
                ((PostAdapter) mBinding.recyclerViewDiary.getAdapter()).updateData(posts);
            });

            petHorizontalRecyclerAdapter = new PetHorizontalRecyclerAdapter(onPetClickListener, onNewPetClickListener);

            mBinding.recyclerViewPets.setAdapter(petHorizontalRecyclerAdapter);
            mViewModel.getPets().observe(getViewLifecycleOwner(), (value) -> {
                ((PetHorizontalRecyclerAdapter) mBinding.recyclerViewPets.getAdapter()).updateData(value);
            });
        }
        mBinding.btnProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myProfileFragment);
        });
        mBinding.btnPets.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
        });
    }
}
