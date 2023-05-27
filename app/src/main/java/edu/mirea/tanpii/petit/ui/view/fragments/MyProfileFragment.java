package edu.mirea.tanpii.petit.ui.view.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentMyprofileBinding;
import edu.mirea.tanpii.petit.ui.view.adapters.MyPetsRecyclerAdapter;
import edu.mirea.tanpii.petit.ui.viewmodel.MyProfileViewModel;

public class MyProfileFragment extends Fragment {

    MyProfileViewModel mViewModel;
    FragmentMyprofileBinding mBinding;

    MyPetsRecyclerAdapter.OnDeletePetClickListener onDeletePetClickListener = new MyPetsRecyclerAdapter.OnDeletePetClickListener() {
        @Override
        public void onDeletePetClick(String petUUID) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Вы действительно хотите удалить питомца?");

            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mViewModel.deletePet(petUUID);
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

    MyPetsRecyclerAdapter.OnEditPetClickListener onEditPetClickListener = new  MyPetsRecyclerAdapter.OnEditPetClickListener() {
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

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMyprofileBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);

        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String userName = pref.getString("user_name", "");
        String userEmail = pref.getString("user_email", "");
        String userPhoto = pref.getString("user_photo", "");

        if (!userPhoto.equals("")) {
            Picasso.get()
                    .load(Uri.parse(userPhoto))
                    .into(mBinding.profileImage);
        }
        mBinding.textName.setText(userName);
        mBinding.textEmail.setText(userEmail);

        mBinding.recyclerViewMypets.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewMypets.setAdapter(new MyPetsRecyclerAdapter(onDeletePetClickListener, onEditPetClickListener));

        mViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
            ((MyPetsRecyclerAdapter) mBinding.recyclerViewMypets.getAdapter()).updateData(pets);
        });

        mBinding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.changeProfileFragment);
            }
        });

        mBinding.btnNewPet.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_global_newPetFragment);
        });

        mBinding.btnDiary.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myDiaryFragment);
        });
        mBinding.btnPets.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
        });
        mBinding.btnHealth.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_healthFragment);
        });
    }
}
