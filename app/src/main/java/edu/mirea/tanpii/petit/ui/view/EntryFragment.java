package edu.mirea.tanpii.petit.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentEntryBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.MyPetsViewModel;

public class EntryFragment extends Fragment {

    FragmentEntryBinding mBinding;

    public static EntryFragment newInstance() {
        return new EntryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentEntryBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_entryFragment_to_loginFragment);
        });

        mBinding.btnRegistration.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_entryFragment_to_registrationFragment);
        });

    }
}
