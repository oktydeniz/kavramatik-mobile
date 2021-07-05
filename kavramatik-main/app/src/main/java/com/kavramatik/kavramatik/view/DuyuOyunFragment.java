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
import com.kavramatik.kavramatik.databinding.FragmentOyunDuyuBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.SenseViewModel;

import java.util.Random;

public class DuyuOyunFragment extends Fragment {

    private FragmentOyunDuyuBinding binding;
    private SenseViewModel viewModel;
    Random rastgele = new Random();
    int[] sayilar = new int[3];
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
        binding = FragmentOyunDuyuBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SenseViewModel.class);
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
                binding.senseProgress.setVisibility(View.VISIBLE);
            } else {
                binding.senseProgress.setVisibility(View.GONE);
                binding.puan.setText("Skorunuz: " + puan);
                //binding.puan.setText("0");
                arkaplanmuzik.setLooping(true);
                arkaplanmuzik.start();
            }
        });
        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
            binding.ustDuyu.setImageBitmap(image1);
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
            binding.sagDuyu.setImageBitmap(image2);
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
            binding.solDuyu.setImageBitmap(image3);
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
            binding.ortaDuyu.setImageBitmap(image4);
            /*binding.ustDuyu.setAnimation(animasyonAsagi);
            binding.solDuyu.setAnimation(animasyonYukari);
            binding.ortaDuyu.setAnimation(animasyonYukari);
            binding.sagDuyu.setAnimation(animasyonYukari);*/
            binding.ustDuyu.setTag(SayiEtiket);
            binding.ustDuyu.setOnLongClickListener(new View.OnLongClickListener() {
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustDuyu.getDrawable();
                            Bitmap ustDuyu = drawable.getBitmap();
                            BitmapDrawable drawable2 = (BitmapDrawable) binding.solDuyu.getDrawable();
                            Bitmap solDuyu = drawable2.getBitmap();
                            if(ustDuyu.sameAs(solDuyu)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.SENSE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    rastgele();
                                    int sayi1 = rastgele.nextInt(2);
                                    if(sayi1 == 0){
                                        int sayi2 = rastgele.nextInt(3);
                                        if(sayi2 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustDuyu.startAnimation(animasyonAsagi);
                                    binding.solDuyu.startAnimation(animasyonYukari);
                                    binding.ortaDuyu.startAnimation(animasyonYukari);
                                    binding.sagDuyu.startAnimation(animasyonYukari);
                                    animasyonbasladi2 = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti2 = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solDuyu);
                                    binding.ortaTasarim.removeView(binding.ortaDuyu);
                                    binding.sagTasarim.removeView(binding.sagDuyu);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustDuyu.sameAs(solDuyu)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi){
                                    binding.solDuyu.startAnimation(animasyonGorunurluk);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustDuyu.getDrawable();
                            Bitmap ustDuyu = drawable.getBitmap();
                            BitmapDrawable drawable3 = (BitmapDrawable) binding.ortaDuyu.getDrawable();
                            Bitmap ortaDuyu = drawable3.getBitmap();
                            if(ustDuyu.sameAs(ortaDuyu)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.SENSE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    rastgele();
                                    int sayi1 = rastgele.nextInt(2);
                                    if(sayi1 == 0){
                                        int sayi2 = rastgele.nextInt(3);
                                        if(sayi2 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustDuyu.startAnimation(animasyonAsagi);
                                    binding.solDuyu.startAnimation(animasyonYukari);
                                    binding.ortaDuyu.startAnimation(animasyonYukari);
                                    binding.sagDuyu.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solDuyu);
                                    binding.ortaTasarim.removeView(binding.ortaDuyu);
                                    binding.sagTasarim.removeView(binding.sagDuyu);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustDuyu.sameAs(ortaDuyu)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi2){
                                    binding.ortaDuyu.startAnimation(animasyonGorunurluk2);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustDuyu.getDrawable();
                            Bitmap ustDuyu = drawable.getBitmap();
                            BitmapDrawable drawable4 = (BitmapDrawable) binding.sagDuyu.getDrawable();
                            Bitmap sagDuyu = drawable4.getBitmap();
                            if(ustDuyu.sameAs(sagDuyu)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.SENSE_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    rastgele();
                                    int sayi1 = rastgele.nextInt(2);
                                    if(sayi1 == 0){
                                        int sayi2 = rastgele.nextInt(3);
                                        if(sayi2 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else if(sayi2 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseOneImage());
                                            binding.sagDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseOneImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseOneImage());
                                            binding.ortaDuyu.setImageBitmap(image4);
                                        }
                                    }
                                    else{
                                        int sayi3 = rastgele.nextInt(3);
                                        if(sayi3 == 0){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else if(sayi3 == 1){
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image4);
                                        }
                                        else{
                                            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.ustDuyu.setImageBitmap(image1);
                                            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getSenseTwoImage());
                                            binding.sagDuyu.setImageBitmap(image2);
                                            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getSenseTwoImage());
                                            binding.solDuyu.setImageBitmap(image3);
                                            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getSenseTwoImage());
                                            binding.ortaDuyu.setImageBitmap(image4);
                                        }
                                    }
                                    binding.ustDuyu.startAnimation(animasyonAsagi);
                                    binding.solDuyu.startAnimation(animasyonYukari);
                                    binding.ortaDuyu.startAnimation(animasyonYukari);
                                    binding.sagDuyu.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi2 = false;
                                    animasyonbitti = false;
                                    animasyonbitti2 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solDuyu);
                                    binding.ortaTasarim.removeView(binding.ortaDuyu);
                                    binding.sagTasarim.removeView(binding.sagDuyu);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                }
                            }
                            else if(!ustDuyu.sameAs(sagDuyu)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi3){
                                    binding.sagDuyu.startAnimation(animasyonGorunurluk3);
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
        while(true){
            int sayi1 = rastgele.nextInt(23);
            int sayi2 = rastgele.nextInt(23);
            int sayi3 = rastgele.nextInt(23);
            if(sayi1 != sayi2 && sayi1 !=sayi3 && sayi2 != sayi3) {
                sayilar[0] = sayi1;
                sayilar[1] = sayi2;
                sayilar[2] = sayi3;
                break;
            }
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