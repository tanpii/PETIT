package edu.mirea.tanpii.petit.ui.view.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Post;

public abstract class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {
    public abstract void updateData(List<Post> newData);

    abstract class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
        }
        public abstract void toDo(int position);
    }
}
