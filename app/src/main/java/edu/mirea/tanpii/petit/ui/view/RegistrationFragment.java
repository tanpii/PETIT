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
import edu.mirea.tanpii.petit.databinding.FragmentRegistrationBinding;

public class RegistrationFragment extends Fragment {
    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    FragmentRegistrationBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnRegistration.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
        });

        mBinding.btnAlreadyAcc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });
    }
}
