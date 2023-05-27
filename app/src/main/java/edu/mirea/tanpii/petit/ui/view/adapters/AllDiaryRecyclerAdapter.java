package edu.mirea.tanpii.petit.ui.view.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.databinding.PostItemBinding;

public class AllDiaryRecyclerAdapter extends PostAdapter {

    List<Post> data;

    public interface OnGoToPostClickListener{
        void onGoToPostClick(List<Post> data, String petUUID, String uuid);
    }

    OnGoToPostClickListener onGoToPostClickListener;

    public AllDiaryRecyclerAdapter(List<Post> data) {
        this.data = data;
    }

    public AllDiaryRecyclerAdapter(OnGoToPostClickListener onGoToPostClickListener) {
        this.onGoToPostClickListener = onGoToPostClickListener;
        data = new ArrayList<>();
    }

    public void updateData(List<Post> newData) {
        data = newData;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemBinding binding = PostItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new AllDiaryRecyclerAdapter.PostHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.toDo(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PostHolder extends MyHolder{
        public PostItemBinding binding;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostItemBinding.bind(itemView);
        }

        @Override
        public void toDo(int position) {
            String mediaURL = data.get(position).getMediaURL();
            if (!mediaURL.equals(""))
                Picasso.get()
                        .load(Uri.parse(mediaURL))
                        .resize(130, 130)
                        .centerCrop()
                        .error(R.drawable.pets_icon)
                        .into(binding.media);
            else
                binding.textView.setText(data.get(position).getText());
            binding.media.setOnClickListener(v -> {
                onGoToPostClickListener.onGoToPostClick(data, data.get(position).getPetUUID(), data.get(position).getUuid());
            });
        }
    }
}
