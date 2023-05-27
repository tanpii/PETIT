package edu.mirea.tanpii.petit.ui.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
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
import edu.mirea.tanpii.petit.ui.view.adapters.AllDiaryRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.DiaryRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.PetHorizontalRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.PostAdapter;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Вы действительно хотите удалить воспоминание?");

            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deletePost(uuid);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
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
                    if (posts.size()==0) {
                        mBinding.imageHint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.playing_dog, null));
                        mBinding.titleHint.setText(R.string.hint_add_post);
                    }
                    else {
                        mBinding.imageHint.setImageDrawable(null);
                        mBinding.titleHint.setText("");
                        ((PostAdapter) mBinding.recyclerViewDiary.getAdapter()).updateData(posts);
                    }
                });
            }
            else {
                mBinding.recyclerViewDiary.setLayoutManager(new LinearLayoutManager(getContext()));
                mBinding.recyclerViewDiary.setAdapter(new DiaryRecyclerAdapter(pet, onDeletePostClickListener, onNewPostClickListener, onEditPostClickListener));
                mViewModel.getPostByPetUUID(pet).observe(getViewLifecycleOwner(), (value) -> {
                    mBinding.imageHint.setImageDrawable(null);
                    mBinding.titleHint.setText("");
                    ((PostAdapter) mBinding.recyclerViewDiary.getAdapter()).updateData(value);
                });
            }
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

            mBinding.recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.recyclerViewDiary.setLayoutManager(new GridLayoutManager(getContext(), 3));

            mBinding.recyclerViewDiary.setAdapter(new AllDiaryRecyclerAdapter(onGoToPostClickListener));
            mViewModel.getPosts().observe(getViewLifecycleOwner(), (Observer<List<Post>>) posts -> {
                ((PostAdapter) mBinding.recyclerViewDiary.getAdapter()).updateData(posts);
                if (posts.size()==0) {
                    mBinding.imageHint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.playing_dog, null));
                    mBinding.titleHint.setText(R.string.hint_add_post);
                }
                else {
                    mBinding.imageHint.setImageDrawable(null);
                    mBinding.titleHint.setText("");
                }
            });

            petHorizontalRecyclerAdapter = new PetHorizontalRecyclerAdapter(onPetClickListener);

            mBinding.recyclerViewPets.setAdapter(petHorizontalRecyclerAdapter);
            mViewModel.getPets().observe(getViewLifecycleOwner(), (value) -> {
                ((PetHorizontalRecyclerAdapter) mBinding.recyclerViewPets.getAdapter()).updateData(value);
            });
        mBinding.btnProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myProfileFragment);
        });
        mBinding.btnPets.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
        });
        mBinding.btnHealth.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_healthFragment);
        });
    }
}
