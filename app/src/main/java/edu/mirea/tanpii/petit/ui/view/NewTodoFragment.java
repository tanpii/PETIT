package edu.mirea.tanpii.petit.ui.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.databinding.FragmentNewtodoBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.NewTodoViewModel;

public class NewTodoFragment extends Fragment {
    FragmentNewtodoBinding mBinding;
    NewTodoViewModel mViewModel;
    int icon = -1;
    String uuid, petUUID = "", petImageURL = "", text;
    long date;
    Calendar dateAndTime = Calendar.getInstance();
    Calendar minDateAndTime = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    SimpleDateFormat stf = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentNewtodoBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(NewTodoViewModel.class);

        mBinding.editDate.setText(sdf.format(dateAndTime.getTime()));
        mBinding.editTime.setText(stf.format(dateAndTime.getTime()));

        mBinding.editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDateAndTime.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mBinding.editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        mBinding.btnNewTodo.setOnClickListener(v -> {
            switch(mBinding.chooseIcon.getCheckedRadioButtonId()) {
                case R.id.radioStandard:
                    icon = Todo.standard_icon;
                    break;
                case R.id.radioWalk:
                    icon = Todo.walk_icon;
                    break;
                case R.id.radioEat:
                    icon = Todo.eat_icon;
                    break;
                case R.id.radioPlay:
                    icon = Todo.play_icon;
                    break;
                case R.id.radioMedicine:
                    icon = Todo.medicine_icon;
                    break;
                case R.id.radioCleaning:
                    icon = Todo.cleaning_icon;
                    break;
                case R.id.radioBirthday:
                    icon = Todo.birthday_icon;
                    break;
                case R.id.radioGrooming:
                    icon = Todo.grooming_icon;
                    break;
                default:
                    icon = -1;
            }
            if (getArguments() != null) {
                petUUID = getArguments().getString("petUUID");
                petImageURL = getArguments().getString("petImageURL");
            }
            uuid = UUID.randomUUID().toString();
            date = dateAndTime.getTimeInMillis();
            text = mBinding.editText.getText().toString();
            if (!petUUID.equals("") & !uuid.equals("") & !mBinding.editDate.getText().toString().equals("") & !text.equals("") & icon!=-1) {
                mViewModel.addTodo(uuid, petUUID, petImageURL, date, text, icon);
                Navigation.findNavController(v).navigate(R.id.action_global_myPetsFragment2);
            }
            else if (icon == -1){
                Toast.makeText(getContext(), "Выберите иконку", Toast.LENGTH_LONG).show();
            } else Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
        });
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date formatDate = dateAndTime.getTime();
            mBinding.editDate.setText(sdf.format(formatDate));
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            Date formatDate = dateAndTime.getTime();
            mBinding.editTime.setText(stf.format(formatDate));
        }
    };

}
