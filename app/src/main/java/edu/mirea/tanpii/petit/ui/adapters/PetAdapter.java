package edu.mirea.tanpii.petit.ui.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.mirea.tanpii.petit.data.models.Todo;

public abstract class PetAdapter extends RecyclerView.Adapter<PetAdapter.MyHolder> {
    public interface OnTodoClickListener{
        void onTodoClick(String uuid);
    }

    public abstract void updateData(List<Todo> newData);

    abstract class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
        }
        public abstract void toDo(int position);
    }
}
