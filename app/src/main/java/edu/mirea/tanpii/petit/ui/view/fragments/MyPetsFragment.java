package edu.mirea.tanpii.petit.ui.view.fragments;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.databinding.FragmentMypetsBinding;
import edu.mirea.tanpii.petit.ui.view.adapters.AllPetRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.PetAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.PetHorizontalRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.view.adapters.PetRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.viewmodel.MyPetsViewModel;

public class MyPetsFragment extends Fragment {

    public final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;

    private MyPetsViewModel mViewModel;
    FragmentMypetsBinding mBinding;

    PetHorizontalRecyclerAdapter.OnPetClickListener onPetClickListener = new PetHorizontalRecyclerAdapter.OnPetClickListener() {

        @Override
        public void onPetClick(Pet pet) {
            if (pet == null) {
                mBinding.recyclerViewInfoPet.setAdapter(new AllPetRecyclerAdapter(onTodoClickListener, mViewModel.getPets().getValue()));
                mViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                    ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(todos);
                    if (todos.size()==0) {
                        mBinding.imageHint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.going_dog, null));
                        mBinding.titleHint.setText(R.string.hint_add_todo);
                    }
                    else {
                        mBinding.imageHint.setImageDrawable(null);
                        mBinding.titleHint.setText("");
                    }
                });
                mViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
                    ((AllPetRecyclerAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updatePets(pets);
                });
            }
            else {
                mBinding.recyclerViewInfoPet.setAdapter(new PetRecyclerAdapter(pet, onNewTodoClickListener, onDeletePetClickListener, onEditPetClickListener, onTodoClickListener));
                mViewModel.getTodoByPetUUID(pet).observe(getViewLifecycleOwner(), (value) -> {
                    ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(value);
                    mBinding.imageHint.setImageDrawable(null);
                    mBinding.titleHint.setText("");
                });
            }
        }
    };

    PetRecyclerAdapter.OnNewTodoClickListener onNewTodoClickListener = new PetRecyclerAdapter.OnNewTodoClickListener() {
        @Override
        public void onNewTodoClick(View view, String petUUID) {
            Bundle bundle = new Bundle();
            bundle.putString("petUUID", petUUID);
            Navigation.findNavController(view).navigate(R.id.action_global_newTodoFragment, bundle);
        }
    };

    PetRecyclerAdapter.OnDeletePetClickListener onDeletePetClickListener = new PetRecyclerAdapter.OnDeletePetClickListener() {
        @Override
        public void onDeletePetClick(String petUUID) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Вы действительно хотите удалить питомца?");

            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deletePet(petUUID);
                    mBinding.recyclerViewInfoPet.setAdapter(new AllPetRecyclerAdapter(onTodoClickListener, mViewModel.getPets().getValue()));
                    mViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                        ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(todos);
                        if (todos.size()==0) {
                            mBinding.imageHint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.going_dog, null));
                            mBinding.titleHint.setText(R.string.hint_add_todo);
                        }
                        else {
                            mBinding.imageHint.setImageDrawable(null);
                            mBinding.titleHint.setText("");
                        }
                    });
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


            mBinding.recyclerViewPets.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.recyclerViewInfoPet.setLayoutManager(new LinearLayoutManager(getContext()));

            mBinding.recyclerViewInfoPet.setAdapter(new AllPetRecyclerAdapter(onTodoClickListener, mViewModel.getPets().getValue()));
            mViewModel.getTodos().observe(getViewLifecycleOwner(), todos -> {
                ((PetAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updateData(todos);
                if (todos.size()==0) {
                    mBinding.imageHint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.going_dog, null));
                    mBinding.titleHint.setText(R.string.hint_add_todo);
                }
                else {
                    mBinding.imageHint.setImageDrawable(null);
                    mBinding.titleHint.setText("");
                }
            });

            mViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
                ((AllPetRecyclerAdapter) mBinding.recyclerViewInfoPet.getAdapter()).updatePets(pets);
            });

            PetHorizontalRecyclerAdapter petHorizontalRecyclerAdapter = new PetHorizontalRecyclerAdapter(onPetClickListener);

            mBinding.recyclerViewPets.setAdapter(petHorizontalRecyclerAdapter);
            mViewModel.getPets().observe(getViewLifecycleOwner(), (value) -> {
                ((PetHorizontalRecyclerAdapter) mBinding.recyclerViewPets.getAdapter()).updateData(value);
            });
        mBinding.btnDiary.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myDiaryFragment);
        });
        mBinding.btnProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myProfileFragment);
        });
        mBinding.btnHealth.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_healthFragment);
        });
    }
}