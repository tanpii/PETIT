package edu.mirea.tanpii.petit.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentEntryBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.RegistrationViewModel;

public class EntryFragment extends Fragment {
    RegistrationViewModel mViewModel;
    FragmentEntryBinding mBinding;

    public static EntryFragment newInstance() {
        return new EntryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        mViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_global_myProfileFragment);;
                }
            }
        });
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
