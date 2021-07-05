package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kavramatik.kavramatik.databinding.QuantityRowBinding;
import com.kavramatik.kavramatik.model.QuantityModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import org.jetbrains.annotations.NotNull;

public class QuantityRecyclerView extends RecyclerView.Adapter<QuantityRecyclerView.ViewHolder> {

    private final ImageClickInterface anInterface;
    private QuantityModel quantityModel;

    public QuantityRecyclerView(ImageClickInterface imageClickInterface) {
        this.anInterface = imageClickInterface;
    }

    @NonNull
    @Override
    public QuantityRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        QuantityRowBinding binding = QuantityRowBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuantityRecyclerView.ViewHolder holder, int position) {
        holder.binding.setQuantity(quantityModel);
        holder.binding.executePendingBindings();
        holder.binding.quantityImage.setOnClickListener(v -> anInterface.onItemClick(quantityModel.getQuantityOneText()));
        holder.binding.quantityImageTwo.setOnClickListener(v -> anInterface.onItemClick(quantityModel.getQuantityTwoText()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addNewModel(QuantityModel model) {
        quantityModel = model;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        QuantityRowBinding binding;

        public ViewHolder(@NonNull @NotNull QuantityRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
