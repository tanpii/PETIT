package edu.mirea.tanpii.petit.ui.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Treatment;
import edu.mirea.tanpii.petit.databinding.DialogAddTreatmentBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.NewTreatmentViewModel;

public class NewTreatmentDialog extends DialogFragment implements View.OnClickListener {

    NewTreatmentViewModel mViewModel;
    DialogAddTreatmentBinding mBinding;
    int icon = -1;
    String title;
    String petUUID;
    String uuid;
    String note;
    long date;
    Calendar dateAndTime = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date formatDate = dateAndTime.getTime();
            mBinding.editDate.setText(sdf.format(formatDate));
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(NewTreatmentViewModel.class);
        mBinding = DialogAddTreatmentBinding.inflate(inflater);
        mBinding.btnNewTreatment.setOnClickListener(this);
        mBinding.editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker();
                datePickerDialog.show();
            }
        });
        return mBinding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onClick(View v) {
        switch(mBinding.chooseIcon.getCheckedRadioButtonId()) {
            case (R.id.medicineIcon):
                icon = Treatment.medicine_icon;
                break;
            case (R.id.clinicIcon):
                icon = Treatment.clinic_icon;
                break;
            case (R.id.searchIcon):
                icon = Treatment.search_icon;
                break;
            case (R.id.tabletIcon):
                icon = Treatment.tablet_icon;
                break;
            default:
                break;
        }
        petUUID = "";
        date = dateAndTime.getTimeInMillis();
        title = mBinding.editText.getText().toString();
        note = mBinding.editNote.getText().toString();
        if (getArguments() != null)
            petUUID = getArguments().getString("petUUID");
        uuid = UUID.randomUUID().toString();

        if (!petUUID.equals("") & !uuid.equals("") & !mBinding.editDate.getText().toString().equals("") & !title.equals("") & icon!=-1) {
            mViewModel.addTreatment(uuid, petUUID, date, title, note, icon);
            dismiss();
        }
        else if (icon == -1){
            Toast.makeText(getContext(), "Выберите иконку", Toast.LENGTH_LONG).show();
        } else Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
    }
}
