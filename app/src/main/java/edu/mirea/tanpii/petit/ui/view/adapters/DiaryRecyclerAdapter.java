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
import edu.mirea.tanpii.petit.data.models.Pet;
import edu.mirea.tanpii.petit.data.models.Post;
import edu.mirea.tanpii.petit.databinding.DiaryItemBinding;
import edu.mirea.tanpii.petit.databinding.HeaderDiaryItemBinding;

public class DiaryRecyclerAdapter extends PostAdapter {

    private static final int HEADER = 0;
    private static final int POST = 1;

    String petUUID;
    String petName;

    List<Post> data;

    public interface OnDeletePostClickListener{
        void onDeletePostClick(String uuid);
    }

    public interface OnNewPostClickListener{
        void onNewPostClick(View view, String petUUID);
    }

    public interface OnEditPostClickListener{
        void onEditPostClick(View view, String uuid, String text, String mediaURL);
    }

    OnDeletePostClickListener onDeletePostClickListener;
    OnNewPostClickListener onNewPostClickListener;
    OnEditPostClickListener onEditPostClickListener;

    public DiaryRecyclerAdapter(List<Post> data) {
        this.data = data;
    }

    public DiaryRecyclerAdapter(Pet pet, OnDeletePostClickListener onDeletePostClickListener, OnNewPostClickListener onNewPostClickListener, OnEditPostClickListener onEditPostClickListener) {
        if (pet != null) {
            petUUID = pet.getUUID();
            petName = pet.getName();
            this.data = new ArrayList<>();
        }
        this.onDeletePostClickListener = onDeletePostClickListener;
        this.onEditPostClickListener = onEditPostClickListener;
        this.onNewPostClickListener = onNewPostClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return HEADER;
            default:
                return POST;
        }
    }

    public void updateData(List<Post> newData) {
        data = newData;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                HeaderDiaryItemBinding HeaderBinding = HeaderDiaryItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new DiaryRecyclerAdapter.HeaderHolder(HeaderBinding.getRoot());
            case POST:
                DiaryItemBinding DiaryBinding = DiaryItemBinding.inflate(LayoutInflater.from(parent.getContext()));
                return new DiaryRecyclerAdapter.DiaryHolder(DiaryBinding.getRoot());
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


    class HeaderHolder extends MyHolder {
        public HeaderDiaryItemBinding binding;

        public HeaderHolder(View itemView) {
            super(itemView);

            binding = HeaderDiaryItemBinding.bind(itemView);
        }

        @Override
        public void toDo(int position) {
            binding.petName.setText(" " + petName);
            binding.btnNewPost.setOnClickListener(v -> {
                onNewPostClickListener.onNewPostClick(v, petUUID);
            });
        }
    }

    class DiaryHolder extends MyHolder {
        public DiaryItemBinding binding;

        public DiaryHolder(View itemView) {
            super(itemView);
            binding = DiaryItemBinding.bind(itemView);
        }

        @Override
        public void toDo(int position) {
            String mediaURL = data.get(position-1).getMediaURL();
            if (!mediaURL.equals("")) {
                Picasso.get()
                        .load(Uri.parse(mediaURL))
                        .centerCrop()
                        .resize(380, 380)
                        .error(R.drawable.pets_icon)
                        .into(binding.media);
                binding.media.setClipToOutline(true);
            }
            else {
                binding.media.getLayoutParams().height = 0;
                binding.media.requestLayout();
            }
            binding.textDate.setText(data.get(position-1).getDate());
            binding.text.setText(data.get(position-1).getText());
            binding.btnEditPost.setOnClickListener(v -> {
                onEditPostClickListener.onEditPostClick(v, data.get(position-1).getUuid(), data.get(position-1).getText(), data.get(position-1).getMediaURL());
            });
            binding.btnDeletePost.setOnClickListener(v -> {
                onDeletePostClickListener.onDeletePostClick(data.get(position-1).getUuid());
            });
        }
    }
}
