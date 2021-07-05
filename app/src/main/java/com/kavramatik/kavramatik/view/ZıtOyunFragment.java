package com.kavramatik.kavramatik.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentOyunZitBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.OppositesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ZıtOyunFragment extends Fragment {

    private FragmentOyunZitBinding binding;
    private OppositesViewModel viewModel;
    Random rastgele = new Random();
    ArrayList<Integer> sayilar = new ArrayList<>();
    ArrayList<Integer> sayilar2 = new ArrayList<>();
    String SayiEtiket = "sayi";
    int puan = 0;
    Animation animasyonYukari, animasyonAsagi, animasyonGorunurluk, animasyonGorunurluk2, animasyonGorunurluk3;
    Boolean animasyonbasladi = false;
    Boolean animasyonbasladi2 = false;
    Boolean animasyonbasladi3 = false;
    Boolean animasyonbitti = false;
    Boolean animasyonbitti2 = false;
    Boolean animasyonbitti3 = false;
    MediaPlayer arkaplanmuzik, alkissesi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOyunZitBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OppositesViewModel.class);
        animasyonYukari = AnimationUtils.loadAnimation(getContext(), R.anim.animasyonyukari);
        animasyonAsagi = AnimationUtils.loadAnimation(getContext(), R.anim.animasyonasagi);
        animasyonGorunurluk = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk);
        animasyonGorunurluk2 = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk2);
        animasyonGorunurluk3 = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk3);
        alkissesi = MediaPlayer.create(getContext(), R.raw.alkissesi);
        arkaplanmuzik = MediaPlayer.create(getContext(), R.raw.arkaplanmuzik);
        //observeData();
        viewModel.getData();
        rastgele();
        observeData();
    }

    public void observeData() {
        //rastgele();
        viewModel.isLoading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.oppositeProgress.setVisibility(View.VISIBLE);
            } else {
                binding.oppositeProgress.setVisibility(View.GONE);
                binding.puan.setText("Skorunuz: " + puan);
                //binding.puan.setText("0");
                arkaplanmuzik.setLooping(true);
                arkaplanmuzik.start();
            }
        });
        viewModel.oppositeLiveData.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
            binding.ustZit.setImageBitmap(image1);
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
            binding.sagZit.setImageBitmap(image2);
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
            binding.solZit.setImageBitmap(image3);
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
            binding.ortaZit.setImageBitmap(image4);
            /*binding.ustZit.setAnimation(animasyonAsagi);
            binding.solZit.setAnimation(animasyonYukari);
            binding.ortaZit.setAnimation(animasyonYukari);
            binding.sagZit.setAnimation(animasyonYukari);*/
            binding.ustZit.setTag(SayiEtiket);
            binding.ustZit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData clipData = new ClipData(view.getTag().toString(), mimeTypes, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(clipData, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                    return true;
                }
            });

            binding.ustTasarim.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()){
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                            view.invalidate();
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        case DragEvent.ACTION_DROP:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            View gorselNesne = (View) dragEvent.getLocalState();
                            ViewGroup eskiTasarimAlani = (ViewGroup) gorselNesne.getParent();
                            eskiTasarimAlani.removeView(gorselNesne);
                            LinearLayout hedefTasarimAlani = (LinearLayout) view;
                            hedefTasarimAlani.addView(gorselNesne);
                            gorselNesne.setVisibility(View.VISIBLE);
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        default: break;
                    }
                    return false;
                }
            });

            binding.solTasarim.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()){
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if(!animasyonbitti){
                                view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                                view.invalidate();
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        case DragEvent.ACTION_DROP:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            View gorselNesne = (View) dragEvent.getLocalState();
                            ViewGroup eskiTasarimAlani = (ViewGroup) gorselNesne.getParent();
                            eskiTasarimAlani.removeView(gorselNesne);
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustZit.getDrawable();
                            Bitmap ustZit = drawable.getBitmap();
                            BitmapDrawable drawable2 = (BitmapDrawable) binding.solZit.getDrawable();
                            Bitmap solZit = drawable2.getBitmap();
                            if(ustZit.sameAs(solZit)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.OPPOSITE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    int sayi1 = rastgele.nextInt(2);
                                    if(sayi1 == 0){
                                        sayilar = new ArrayList<>();
                                        rastgele();
                                        int sayi2 = rastgele.nextInt(3);
                                        if(sayi2 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        sayilar2 = new ArrayList<>();
                                        rastgele2();
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustZit.startAnimation(animasyonAsagi);
                                    binding.solZit.startAnimation(animasyonYukari);
                                    binding.ortaZit.startAnimation(animasyonYukari);
                                    binding.sagZit.startAnimation(animasyonYukari);
                                    animasyonbasladi2 = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti2 = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solZit);
                                    binding.ortaTasarim.removeView(binding.ortaZit);
                                    binding.sagTasarim.removeView(binding.sagZit);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }

                            }
                            else if(!ustZit.sameAs(solZit)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi){
                                    binding.solZit.startAnimation(animasyonGorunurluk);
                                    animasyonbasladi = true;
                                }
                                animasyonbitti = true;
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        default: break;
                    }
                    return false;
                }
            });

            binding.ortaTasarim.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()){
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if(!animasyonbitti2){
                                view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                                view.invalidate();
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        case DragEvent.ACTION_DROP:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            View gorselNesne = (View) dragEvent.getLocalState();
                            ViewGroup eskiTasarimAlani = (ViewGroup) gorselNesne.getParent();
                            eskiTasarimAlani.removeView(gorselNesne);
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustZit.getDrawable();
                            Bitmap ustZit = drawable.getBitmap();
                            BitmapDrawable drawable3 = (BitmapDrawable) binding.ortaZit.getDrawable();
                            Bitmap ortaZit = drawable3.getBitmap();
                            if(ustZit.sameAs(ortaZit)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.OPPOSITE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    int sayi1 = rastgele.nextInt(2);
                                    if(sayi1 == 0){
                                        sayilar = new ArrayList<>();
                                        rastgele();
                                        int sayi2 = rastgele.nextInt(3);
                                        if(sayi2 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        sayilar2 = new ArrayList<>();
                                        rastgele2();
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustZit.startAnimation(animasyonAsagi);
                                    binding.solZit.startAnimation(animasyonYukari);
                                    binding.ortaZit.startAnimation(animasyonYukari);
                                    binding.sagZit.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solZit);
                                    binding.ortaTasarim.removeView(binding.ortaZit);
                                    binding.sagTasarim.removeView(binding.sagZit);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustZit.sameAs(ortaZit)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi2){
                                    binding.ortaZit.startAnimation(animasyonGorunurluk2);
                                    animasyonbasladi2 = true;
                                }
                                animasyonbitti2 = true;
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        default: break;
                    }
                    return false;
                }
            });

            binding.sagTasarim.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch (dragEvent.getAction()){
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            if(!animasyonbitti3){
                                view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                                view.invalidate();
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        case DragEvent.ACTION_DROP:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            View gorselNesne = (View) dragEvent.getLocalState();
                            ViewGroup eskiTasarimAlani = (ViewGroup) gorselNesne.getParent();
                            eskiTasarimAlani.removeView(gorselNesne);
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustZit.getDrawable();
                            Bitmap ustZit = drawable.getBitmap();
                            BitmapDrawable drawable4 = (BitmapDrawable) binding.sagZit.getDrawable();
                            Bitmap sagZit = drawable4.getBitmap();
                            if(ustZit.sameAs(sagZit)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.OPPOSITE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    int sayi1 = rastgele.nextInt(2);
                                    if(sayi1 == 0){
                                        sayilar = new ArrayList<>();
                                        rastgele();
                                        int sayi2 = rastgele.nextInt(3);
                                        if(sayi2 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getOppositeOneImage());
                                            binding.sagZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getOppositeOneImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getOppositeOneImage());
                                            binding.ortaZit.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        sayilar2 = new ArrayList<>();
                                        rastgele2();
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.ustZit.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getOppositeTwoImage());
                                            binding.sagZit.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getOppositeTwoImage());
                                            binding.solZit.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getOppositeTwoImage());
                                            binding.ortaZit.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustZit.startAnimation(animasyonAsagi);
                                    binding.solZit.startAnimation(animasyonYukari);
                                    binding.ortaZit.startAnimation(animasyonYukari);
                                    binding.sagZit.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi2 = false;
                                    animasyonbitti = false;
                                    animasyonbitti2 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solZit);
                                    binding.ortaTasarim.removeView(binding.ortaZit);
                                    binding.sagTasarim.removeView(binding.sagZit);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustZit.sameAs(sagZit)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi3){
                                    binding.sagZit.startAnimation(animasyonGorunurluk3);
                                    animasyonbasladi3 = true;
                                }
                                animasyonbitti3 = true;
                            }
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            view.getBackground().clearColorFilter();
                            view.invalidate();
                            return true;
                        default: break;
                    }
                    return false;
                }
            });
        });
    }
    private void rastgele() {
        for (int i = 2; i < 27; i++) {
            sayilar.add(i);
        }
        Collections.shuffle(sayilar);
    }
    private void rastgele2() {
        for (int i = 0; i < 27; i++) {
            sayilar2.add(i);
        }
        Collections.shuffle(sayilar2);
    }
    @Override
    public void onPause() {
        super.onPause();
        arkaplanmuzik.pause();
    }
    /*public void geriSayim() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {

            }
        }.start();
    }*/
}