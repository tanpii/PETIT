package edu.mirea.tanpii.petit.ui.view;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.databinding.FragmentMypetsBinding;
import edu.mirea.tanpii.petit.ui.adapters.AllPetRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.adapters.PetAdapter;
import edu.mirea.tanpii.petit.ui.adapters.PetHorizontalRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.adapters.PetRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.viewmodel.MyPetsViewModel;

public class MyPetsFragment extends Fragment {

    public final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    private MyPetsViewModel mViewModel;
    FragmentMypetsBinding mBinding;

    PetHorizontalRecyclerAdapter.OnPetClickListener onPetClickListener = new PetHorizontalRecyclerAdapter.OnPetClickListener() {

        @Override
        public void onPetClick(Pet pet) {
            if (pet == null) {
                mBinding.recyclerViewInfoPet.setAdapter(new AllPetRecyclerAdapter(onTodoClickListener));
                mViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                    ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(todos);
                });
            }
            else {
                mBinding.recyclerViewInfoPet.setAdapter(new PetRecyclerAdapter(pet, onNewTodoClickListener, onDeletePetClickListener, onEditPetClickListener, onTodoClickListener));
                mViewModel.getTodoByPetUUID(pet).observe(getViewLifecycleOwner(), (value) -> {
                    ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(value);
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

    PetRecyclerAdapter.OnNewTodoClickListener onNewTodoClickListener = new PetRecyclerAdapter.OnNewTodoClickListener() {
        @Override
        public void onNewTodoClick(View view, String petUUID, String petImageURL) {
            Bundle bundle = new Bundle();
            bundle.putString("petUUID", petUUID);
            bundle.putString("petImageURL", petImageURL);
            Navigation.findNavController(view).navigate(R.id.action_global_newTodoFragment, bundle);
        }
    };

    PetRecyclerAdapter.OnDeletePetClickListener onDeletePetClickListener = new PetRecyclerAdapter.OnDeletePetClickListener() {
        @Override
        public void onDeletePetClick(String petUUID) {
            mViewModel.deletePet(petUUID);
            mBinding.recyclerViewInfoPet.setAdapter(new AllPetRecyclerAdapter(onTodoClickListener));
            mViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(todos);
            });
        }
    };

    PetAdapter.OnTodoClickListener onTodoClickListener = new PetAdapter.OnTodoClickListener() {
        @Override
        public void onTodoClick(String uuid) {
            mViewModel.deleteTodo(uuid);
        }
    };

    PetRecyclerAdapter.OnEditPetClickListener onEditPetClickListener = new PetRecyclerAdapter.OnEditPetClickListener() {
        @Override
        public void onEditPetClick(View view, String uuid, String name, String birthday, String photoURL) {
            Bundle bundle = new Bundle();
            bundle.putString("petUUID", uuid);
            bundle.putString("petName", name);
            bundle.putString("petBirthday", birthday);
            bundle.putString("photoURL", photoURL);
            Navigation.findNavController(view).navigate(R.id.action_global_editPetFragment, bundle);
        }
    };

    public static MyPetsFragment newInstance() {
        return new MyPetsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMypetsBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MyPetsViewModel.class);

        int permissionStatus = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
        }
        else {
            mBinding.recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.recyclerViewInfoPet.setLayoutManager(new LinearLayoutManager(getContext()));

            mBinding.recyclerViewInfoPet.setAdapter(new AllPetRecyclerAdapter(onTodoClickListener));
            mViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(todos);
            });

            PetHorizontalRecyclerAdapter petHorizontalRecyclerAdapter = new PetHorizontalRecyclerAdapter(onPetClickListener, onNewPetClickListener);

            mBinding.recyclerViewPets.setAdapter(petHorizontalRecyclerAdapter);
            mViewModel.getPets().observe(getViewLifecycleOwner(), (value) -> {
                ((PetHorizontalRecyclerAdapter) mBinding.recyclerViewPets.getAdapter()).updateData(value);
            });
        }
        mBinding.btnDiary.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myDiaryFragment);
        });
        mBinding.btnProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myProfileFragment);
        });
    }
}