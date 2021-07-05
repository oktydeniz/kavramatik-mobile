package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.DimensionRowBinding;
import com.kavramatik.kavramatik.model.DimensionModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

public class DimensionRecyclerView extends RecyclerView.Adapter<DimensionRecyclerView.ViewHolder> {

    private ImageClickInterface anInterface;
    private DimensionModel dimensionModel;

    public DimensionRecyclerView(ImageClickInterface anInterface) {
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DimensionRowBinding binding = DimensionRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setDimension(dimensionModel);
        holder.binding.executePendingBindings();
        holder.binding.firstImageDimension.setOnClickListener(v -> anInterface.onItemClick(dimensionModel.getDimensionImageTextOne()));
        holder.binding.secondImageDimension.setOnClickListener(v -> anInterface.onItemClick(dimensionModel.getDimensionImageTextTwo()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addNewDimension(DimensionModel model) {
        dimensionModel = model;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        DimensionRowBinding binding;

        public ViewHolder(@NonNull DimensionRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
