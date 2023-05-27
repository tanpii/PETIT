package edu.mirea.tanpii.petit.ui.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentProfilechangeBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.ChangeProfileViewModel;
import edu.mirea.tanpii.petit.ui.viewmodel.MyProfileViewModel;

public class ChangeProfileFragment extends Fragment {
    int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private final int PICK_IMAGE_REQUEST = 22;
    String photoURL = "";
    SharedPreferences pref;

    OnBackPressedCallback callback;

    public static ChangeProfileFragment newInstance() {
        return new ChangeProfileFragment();
    }

    ChangeProfileViewModel mViewModel;
    FragmentProfilechangeBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentProfilechangeBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ChangeProfileViewModel.class);

        mViewModel.getLoggedOutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut) {
                    Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigate(R.id.navigation2);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String userName = pref.getString("user_name", "");
        photoURL = pref.getString("user_photo", "");

        if (!photoURL.equals("")) {
            Picasso.get()
                    .load(Uri.parse(photoURL))
                    .into(mBinding.profileImage);
        }
        mBinding.editName.setText(userName);

        callback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_global_newPetFragment);
            }
        };

        mBinding.btnChangeAcc.setOnClickListener(v -> {
            String name = mBinding.editName.getText().toString();
            SharedPreferences.Editor edt = pref.edit();
            edt.putString("user_name", name);
            edt.putString("user_photo", photoURL);
            edt.commit();
            Navigation.findNavController(v).navigate(R.id.action_global_myProfileFragment);
        });
        mBinding.btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor edt = pref.edit();
            edt.putString("user_name", "");
            edt.putString("user_photo", "");
            edt.commit();
            mViewModel.logOut();
        });
        mBinding.profileImage.setOnClickListener(v -> {
            int permissionStatus = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE);
            }
            else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callback.setEnabled(true);
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        getActivity();
        Uri uri = null;
        if (data!=null)
            uri = data.getData();
        if (uri!=null)
            photoURL = uri.toString();
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null) {
            try {
                Picasso.get()
                        .load(uri)
                        .into(mBinding.profileImage);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Ошибка загрузки фотографии", Toast.LENGTH_LONG).show();
            }
        }
        callback.setEnabled(false);
    }
}
