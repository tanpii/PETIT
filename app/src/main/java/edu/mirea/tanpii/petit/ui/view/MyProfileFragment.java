package edu.mirea.tanpii.petit.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentMyprofileBinding;

public class MyProfileFragment extends Fragment {
    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    FragmentMyprofileBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMyprofileBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnDiary.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myDiaryFragment);
        });
        mBinding.btnPets.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
        });
    }
}
