package edu.mirea.tanpii.petit.ui.view.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.databinding.MypetItemBinding;

public class MyPetsRecyclerAdapter extends RecyclerView.Adapter<MyPetsRecyclerAdapter.PetInfoHolder>{

    List<Pet> data;

    MyPetsRecyclerAdapter.OnDeletePetClickListener onDeletePetClickListener;
    MyPetsRecyclerAdapter.OnEditPetClickListener onEditPetClickListener;

    public interface OnDeletePetClickListener{
        void onDeletePetClick(String petUUID);
    }

    public interface OnEditPetClickListener{
        void onEditPetClick(View view, String petUUID, String petName, String petBirthday, String photoURL);
    }

    public MyPetsRecyclerAdapter(MyPetsRecyclerAdapter.OnDeletePetClickListener onDeletePetClickListener, MyPetsRecyclerAdapter.OnEditPetClickListener onEditPetClickListener) {
        this.data = new ArrayList<>();
        this.onDeletePetClickListener = onDeletePetClickListener;
        this.onEditPetClickListener = onEditPetClickListener;
    }

    public MyPetsRecyclerAdapter(List<Pet> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyPetsRecyclerAdapter.PetInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MypetItemBinding mBinding = MypetItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MyPetsRecyclerAdapter.PetInfoHolder(mBinding.getRoot());
    }

    public void updateData(List<Pet> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyPetsRecyclerAdapter.PetInfoHolder holder, int position) {
        holder.toDo(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PetInfoHolder extends RecyclerView.ViewHolder {
        public MypetItemBinding binding;

        public PetInfoHolder(View itemView) {
            super(itemView);

            binding = MypetItemBinding.bind(itemView);
        }

        public void toDo(int position) {
            if (!data.get(position).getPhotoURL().equals("")) {
                Picasso.get()
                        .load(Uri.parse(data.get(position).getPhotoURL()))
                        .error(R.drawable.pets_icon)
                        .into(binding.petPicture);
                binding.firstLetter.setText("");
            }
            else
                binding.firstLetter.setText(data.get(position).getName());
            binding.petName.setText(data.get(position).getName());
            binding.petBirthday.setText(data.get(position).getBirthday());
            binding.btnDeletePet.setOnClickListener(v -> {
                onDeletePetClickListener.onDeletePetClick(data.get(position).getUUID());
            });
            binding.btnEditPet.setOnClickListener(v -> {
                onEditPetClickListener.onEditPetClick(v, data.get(position).getUUID(), data.get(position).getName(), data.get(position).getBirthday(), data.get(position).getPhotoURL());
            });
        }
    }
}
