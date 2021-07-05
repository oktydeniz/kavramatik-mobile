package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.DirectionRowBinding;
import com.kavramatik.kavramatik.model.DirectionModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

public class DirectionRecyclerView extends RecyclerView.Adapter<DirectionRecyclerView.ViewHolder> {

    private final ImageClickInterface imageClickInterface;
    private DirectionModel directionModel;

    public DirectionRecyclerView(ImageClickInterface imageClickInterface) {
        this.imageClickInterface = imageClickInterface;
    }

    @NonNull
    @Override
    public DirectionRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DirectionRowBinding binding = DirectionRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectionRecyclerView.ViewHolder holder, int position) {
        holder.binding.setDirection(directionModel);
        holder.binding.executePendingBindings();
        holder.binding.directionImage.setOnClickListener(v -> imageClickInterface.onItemClick(directionModel.getDirectionText()));
        holder.binding.directionImageTwo.setOnClickListener(v -> imageClickInterface.onItemClick(directionModel.getDirectionTwoText()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addItem(DirectionModel newModel) {
        directionModel = newModel;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        DirectionRowBinding binding;

        public ViewHolder(@NonNull DirectionRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
