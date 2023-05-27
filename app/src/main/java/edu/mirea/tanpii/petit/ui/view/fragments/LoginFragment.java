package edu.mirea.tanpii.petit.ui.view.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentEntryBinding;
import edu.mirea.tanpii.petit.databinding.FragmentLoginBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.LoginViewModel;
import edu.mirea.tanpii.petit.ui.viewmodel.RegistrationViewModel;

public class LoginFragment extends Fragment {
    private LoginViewModel mViewModel;
    FragmentLoginBinding mBinding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_global_myProfileFragment);;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnLogin.setOnClickListener(v -> {
            String email = mBinding.editEmail.getText().toString();
            String password = mBinding.editPass.getText().toString();
            if (email.length() > 0 && password.length() > 0) {
                SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putString("user_email", email);
                edt.commit();
                mViewModel.login(email, password);
            } else {
                Toast.makeText(getContext(), "Заполните все поля.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.btnDontHaveAcc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.registrationFragment);
        });
    }
}