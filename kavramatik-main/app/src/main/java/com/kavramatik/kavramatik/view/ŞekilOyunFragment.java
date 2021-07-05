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
import com.kavramatik.kavramatik.databinding.FragmentOyunSekilBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.ShapeViewModel;

import java.util.ArrayList;
import java.util.Random;

public class ŞekilOyunFragment extends Fragment {

    private FragmentOyunSekilBinding binding;
    private ShapeViewModel viewModel;
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
        binding = FragmentOyunSekilBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ShapeViewModel.class);
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
                binding.shapeProgress.setVisibility(View.VISIBLE);
            } else {
                binding.shapeProgress.setVisibility(View.GONE);
                binding.puan.setText("Skorunuz: " + puan);
                //binding.puan.setText("0");
                arkaplanmuzik.setLooping(true);
                arkaplanmuzik.start();
            }
        });
        viewModel.shapeModel.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
            binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
            binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
            binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
            binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
            /*binding.ustSekil.setAnimation(animasyonAsagi);
            binding.solSekil.setAnimation(animasyonYukari);
            binding.ortaSekil.setAnimation(animasyonYukari);
            binding.sagSekil.setAnimation(animasyonYukari);*/
            binding.ustSekil.setTag(SayiEtiket);
            binding.ustSekil.setOnLongClickListener(new View.OnLongClickListener() {
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustSekil.getDrawable();
                            Bitmap ustSekil = drawable.getBitmap();
                            BitmapDrawable drawable2 = (BitmapDrawable) binding.solSekil.getDrawable();
                            Bitmap solSekil = drawable2.getBitmap();
                            if(ustSekil.sameAs(solSekil)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.SHAPE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    int sayi = rastgele.nextInt(5);
                                    if(sayi == 0){
                                        sayilar = new ArrayList<>();
                                        rastgele();
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    else if(sayi == 1){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 160, 120, false));
                                    }
                                    else if(sayi == 2){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 160, 120, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 160, 120, false));
                                    }
                                    else if(sayi == 3){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 160, 120, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 160, 120, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 160, 120, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    else{
                                        sayilar = new ArrayList<>();
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 160, 120, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 160, 120, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    binding.ustSekil.startAnimation(animasyonAsagi);
                                    binding.solSekil.startAnimation(animasyonYukari);
                                    binding.ortaSekil.startAnimation(animasyonYukari);
                                    binding.sagSekil.startAnimation(animasyonYukari);
                                    animasyonbasladi2 = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti2 = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solSekil);
                                    binding.ortaTasarim.removeView(binding.ortaSekil);
                                    binding.sagTasarim.removeView(binding.sagSekil);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustSekil.sameAs(solSekil)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi){
                                    binding.solSekil.startAnimation(animasyonGorunurluk);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustSekil.getDrawable();
                            Bitmap ustSekil = drawable.getBitmap();
                            BitmapDrawable drawable3 = (BitmapDrawable) binding.ortaSekil.getDrawable();
                            Bitmap ortaSekil = drawable3.getBitmap();
                            if(ustSekil.sameAs(ortaSekil)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.SHAPE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    int sayi = rastgele.nextInt(5);
                                    if(sayi == 0){
                                        sayilar = new ArrayList<>();
                                        rastgele();
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    else if(sayi == 1){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 160, 120, false));
                                    }
                                    else if(sayi == 2){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 160, 120, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 160, 120, false));
                                    }
                                    else if(sayi == 3){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 160, 120, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 160, 120, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 160, 120, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    else{
                                        sayilar = new ArrayList<>();
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 160, 120, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 160, 120, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    binding.ustSekil.startAnimation(animasyonAsagi);
                                    binding.solSekil.startAnimation(animasyonYukari);
                                    binding.ortaSekil.startAnimation(animasyonYukari);
                                    binding.sagSekil.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solSekil);
                                    binding.ortaTasarim.removeView(binding.ortaSekil);
                                    binding.sagTasarim.removeView(binding.sagSekil);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustSekil.sameAs(ortaSekil)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi2){
                                    binding.ortaSekil.startAnimation(animasyonGorunurluk2);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustSekil.getDrawable();
                            Bitmap ustSekil = drawable.getBitmap();
                            BitmapDrawable drawable4 = (BitmapDrawable) binding.sagSekil.getDrawable();
                            Bitmap sagSekil = drawable4.getBitmap();
                            if(ustSekil.sameAs(sagSekil)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.SHAPE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    int sayi = rastgele.nextInt(5);
                                    if(sayi == 0){
                                        sayilar = new ArrayList<>();
                                        rastgele();
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    else if(sayi == 1){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 160, 120, false));
                                    }
                                    else if(sayi == 2){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 160, 120, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 160, 120, false));
                                    }
                                    else if(sayi == 3){
                                        sayilar = new ArrayList<>();
                                        sayilar.add(6);
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 160, 120, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 160, 120, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 160, 120, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    else{
                                        sayilar = new ArrayList<>();
                                        sayilar.add(8);
                                        sayilar.add(0);
                                        sayilar.add(2);
                                        sayilar.add(4);
                                        sayilar.add(6);
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ustSekil.setImageBitmap(Bitmap.createScaledBitmap(image1, 160, 120, false));
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getShapeImage());
                                        binding.ortaSekil.setImageBitmap(Bitmap.createScaledBitmap(image2, 160, 120, false));
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getShapeImage());
                                        binding.solSekil.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getShapeImage());
                                        binding.sagSekil.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                    }
                                    binding.ustSekil.startAnimation(animasyonAsagi);
                                    binding.solSekil.startAnimation(animasyonYukari);
                                    binding.ortaSekil.startAnimation(animasyonYukari);
                                    binding.sagSekil.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi2 = false;
                                    animasyonbitti = false;
                                    animasyonbitti2 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solSekil);
                                    binding.ortaTasarim.removeView(binding.ortaSekil);
                                    binding.sagTasarim.removeView(binding.sagSekil);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustSekil.sameAs(sagSekil)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi3){
                                    binding.sagSekil.startAnimation(animasyonGorunurluk3);
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
        sayilar.add(2);
        sayilar.add(4);
        sayilar.add(6);
        sayilar.add(8);
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