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
import com.kavramatik.kavramatik.databinding.FragmentOyunRenkBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.ColorMatchViewModel;
import com.kavramatik.kavramatik.viewModel.ColorViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RenkOyunFragment extends Fragment {

    private FragmentOyunRenkBinding binding;
    private ColorMatchViewModel viewModel;
    Random rastgele = new Random();
    ArrayList<Integer> sayilar = new ArrayList<>();
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
        binding = FragmentOyunRenkBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ColorMatchViewModel.class);
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
        viewModel.loadingColors.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.colorProgress.setVisibility(View.VISIBLE);
            } else {
                binding.colorProgress.setVisibility(View.GONE);
                binding.puan.setText("Skorunuz: " + puan);
                //binding.puan.setText("0");
                arkaplanmuzik.setLooping(true);
                arkaplanmuzik.start();
            }
        });
        viewModel.mutableLiveDataColors.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
            binding.ustRenk.setImageBitmap(image1);
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
            binding.ortaRenk.setImageBitmap(image2);
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
            binding.solRenk.setImageBitmap(image3);
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
            binding.sagRenk.setImageBitmap(image4);
            /*binding.ustRenk.setAnimation(animasyonAsagi);
            binding.solRenk.setAnimation(animasyonYukari);
            binding.ortaRenk.setAnimation(animasyonYukari);
            binding.sagRenk.setAnimation(animasyonYukari);*/
            binding.ustRenk.setTag(SayiEtiket);
            binding.ustRenk.setOnLongClickListener(new View.OnLongClickListener() {
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustRenk.getDrawable();
                            Bitmap ustRenk = drawable.getBitmap();
                            BitmapDrawable drawable2 = (BitmapDrawable) binding.solRenk.getDrawable();
                            Bitmap solRenk = drawable2.getBitmap();
                            if(ustRenk.sameAs(solRenk)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.COLOR_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    sayilar = new ArrayList<>();
                                    rastgele();
                                    int sayi = rastgele.nextInt(3);
                                    if(sayi == 0){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.solRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image4);
                                    }
                                    else if(sayi == 1){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.solRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image4);
                                    }
                                    else{
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.solRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image4);
                                    }
                                    binding.ustRenk.startAnimation(animasyonAsagi);
                                    binding.solRenk.startAnimation(animasyonYukari);
                                    binding.ortaRenk.startAnimation(animasyonYukari);
                                    binding.sagRenk.startAnimation(animasyonYukari);
                                    animasyonbasladi2 = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti2 = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solRenk);
                                    binding.ortaTasarim.removeView(binding.ortaRenk);
                                    binding.sagTasarim.removeView(binding.sagRenk);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustRenk.sameAs(solRenk)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi){
                                    binding.solRenk.startAnimation(animasyonGorunurluk);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustRenk.getDrawable();
                            Bitmap ustRenk = drawable.getBitmap();
                            BitmapDrawable drawable3 = (BitmapDrawable) binding.ortaRenk.getDrawable();
                            Bitmap ortaRenk = drawable3.getBitmap();
                            if(ustRenk.sameAs(ortaRenk)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.COLOR_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    sayilar = new ArrayList<>();
                                    rastgele();
                                    int sayi = rastgele.nextInt(3);
                                    if(sayi == 0){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.solRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image4);
                                    }
                                    else if(sayi == 1){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.solRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image4);
                                    }
                                    else{
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.solRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image4);
                                    }
                                    binding.ustRenk.startAnimation(animasyonAsagi);
                                    binding.solRenk.startAnimation(animasyonYukari);
                                    binding.ortaRenk.startAnimation(animasyonYukari);
                                    binding.sagRenk.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solRenk);
                                    binding.ortaTasarim.removeView(binding.ortaRenk);
                                    binding.sagTasarim.removeView(binding.sagRenk);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustRenk.sameAs(ortaRenk)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi2){
                                    binding.ortaRenk.startAnimation(animasyonGorunurluk2);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustRenk.getDrawable();
                            Bitmap ustRenk = drawable.getBitmap();
                            BitmapDrawable drawable4 = (BitmapDrawable) binding.sagRenk.getDrawable();
                            Bitmap sagRenk = drawable4.getBitmap();
                            if(ustRenk.sameAs(sagRenk)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.COLOR_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    sayilar = new ArrayList<>();
                                    rastgele();
                                    int sayi = rastgele.nextInt(3);
                                    if(sayi == 0){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.solRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image4);
                                    }
                                    else if(sayi == 1){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.solRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image4);
                                    }
                                    else{
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.ustRenk.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getColorOne());
                                        binding.sagRenk.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getColorOne());
                                        binding.solRenk.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getColorOne());
                                        binding.ortaRenk.setImageBitmap(image4);
                                    }
                                    binding.ustRenk.startAnimation(animasyonAsagi);
                                    binding.solRenk.startAnimation(animasyonYukari);
                                    binding.ortaRenk.startAnimation(animasyonYukari);
                                    binding.sagRenk.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi2 = false;
                                    animasyonbitti = false;
                                    animasyonbitti2 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solRenk);
                                    binding.ortaTasarim.removeView(binding.ortaRenk);
                                    binding.sagTasarim.removeView(binding.sagRenk);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustRenk.sameAs(sagRenk)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi3){
                                    binding.sagRenk.startAnimation(animasyonGorunurluk3);
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
        int sayi = rastgele.nextInt(4);
        if(sayi == 0){
            sayilar.add(0);
            sayilar.add(4);
            sayilar.add(8);
            sayilar.add(12);
            sayilar.add(16);
            sayilar.add(20);
            sayilar.add(24);
            sayilar.add(28);
            Collections.shuffle(sayilar);
        }
        if(sayi == 1){
            sayilar.add(1);
            sayilar.add(5);
            sayilar.add(9);
            sayilar.add(13);
            sayilar.add(17);
            sayilar.add(21);
            sayilar.add(25);
            sayilar.add(29);
            Collections.shuffle(sayilar);
        }
        if(sayi == 2){
            sayilar.add(2);
            sayilar.add(6);
            sayilar.add(10);
            sayilar.add(14);
            sayilar.add(18);
            sayilar.add(22);
            sayilar.add(26);
            sayilar.add(30);
            Collections.shuffle(sayilar);
        }
        if(sayi == 3){
            sayilar.add(3);
            sayilar.add(7);
            sayilar.add(11);
            sayilar.add(15);
            sayilar.add(19);
            sayilar.add(23);
            sayilar.add(27);
            sayilar.add(31);
            Collections.shuffle(sayilar);
        }
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