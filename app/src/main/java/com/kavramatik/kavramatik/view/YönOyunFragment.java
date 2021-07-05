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
import com.kavramatik.kavramatik.databinding.FragmentOyunYonBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.DirectionViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class YönOyunFragment extends Fragment {

    private FragmentOyunYonBinding binding;
    private DirectionViewModel viewModel;
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
        binding = FragmentOyunYonBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DirectionViewModel.class);
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
        viewModel.loading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.directionProgress.setVisibility(View.VISIBLE);
            } else {
                binding.directionProgress.setVisibility(View.GONE);
                binding.puan.setText("Skorunuz: " + puan);
                //binding.puan.setText("0");
                arkaplanmuzik.setLooping(true);
                arkaplanmuzik.start();
            }
        });
        viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
            binding.ustYon.setImageBitmap(image1);
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
            binding.sagYon.setImageBitmap(image2);
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
            binding.solYon.setImageBitmap(image3);
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
            binding.ortaYon.setImageBitmap(image4);
            /*binding.ustYon.setAnimation(animasyonAsagi);
            binding.solYon.setAnimation(animasyonYukari);
            binding.ortaYon.setAnimation(animasyonYukari);
            binding.sagYon.setAnimation(animasyonYukari);*/
            binding.ustYon.setTag(SayiEtiket);
            binding.ustYon.setOnLongClickListener(new View.OnLongClickListener() {
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustYon.getDrawable();
                            Bitmap ustYon = drawable.getBitmap();
                            BitmapDrawable drawable2 = (BitmapDrawable) binding.solYon.getDrawable();
                            Bitmap solYon = drawable2.getBitmap();
                            if(ustYon.sameAs(solYon)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.DIRECTION_SCORE);
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
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        sayilar2 = new ArrayList<>();
                                        rastgele2();
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustYon.startAnimation(animasyonAsagi);
                                    binding.solYon.startAnimation(animasyonYukari);
                                    binding.ortaYon.startAnimation(animasyonYukari);
                                    binding.sagYon.startAnimation(animasyonYukari);
                                    animasyonbasladi2 = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti2 = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solYon);
                                    binding.ortaTasarim.removeView(binding.ortaYon);
                                    binding.sagTasarim.removeView(binding.sagYon);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustYon.sameAs(solYon)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi){
                                    binding.solYon.startAnimation(animasyonGorunurluk);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustYon.getDrawable();
                            Bitmap ustYon = drawable.getBitmap();
                            BitmapDrawable drawable3 = (BitmapDrawable) binding.ortaYon.getDrawable();
                            Bitmap ortaYon = drawable3.getBitmap();
                            if(ustYon.sameAs(ortaYon)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.DIRECTION_SCORE);
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
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        sayilar2 = new ArrayList<>();
                                        rastgele2();
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustYon.startAnimation(animasyonAsagi);
                                    binding.solYon.startAnimation(animasyonYukari);
                                    binding.ortaYon.startAnimation(animasyonYukari);
                                    binding.sagYon.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solYon);
                                    binding.ortaTasarim.removeView(binding.ortaYon);
                                    binding.sagTasarim.removeView(binding.sagYon);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustYon.sameAs(ortaYon)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi2){
                                    binding.ortaYon.startAnimation(animasyonGorunurluk2);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustYon.getDrawable();
                            Bitmap ustYon = drawable.getBitmap();
                            BitmapDrawable drawable4 = (BitmapDrawable) binding.sagYon.getDrawable();
                            Bitmap sagYon = drawable4.getBitmap();
                            if(ustYon.sameAs(sagYon)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.DIRECTION_SCORE);
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
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        sayilar2 = new ArrayList<>();
                                        rastgele2();
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.ustYon.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar2.get(0)).getDirectionImage());
                                            binding.sagYon.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar2.get(1)).getDirectionImage());
                                            binding.solYon.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar2.get(2)).getDirectionImage());
                                            binding.ortaYon.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustYon.startAnimation(animasyonAsagi);
                                    binding.solYon.startAnimation(animasyonYukari);
                                    binding.ortaYon.startAnimation(animasyonYukari);
                                    binding.sagYon.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi2 = false;
                                    animasyonbitti = false;
                                    animasyonbitti2 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solYon);
                                    binding.ortaTasarim.removeView(binding.ortaYon);
                                    binding.sagTasarim.removeView(binding.sagYon);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustYon.sameAs(sagYon)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi3){
                                    binding.sagYon.startAnimation(animasyonGorunurluk3);
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
        sayilar.add(0);
        sayilar.add(4);
        sayilar.add(8);
        sayilar.add(12);
        sayilar.add(16);
        sayilar.add(20);
        Collections.shuffle(sayilar);
    }
    private void rastgele2() {
        sayilar2.add(2);
        sayilar2.add(6);
        sayilar2.add(10);
        sayilar2.add(14);
        sayilar2.add(18);
        sayilar2.add(22);
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