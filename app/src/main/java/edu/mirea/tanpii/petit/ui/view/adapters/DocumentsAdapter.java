package edu.mirea.tanpii.petit.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.mirea.tanpii.petit.data.models.Document;
import edu.mirea.tanpii.petit.databinding.DocumentItemBinding;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.DocumentHolder> {

    List<Document> data;

    public interface OnGoToDocumentClickListener{
        void onGoToDocumentClick(String documentURL, View view);
    }
    public interface OnDeleteDocumentClickListener{
        void onDeleteDocumentClick(String uuid);
    }

    DocumentsAdapter.OnGoToDocumentClickListener onGoToDocumentClickListener;
    DocumentsAdapter.OnDeleteDocumentClickListener onDeleteDocumentClickListener;

    public DocumentsAdapter(List<Document> data) {
        this.data = data;
    }

    public DocumentsAdapter(DocumentsAdapter.OnGoToDocumentClickListener onGoToDocumentClickListener, DocumentsAdapter.OnDeleteDocumentClickListener onDeleteDocumentClickListener) {
        this.onGoToDocumentClickListener = onGoToDocumentClickListener;
        this.onDeleteDocumentClickListener = onDeleteDocumentClickListener;
        data = new ArrayList<>();
    }

    public void updateData(List<Document> newData) {
        data = newData;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DocumentsAdapter.DocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocumentItemBinding binding = DocumentItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new DocumentsAdapter.DocumentHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsAdapter.DocumentHolder holder, int position) {
        holder.toDo(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DocumentHolder extends RecyclerView.ViewHolder {
        public DocumentItemBinding binding;

        public DocumentHolder(@NonNull View itemView) {
            super(itemView);
            binding = DocumentItemBinding.bind(itemView);
        }

        public void toDo(int position) {
            binding.btnDelete.setOnClickListener(v -> onDeleteDocumentClickListener.onDeleteDocumentClick(data.get(position).getUuid()));
            binding.window.setOnClickListener(v -> onGoToDocumentClickListener.onGoToDocumentClick(data.get(position).getDocumentURL(), v));
            binding.textTitle.setText(data.get(position).getTitle());
        }
    }
}
