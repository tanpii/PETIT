package edu.mirea.tanpii.petit.ui.adapters;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.databinding.PetInfoItemBinding;
import edu.mirea.tanpii.petit.databinding.TodoItemBinding;

public class PetRecyclerAdapter extends PetAdapter{

    private static final int PET_INFO = 0;
    private static final int TODO = 1;

    String petUUID;
    String petName;
    String petBirthday;
    String photoURL;

    List<Todo> data;

    OnNewTodoClickListener onNewTodoClickListener;
    OnDeletePetClickListener onDeletePetClickListener;
    OnEditPetClickListener onEditPetClickListener;
    OnTodoClickListener onTodoClickListener;

    public interface OnDeletePetClickListener{
        void onDeletePetClick(String petUUID);
    }

    public interface OnEditPetClickListener{
        void onEditPetClick(View view, String petUUID, String petName, String petBirthday, String photoURL);
    }

    public interface OnNewTodoClickListener{
        void onNewTodoClick(View view, String petUUID, String petImageURL);
    }

    public PetRecyclerAdapter(List<Todo> data) {
        this.data = data;
    }

    public PetRecyclerAdapter(Pet pet, OnNewTodoClickListener onNewTodoClickListener, OnDeletePetClickListener onDeletePetClickListener, OnEditPetClickListener onEditPetClickListener, OnTodoClickListener onTodoClickListener) {
        if (pet != null) {
            petUUID = pet.getUUID();
            petName = pet.getName();
            petBirthday = pet.getBirthday();
            photoURL = pet.getPhotoURL();
            data = new ArrayList<>();
        }
        this.onNewTodoClickListener = onNewTodoClickListener;
        this.onDeletePetClickListener = onDeletePetClickListener;
        this.onEditPetClickListener = onEditPetClickListener;
        this.onTodoClickListener = onTodoClickListener;
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (position) {
            case 0:
                return PET_INFO;
            default:
                return TODO;
        }
    }


    public void updateData(List<Todo> newData) {
        data = newData;

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
        case PET_INFO:
            PetInfoItemBinding PetInfoBinding = PetInfoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new PetInfoHolder(PetInfoBinding.getRoot());
        case TODO:
            TodoItemBinding TodoBinding = TodoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TodoHolder(TodoBinding.getRoot());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.toDo(position);
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    class PetInfoHolder extends MyHolder {
        public PetInfoItemBinding binding;

        public PetInfoHolder(View itemView) {
            super(itemView);

            binding = PetInfoItemBinding.bind(itemView);
        }

        @Override
        public void toDo(int position) {
            if (!photoURL.equals(""))
                Picasso.get()
                        .load(Uri.parse(photoURL))
                        .error(R.drawable.pets_icon)
                        .into(binding.petPicture);
            binding.petName.setText(petName);
            binding.petBirthday.setText(petBirthday);
            binding.btnNewTodo.setOnClickListener(v -> {
                onNewTodoClickListener.onNewTodoClick(v, petUUID, photoURL);
            });
            binding.btnDeletePet.setOnClickListener(v -> {
                onDeletePetClickListener.onDeletePetClick(petUUID);
            });
            binding.btnEditPet.setOnClickListener(v -> {
                onEditPetClickListener.onEditPetClick(v, petUUID, petName, petBirthday, photoURL);
            });
        }
    }

    class TodoHolder extends MyHolder {
        public TodoItemBinding binding;

        public TodoHolder(View itemView) {
            super(itemView);
            binding = TodoItemBinding.bind(itemView);
        }

        @Override
        public void toDo(int position) {
            Date date = new Date(data.get(position-1).getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            SimpleDateFormat stf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            binding.dateTodo.setText(sdf.format(date));
            binding.timeTodo.setText(stf.format(date));
            binding.textTodo.setText(data.get(position-1).getText());
            switch (data.get(position-1).getIcon()) {
                case Todo.standard_icon:
                    binding.iconTodo.setImageResource(R.drawable.standard_icon);
                    break;
                case Todo.walk_icon:
                    binding.iconTodo.setImageResource(R.drawable.walk_icon);
                    break;
                case Todo.play_icon:
                    binding.iconTodo.setImageResource(R.drawable.play_icon);
                    break;
                case Todo.eat_icon:
                    binding.iconTodo.setImageResource(R.drawable.eat_icon);
                    break;
                case Todo.cleaning_icon:
                    binding.iconTodo.setImageResource(R.drawable.cleaning_icon);
                    break;
                case Todo.grooming_icon:
                    binding.iconTodo.setImageResource(R.drawable.grooming_icon);
                    break;
                case Todo.birthday_icon:
                    binding.iconTodo.setImageResource(R.drawable.birthday_icon);
                    break;
                case Todo.medicine_icon:
                    binding.iconTodo.setImageResource(R.drawable.medicine_icon);
                    break;
                default:
                    break;
            }
            binding.btnTodo.setOnClickListener(v -> {
                onTodoClickListener.onTodoClick(data.get(position-1).getUuid());
            });
        }
    }
}
