package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kavramatik.kavramatik.databinding.OppositesRowBinding;
import com.kavramatik.kavramatik.model.OppositesModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

public class OppositeRecyclerView extends RecyclerView.Adapter<OppositeRecyclerView.ViewHolder> {
    private final ImageClickInterface imageClickInterface;
    private OppositesModel oppositesModel;

    public OppositeRecyclerView(ImageClickInterface imageClickInterface) {
        this.imageClickInterface = imageClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OppositesRowBinding binding = OppositesRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.oppositesRowBinding.setOpposite(oppositesModel);
        holder.oppositesRowBinding.executePendingBindings();
        holder.oppositesRowBinding.oppositeImage.setOnClickListener(v -> imageClickInterface.onItemClick(oppositesModel.getOppositeOneImageText()));
        holder.oppositesRowBinding.oppositeImageTwo.setOnClickListener(v -> imageClickInterface.onItemClick(oppositesModel.getOppositeTwoImageText()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void showNew(OppositesModel model) {
        oppositesModel = model;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        OppositesRowBinding oppositesRowBinding;

        public ViewHolder(@NonNull OppositesRowBinding itemView) {
            super(itemView.getRoot());
            this.oppositesRowBinding = itemView;
        }
    }
}
