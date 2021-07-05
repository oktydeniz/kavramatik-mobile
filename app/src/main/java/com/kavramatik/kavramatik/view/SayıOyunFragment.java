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
import com.kavramatik.kavramatik.databinding.FragmentOyunSayiBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.view.user.ProfileFragment;
import com.kavramatik.kavramatik.viewModel.NumberViewModel;
import java.util.Random;

public class SayıOyunFragment extends Fragment {

    private FragmentOyunSayiBinding binding;
    private NumberViewModel viewModel;
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
        binding = FragmentOyunSayiBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(NumberViewModel.class);
        animasyonYukari = AnimationUtils.loadAnimation(getContext(), R.anim.animasyonyukari);
        animasyonAsagi = AnimationUtils.loadAnimation(getContext(), R.anim.animasyonasagi);
        animasyonGorunurluk = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk);
        animasyonGorunurluk2 = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk2);
        animasyonGorunurluk3 = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk3);
        alkissesi = MediaPlayer.create(getContext(), R.raw.alkissesi);
        arkaplanmuzik = MediaPlayer.create(getContext(), R.raw.arkaplanmuzik);
        //binding.balon.setVisibility(View.GONE);
        //observeData();
        viewModel.getData();
        rastgele();
        observeData();

    }

    public void observeData() {
        //rastgele();
        viewModel.loading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.numberProgress.setVisibility(View.VISIBLE);
            } else {
                binding.numberProgress.setVisibility(View.GONE);
                binding.puan.setText("Skorunuz: " + puan);
                //binding.puan.setText("0");
                arkaplanmuzik.setLooping(true);
                arkaplanmuzik.start();
            }
        });
        viewModel.numberModel.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
            binding.ustSayi.setImageBitmap(image1);
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
            binding.solSayi.setImageBitmap(image2);
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
            binding.ortaSayi.setImageBitmap(image3);
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
            binding.sagSayi.setImageBitmap(image4);
            /*binding.ustSayi.setAnimation(animasyonAsagi);
            binding.solSayi.setAnimation(animasyonYukari);
            binding.ortaSayi.setAnimation(animasyonYukari);
            binding.sagSayi.setAnimation(animasyonYukari);*/
            binding.ustSayi.setTag(SayiEtiket);
            binding.ustSayi.setOnLongClickListener(new View.OnLongClickListener() {
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustSayi.getDrawable();
                            Bitmap ustSayi = drawable.getBitmap();
                            BitmapDrawable drawable2 = (BitmapDrawable) binding.solSayi.getDrawable();
                            Bitmap solSayi = drawable2.getBitmap();
                            if(ustSayi.sameAs(solSayi)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.NUMBER_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    rastgele();
                                    int sayi = rastgele.nextInt(3);
                                    if(sayi == 0){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image4);
                                    }
                                    else if(sayi == 1){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image4);
                                    }
                                    else{
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image4);
                                    }
                                /*binding.solSayi.setImageAlpha(255);
                                binding.ortaSayi.setImageAlpha(255);
                                binding.sagSayi.setImageAlpha(255);*/

                                    binding.ustSayi.startAnimation(animasyonAsagi);
                                    binding.solSayi.startAnimation(animasyonYukari);
                                    binding.ortaSayi.startAnimation(animasyonYukari);
                                    binding.sagSayi.startAnimation(animasyonYukari);
                                    animasyonbasladi2 = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti2 = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solSayi);
                                    binding.ortaTasarim.removeView(binding.ortaSayi);
                                    binding.sagTasarim.removeView(binding.sagSayi);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                    /*binding.balon.setVisibility(View.VISIBLE);
                                    binding.balon.startAnimation(balonYukari);*/
                                }
                            }
                            else if(!ustSayi.sameAs(solSayi)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                if(!animasyonbasladi){
                                    binding.solSayi.startAnimation(animasyonGorunurluk);
                                    animasyonbasladi = true;
                                }
                                animasyonbitti = true;
                                //binding.solSayi.setImageAlpha(110);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustSayi.getDrawable();
                            Bitmap ustSayi = drawable.getBitmap();
                            BitmapDrawable drawable3 = (BitmapDrawable) binding.ortaSayi.getDrawable();
                            Bitmap ortaSayi = drawable3.getBitmap();
                            if(ustSayi.sameAs(ortaSayi)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.NUMBER_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    rastgele();
                                    int sayi = rastgele.nextInt(3);
                                    if(sayi == 0){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image4);
                                    }
                                    else if(sayi == 1){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image4);
                                    }
                                    else{
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image4);
                                    }
                                /*binding.ortaSayi.setImageAlpha(255);
                                binding.solSayi.setImageAlpha(255);
                                binding.sagSayi.setImageAlpha(255);*/
                                    binding.ustSayi.startAnimation(animasyonAsagi);
                                    binding.solSayi.startAnimation(animasyonYukari);
                                    binding.ortaSayi.startAnimation(animasyonYukari);
                                    binding.sagSayi.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi3 = false;
                                    animasyonbitti = false;
                                    animasyonbitti3 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solSayi);
                                    binding.ortaTasarim.removeView(binding.ortaSayi);
                                    binding.sagTasarim.removeView(binding.sagSayi);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                    /*binding.balon.setVisibility(View.VISIBLE);
                                    binding.balon.startAnimation(balonYukari);*/
                                }

                            }
                            else if(!ustSayi.sameAs(ortaSayi)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                //binding.ortaSayi.setImageAlpha(110);
                                if(!animasyonbasladi2){
                                    binding.ortaSayi.startAnimation(animasyonGorunurluk2);
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
                            BitmapDrawable drawable = (BitmapDrawable) binding.ustSayi.getDrawable();
                            Bitmap ustSayi = drawable.getBitmap();
                            BitmapDrawable drawable4 = (BitmapDrawable) binding.sagSayi.getDrawable();
                            Bitmap sagSayi = drawable4.getBitmap();
                            if(ustSayi.sameAs(sagSayi)) {
                                alkissesi.start();
                                puan += 5;
                                binding.puan.setText("Skorunuz: " + puan);
                                Scores.updateScore(requireContext(), Scores.NUMBER_SCORE);
                                /*puan += 10;
                                binding.puan.setText("" + puan);*/
                                if(puan < 100){
                                    binding.ustTasarim.addView(gorselNesne);
                                    gorselNesne.setVisibility(View.VISIBLE);
                                    rastgele();
                                    int sayi = rastgele.nextInt(3);
                                    if(sayi == 0){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image4);
                                    }
                                    else if(sayi == 1){
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image4);
                                    }
                                    else{
                                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.ustSayi.setImageBitmap(image1);
                                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar[0]).getNumberImage());
                                        binding.sagSayi.setImageBitmap(image2);
                                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar[1]).getNumberImage());
                                        binding.solSayi.setImageBitmap(image3);
                                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar[2]).getNumberImage());
                                        binding.ortaSayi.setImageBitmap(image4);
                                    }
                                /*binding.sagSayi.setImageAlpha(255);
                                binding.solSayi.setImageAlpha(255);
                                binding.ortaSayi.setImageAlpha(255);*/
                                    binding.ustSayi.startAnimation(animasyonAsagi);
                                    binding.solSayi.startAnimation(animasyonYukari);
                                    binding.ortaSayi.startAnimation(animasyonYukari);
                                    binding.sagSayi.startAnimation(animasyonYukari);
                                    animasyonbasladi = false;
                                    animasyonbasladi2 = false;
                                    animasyonbitti = false;
                                    animasyonbitti2 = false;
                                }
                                else {
                                    binding.solTasarim.removeView(binding.solSayi);
                                    binding.ortaTasarim.removeView(binding.ortaSayi);
                                    binding.sagTasarim.removeView(binding.sagSayi);
                                    binding.puan.setVisibility(View.GONE);
                                    binding.tebrikler.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setVisibility(View.VISIBLE);
                                    binding.sonSkor.setText("Skorunuz: " + puan);
                                    /*binding.balon.setVisibility(View.VISIBLE);
                                    binding.balon.startAnimation(balonYukari);*/
                                }


                            }
                            else if(!ustSayi.sameAs(sagSayi)){
                                binding.ustTasarim.addView(gorselNesne);
                                gorselNesne.setVisibility(View.VISIBLE);
                                //binding.sagSayi.setImageAlpha(110);
                                if(!animasyonbasladi3){
                                    binding.sagSayi.startAnimation(animasyonGorunurluk3);
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
            int sayi1 = rastgele.nextInt(11);
            int sayi2 = rastgele.nextInt(11);
            int sayi3 = rastgele.nextInt(11);
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