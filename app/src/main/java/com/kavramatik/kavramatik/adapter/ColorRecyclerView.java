package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.ColorRowBinding;
import com.kavramatik.kavramatik.model.ColorModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;


public class ColorRecyclerView extends RecyclerView.Adapter<ColorRecyclerView.ViewHolder> {

    private ColorModel models;
    private final ImageClickInterface imageClickInterface;

    public ColorRecyclerView(ImageClickInterface clickInterface) {
        this.imageClickInterface = clickInterface;
    }

    @NonNull
    @Override
    public ColorRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ColorRowBinding binding = ColorRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ColorRecyclerView.ViewHolder holder, int position) {
        holder.binding.setColor(models);
        holder.binding.executePendingBindings();
        holder.binding.colorCircleImage.setOnClickListener(v -> imageClickInterface.onItemClick(models.getColorText()));
    }

    public void addNewColor(ColorModel colorModel) {
        this.models = colorModel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ColorRowBinding binding;

        public ViewHolder(@NonNull ColorRowBinding colorRowBinding) {
            super(colorRowBinding.getRoot());
            this.binding = colorRowBinding;
        }
    }
}
