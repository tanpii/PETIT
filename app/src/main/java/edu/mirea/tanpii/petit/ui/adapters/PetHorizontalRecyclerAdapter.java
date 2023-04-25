package edu.mirea.tanpii.petit.ui.adapters;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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

    public interface OnNewPetClickListener{
        void onNewPetClick(View view);
    }

    OnPetClickListener onPetClickListener;
    OnNewPetClickListener onNewPetClickListener;
    List<Pet> data;

    public PetHorizontalRecyclerAdapter(OnPetClickListener onPetClickListener, OnNewPetClickListener onNewPetClickListener) {
        this.data = new ArrayList<>();
        this.onPetClickListener = onPetClickListener;
        this.onNewPetClickListener = onNewPetClickListener;
    }

    public PetHorizontalRecyclerAdapter(List<Pet> data) {
        this.data = data;
    }

    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return 0;
        }
        else if (position == data.size()+1)
        {
            return 2;
        }
        else
        {
            return 1;
        }
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
                if (!data.get(position-1).getPhotoURL().equals(""))
                    try {
                        Picasso.get()
                                .load(Uri.parse(data.get(position-1).getPhotoURL()))
                                .error(R.drawable.pets_icon)
                                .into(holder.binding.petPicture);
                    } catch (SecurityException e) {
                    }
                holder.binding.petName.setText(data.get(position-1).getName());
                holder.binding.petPicture.setOnClickListener(v -> {
                    onPetClickListener.onPetClick(data.get(position-1));
                });
                break;
            case 2:
                holder.binding.petPicture.setImageResource(R.drawable.new_pet);
                holder.binding.petPicture.setOnClickListener(v -> {
                    onNewPetClickListener.onNewPetClick(v);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 2;
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
