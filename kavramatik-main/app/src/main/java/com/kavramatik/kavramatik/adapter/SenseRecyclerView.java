package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.SenseRowBinding;
import com.kavramatik.kavramatik.model.SenseModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

public class SenseRecyclerView extends RecyclerView.Adapter<SenseRecyclerView.ViewHolder> {

    private final ImageClickInterface imageClickInterface;
    private SenseModel senseModel;

    public SenseRecyclerView(ImageClickInterface imageClickInterface) {
        this.imageClickInterface = imageClickInterface;
    }

    @NonNull
    @Override
    public SenseRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SenseRowBinding binding = SenseRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SenseRecyclerView.ViewHolder holder, int position) {
        holder.binding.setSense(senseModel);
        holder.binding.executePendingBindings();
        holder.binding.senseImage.setOnClickListener(v -> imageClickInterface.onItemClick(senseModel.getSenseOneText()));
        holder.binding.senseImageTwo.setOnClickListener(v -> imageClickInterface.onItemClick(senseModel.getSenseTwoText()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void showNew(SenseModel model) {
        senseModel = model;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SenseRowBinding binding;

        public ViewHolder(@NonNull SenseRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
