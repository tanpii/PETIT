package edu.mirea.tanpii.petit.ui.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseUser;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentRegistrationBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.RegistrationViewModel;

public class RegistrationFragment extends Fragment {
    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    FragmentRegistrationBinding mBinding;
    RegistrationViewModel mViewModel;

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
        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnRegistration.setOnClickListener(v -> {
            String email = mBinding.editEmail.getText().toString();
            String password = mBinding.editPass.getText().toString();
            if (email.length() > 0 && password.length() > 5) {
                mViewModel.register(email, password);
                SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putString("user_email", email);
                edt.commit();
            } else {
                Toast.makeText(getContext(), "Заполните все поля. Пароль не менее 6 символов.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.btnAlreadyAcc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });
    }
}
