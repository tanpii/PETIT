package edu.mirea.tanpii.petit.ui.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentEditpostBinding;
import edu.mirea.tanpii.petit.databinding.FragmentNewpostBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.EditPostViewModel;

public class EditPostFragment extends Fragment {

    private final int PICK_IMAGE_REQUEST = 22;

    public static EditPostFragment newInstance() {
        return new EditPostFragment();
    }

    String uuid, petUUID, date, text, mediaURL = "";
    FragmentEditpostBinding mBinding;
    EditPostViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentEditpostBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(EditPostViewModel.class);

        if (getArguments() != null) {
            uuid = getArguments().getString("uuid");
            mBinding.editText.setText(getArguments().getString("text"));
            mediaURL = getArguments().getString("mediaURL");
            if (!mediaURL.equals(""))
                Picasso.get()
                        .load(Uri.parse(mediaURL))
                        .error(R.drawable.pets_icon)
                        .into(mBinding.noteImage);
        }

        mBinding.noteImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
        });

        mBinding.btnEditPost.setOnClickListener(v -> {
            text = mBinding.editText.getText().toString();
            if (!text.equals("") || !mediaURL.equals("")) {
                mViewModel.editPost(uuid, text, mediaURL);
                Navigation.findNavController(v).navigate(R.id.action_global_myDiaryFragment);
            }
            else
                Toast.makeText(v.getContext(), "Заполните поля", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        Uri uri = data.getData();
        if (uri!=null)
            mediaURL = uri.toString();
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null) {
            try {
                Picasso.get()
                        .load(uri)
                        .error(R.drawable.pets_icon)
                        .into(mBinding.noteImage);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Ошибка загрузки фотографии", Toast.LENGTH_LONG).show();
            }
        }
    }
}
