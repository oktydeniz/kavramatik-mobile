package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.NumberRowBinding;
import com.kavramatik.kavramatik.model.NumberModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

import org.jetbrains.annotations.NotNull;

public class NumberRecyclerView extends RecyclerView.Adapter<NumberRecyclerView.ViewHolder> {

    private final ImageClickInterface imageClickInterface;
    private NumberModel model;

    public NumberRecyclerView(ImageClickInterface clickInterface) {
        this.imageClickInterface = clickInterface;
    }

    @NonNull
    @NotNull
    @Override
    public NumberRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NumberRowBinding binding = NumberRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberRecyclerView.ViewHolder holder, int position) {
        holder.binding.setNumber(model);
        holder.binding.executePendingBindings();
        holder.binding.numberImage.setOnClickListener(v -> imageClickInterface.onItemClick(model.getNumberText()));

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addItem(NumberModel newItem) {
        model = newItem;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        NumberRowBinding binding;

        public ViewHolder(@NonNull NumberRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
