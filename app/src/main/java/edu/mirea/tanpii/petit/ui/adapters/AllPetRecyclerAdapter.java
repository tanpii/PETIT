package edu.mirea.tanpii.petit.ui.adapters;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Todo;
import edu.mirea.tanpii.petit.databinding.PetInfoItemBinding;
import edu.mirea.tanpii.petit.databinding.PetTodoItemBinding;
import edu.mirea.tanpii.petit.databinding.TodoItemBinding;
import edu.mirea.tanpii.petit.ui.viewmodel.MyPetsViewModel;

public class AllPetRecyclerAdapter extends PetAdapter{

    private static final int CALENDAR = 0;
    private static final int TODO = 1;

    OnTodoClickListener onTodoClickListener;
    List<Todo> data;

    public AllPetRecyclerAdapter(List<Todo> data) {
        this.data = data;
    }

    public AllPetRecyclerAdapter(OnTodoClickListener onTodoClickListener) {
        this.onTodoClickListener = onTodoClickListener;
        data = new ArrayList<>();
    }

    public int getItemViewType(int position)
    {
        switch (position) {
//            case 0:
//                return PET_INFO;
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
            case CALENDAR:
                break;
            case TODO:
                PetTodoItemBinding TodoBinding = PetTodoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new AllPetRecyclerAdapter.TodoHolder(TodoBinding.getRoot());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.toDo(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class TodoHolder extends AllPetRecyclerAdapter.MyHolder {
        public PetTodoItemBinding binding;

        public TodoHolder(View itemView) {
            super(itemView);
            binding = PetTodoItemBinding.bind(itemView);
        }

        @Override
        public void toDo(int position) {
            Date date = new Date(data.get(position).getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            SimpleDateFormat stf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            binding.dateTodo.setText(sdf.format(date));
            binding.timeTodo.setText(stf.format(date));
            binding.textTodo.setText(data.get(position).getText());
            String imageURL = data.get(position).getPetImageURL();
            if (!imageURL.equals("")) {
                Picasso.get()
                        .load(Uri.parse(imageURL))
                        .error(R.drawable.pets_icon)
                        .into(binding.petImage);
            }
            switch (data.get(position).getIcon()) {
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
                onTodoClickListener.onTodoClick(data.get(position).getUuid());
            });
        }
    }
}
