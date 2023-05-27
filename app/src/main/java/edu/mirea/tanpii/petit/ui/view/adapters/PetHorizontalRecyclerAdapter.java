package edu.mirea.tanpii.petit.ui.view.adapters;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.databinding.PetItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PetHorizontalRecyclerAdapter extends RecyclerView.Adapter<PetHorizontalRecyclerAdapter.PetViewHolder>{

    public interface OnPetClickListener{
        void onPetClick(Pet pet);
    }

    OnPetClickListener onPetClickListener;
    List<Pet> data;

    public PetHorizontalRecyclerAdapter(OnPetClickListener onPetClickListener) {
        this.data = new ArrayList<>();
        this.onPetClickListener = onPetClickListener;
    }

    public PetHorizontalRecyclerAdapter(List<Pet> data) {
        this.data = data;
    }

    public int getItemViewType(int position)
    {
        if (position == 0)
            return 0;
        else
            return 1;
    }


    public void updateData(List<Pet> newData) {
        data = newData;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PetItemBinding mBinding = PetItemBinding.inflate(LayoutInflater.from(parent.getContext()));

        return new PetViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        switch (this.getItemViewType(position))
        {
            case 0:
                holder.binding.petPicture.setImageResource(R.drawable.all_pet);
                holder.binding.petPicture.setOnClickListener(v -> {
                    onPetClickListener.onPetClick(null);
                });
                break;
            case 1:
                if (!data.get(position-1).getPhotoURL().equals("")) {
                    Picasso.get()
                            .load(Uri.parse(data.get(position-1).getPhotoURL()))
                            .error(R.drawable.pets_icon)
                            .into(holder.binding.petPicture);
                    holder.binding.firstLetter.setText("");
                }
                else
                    holder.binding.firstLetter.setText(data.get(position-1).getName());
                holder.binding.petName.setText(data.get(position-1).getName());
                holder.binding.petPicture.setOnClickListener(v -> {
                    onPetClickListener.onPetClick(data.get(position-1));
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class PetViewHolder extends RecyclerView.ViewHolder {
        public PetItemBinding binding;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = PetItemBinding.bind(itemView);
        }
    }

    public List<Pet> getData() {
        return data;
    }
}
