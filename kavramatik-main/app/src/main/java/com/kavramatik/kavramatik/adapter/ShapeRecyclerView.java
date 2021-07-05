package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.ShapeRowBinding;
import com.kavramatik.kavramatik.model.DirectionModel;
import com.kavramatik.kavramatik.model.ShapeModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

import java.util.List;

public class ShapeRecyclerView extends RecyclerView.Adapter<ShapeRecyclerView.ViewHolder> {
    private ShapeModel shapeModels;
    private final ImageClickInterface imageClickInterface;

    public ShapeRecyclerView(ImageClickInterface clickInterface) {
        this.imageClickInterface = clickInterface;
    }

    @NonNull
    @Override
    public ShapeRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ShapeRowBinding binding = ShapeRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShapeRecyclerView.ViewHolder holder, int position) {
        holder.binding.setShape(shapeModels);
        holder.binding.executePendingBindings();
        holder.binding.shapeCircleImage.setOnClickListener(v -> imageClickInterface.onItemClick(shapeModels.getShapeText()));
        holder.binding.shapeCircleImageTwo.setOnClickListener(v -> imageClickInterface.onItemClick(shapeModels.getShapeTwoText()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addItem(ShapeModel newModel) {
        shapeModels = newModel;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeRowBinding binding;

        public ViewHolder(@NonNull ShapeRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
