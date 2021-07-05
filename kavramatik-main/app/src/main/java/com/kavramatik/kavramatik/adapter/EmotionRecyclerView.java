package com.kavramatik.kavramatik.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.EmotionRowBinding;
import com.kavramatik.kavramatik.model.EmotionModel;
import com.kavramatik.kavramatik.util.ImageClickInterface;

import org.jetbrains.annotations.NotNull;

public class EmotionRecyclerView extends RecyclerView.Adapter<EmotionRecyclerView.ViewHolder> {
    private final ImageClickInterface imageClickInterface;
    private EmotionModel emotionModel;

    public EmotionRecyclerView(ImageClickInterface clickInterface) {
        this.imageClickInterface = clickInterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EmotionRowBinding binding = EmotionRowBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.binding.setEmotion(emotionModel);
        holder.binding.executePendingBindings();
        holder.binding.emotionImage.setOnClickListener(v -> imageClickInterface.onItemClick(emotionModel.getEmotionText()));

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addNewItem(EmotionModel model) {
        this.emotionModel = model;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EmotionRowBinding binding;

        public ViewHolder(@NonNull EmotionRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
