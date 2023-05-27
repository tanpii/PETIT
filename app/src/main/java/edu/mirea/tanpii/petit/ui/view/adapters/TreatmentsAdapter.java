package edu.mirea.tanpii.petit.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.mirea.tanpii.petit.R;
import edu.mirea.tanpii.petit.data.models.Treatment;
import edu.mirea.tanpii.petit.databinding.TreatmentItemBinding;

public class TreatmentsAdapter extends RecyclerView.Adapter<TreatmentsAdapter.TreatmentHolder> {

    List<Treatment> data;

    TreatmentsAdapter.OnDeleteTreatmentClickListener onDeleteTreatmentClickListener;

    public interface OnDeleteTreatmentClickListener {
        void OnDeleteTreatmentClick(String uuid);
    }

    public void updateData(List<Treatment> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    public TreatmentsAdapter(List<Treatment> data) {
        this.data = data;
    }

    public TreatmentsAdapter(TreatmentsAdapter.OnDeleteTreatmentClickListener onDeleteTreatmentClickListener) {
        data = new ArrayList<Treatment>();
        this.onDeleteTreatmentClickListener = onDeleteTreatmentClickListener;
    }

    @NonNull
    @Override
    public TreatmentsAdapter.TreatmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TreatmentItemBinding treatmentBinding = TreatmentItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new TreatmentHolder(treatmentBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull TreatmentsAdapter.TreatmentHolder holder, int position) {
        holder.toDo(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class TreatmentHolder extends RecyclerView.ViewHolder {
        public TreatmentItemBinding binding;
        public TreatmentHolder(@NonNull View itemView) {
            super(itemView);
            binding = TreatmentItemBinding.bind(itemView);
        }
        public void toDo(int position) {
            Date date = new Date(data.get(position).getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            binding.textDate.setText(sdf.format(date));
            binding.textTitle.setText(data.get(position).getTitle());
            binding.textNote.setText(data.get(position).getNote());
            switch (data.get(position).getIcon()) {
                case Treatment.medicine_icon:
                    binding.iconTreatment.setImageResource(R.drawable.medicine_icon);
                    break;
                case Treatment.clinic_icon:
                    binding.iconTreatment.setImageResource(R.drawable.clinic_icon);
                    break;
                case Treatment.search_icon:
                    binding.iconTreatment.setImageResource(R.drawable.search_icon);
                    break;
                case Treatment.tablet_icon:
                    binding.iconTreatment.setImageResource(R.drawable.tablet_icon);
                    break;
                default:
                    break;
            }
            binding.btnDelete.setOnClickListener(v -> {
                onDeleteTreatmentClickListener.OnDeleteTreatmentClick(data.get(position).getUuid());
            });
        }
    }
}
