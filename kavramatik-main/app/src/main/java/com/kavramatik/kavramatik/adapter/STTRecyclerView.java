package com.kavramatik.kavramatik.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavramatik.kavramatik.databinding.SttRowBinding;
import com.kavramatik.kavramatik.model.STTModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;

import java.util.Locale;

public class STTRecyclerView extends RecyclerView.Adapter<STTRecyclerView.ViewHolder> {
    private STTModel sttModel;
    private Context context;
    TextToSpeech tts;
    private final ImageClickInterface imageClickInterface;

    public STTRecyclerView(ImageClickInterface imageClickInterface) {
        this.imageClickInterface = imageClickInterface;
    }

    @NonNull
    @Override
    public STTRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        SttRowBinding binding = SttRowBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sttRowBinding.setModel(sttModel);
        holder.sttRowBinding.executePendingBindings();
        String s = holder.sttRowBinding.textView6.getText().toString();
        boolean isFirst = SharedPreferencesManager.getSttIsFirst(context);
        if (isFirst) speech(s);

        holder.sttRowBinding.startListening.setOnClickListener(v -> imageClickInterface.onItemClick(sttModel.getText()));
    }

    private void speech(String s) {
        tts = new TextToSpeech(context, status -> {
            try {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.getDefault());
                    Locale locale = new Locale("tr", "TR");

                    int result = tts.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        AppAlertDialogs.showAlertDialogForSettings(context);
                    } else {
                        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, "");
                    }
                } else {
                    AppAlertDialogs.noTTSPacketDialog(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        SharedPreferencesManager.setSttIsFirst(context, false);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void addItem(STTModel model) {
        this.sttModel = model;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        SttRowBinding sttRowBinding;

        public ViewHolder(@NonNull SttRowBinding itemView) {
            super(itemView.getRoot());
            this.sttRowBinding = itemView;
        }
    }
}
