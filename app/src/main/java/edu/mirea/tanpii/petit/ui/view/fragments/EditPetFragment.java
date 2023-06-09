package edu.mirea.tanpii.petit.ui.view.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.databinding.FragmentEditpetBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.EditPetViewModel;

public class EditPetFragment extends Fragment {
    private final int PICK_IMAGE_REQUEST = 22;

    public static NewPetFragment newInstance() {
        return new NewPetFragment();
    }

    String uuid, name, birthday, photoURL = "";
    FragmentEditpetBinding mBinding;
    EditPetViewModel mViewModel;
    Calendar date = Calendar.getInstance();
    Calendar minDate = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentEditpetBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(EditPetViewModel.class);

        if (getArguments() != null) {
            uuid = getArguments().getString("petUUID");
            mBinding.editName.setText(getArguments().getString("petName"));
            mBinding.editBirthday.setText(getArguments().getString("petBirthday"));
            photoURL = getArguments().getString("photoURL");
            if (!photoURL.equals(""))
                Picasso.get()
                        .load(Uri.parse(photoURL))
                        .error(R.drawable.pets_icon)
                        .into(mBinding.petImage);
        }

        mBinding.petImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
        });

        mBinding.editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), d,
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(minDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mBinding.btnEditPet.setOnClickListener(v -> {
            name = mBinding.editName.getText().toString();
            birthday = mBinding.editBirthday.getText().toString();
            if (!name.equals("") && !birthday.equals("")) {
                mViewModel.editPet(uuid, name, birthday, photoURL);
                Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
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
            photoURL = uri.toString();
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null) {
            try {
                Picasso.get()
                        .load(uri)
                        .error(R.drawable.pets_icon)
                        .into(mBinding.petImage);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Ошибка загрузки фотографии", Toast.LENGTH_LONG).show();
            }
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date formatDate = date.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            birthday = sdf.format(formatDate);
            mBinding.editBirthday.setText(birthday);
        }
    };
}
