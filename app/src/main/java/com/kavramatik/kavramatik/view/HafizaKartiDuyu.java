package com.kavramatik.kavramatik.view;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.HafizaKartiDuyuBinding;
import com.kavramatik.kavramatik.databinding.HafizaKartiMiktarBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.QuantityViewModel;
import com.kavramatik.kavramatik.viewModel.SenseViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class HafizaKartiDuyu extends Fragment {

    public HafizaKartiDuyuBinding binding;
    int[] cardsArray = {101, 102, 201, 202};
    //int image101, image102, image201, image202;
    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    public SenseViewModel viewModel;
    ArrayList<Integer> sayilar = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    //int ipucuSayisi = 3;
    Animation animasyonGorunurluk;
    MediaPlayer alkissesi;
    int tur;
    int skor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HafizaKartiDuyuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SenseViewModel.class);
        animasyonGorunurluk = AnimationUtils.loadAnimation(getContext(), R.anim.animasyongorunurluk);
        alkissesi = MediaPlayer.create(getContext(), R.raw.alkissesi);
        //binding.kalanIpucu.setText("Kalan İpucu: " + ipucuSayisi);
        binding.kalanIpucu.setVisibility(View.GONE);
        binding.skor.setText("Skorunuz: " + skor);
        tur++;
        viewModel.getData();
        rastgele();
        imageViews.add(binding.kart10);
        imageViews.add(binding.kart11);
        imageViews.add(binding.kart14);
        imageViews.add(binding.kart15);
        Collections.shuffle(imageViews);
        imageViews.get(0).setTag("0");
        imageViews.get(1).setTag("1");
        imageViews.get(2).setTag("2");
        imageViews.get(3).setTag("3");
        //frontOfCardsResources();
        //Collections.shuffle(Arrays.asList(cardsArray));
        imageViews.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(imageViews.get(0), theCard);
            }
        });
        imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(imageViews.get(1), theCard);
            }
        });
        imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(imageViews.get(2), theCard);
            }
        });
        imageViews.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int theCard = Integer.parseInt((String) view.getTag());
                doStuff(imageViews.get(3), theCard);
            }
        });
        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
            binding.ipucu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                    imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                    imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                    imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                    geriSayim();
                }
            });
        });
        alkissesi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                binding.ipucu.setEnabled(true);
                if(tur == 2){
                    imageViews.get(0).setVisibility(View.VISIBLE);
                    imageViews.get(1).setVisibility(View.VISIBLE);
                    imageViews.get(2).setVisibility(View.VISIBLE);
                    imageViews.get(3).setVisibility(View.VISIBLE);
                    binding.kart6.setVisibility(View.VISIBLE);
                    binding.kart7.setVisibility(View.VISIBLE);
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews = new ArrayList<>();
                    sayilar = new ArrayList<>();
                    cardsArray = new int[6];
                    cardsArray[0] = 101;
                    cardsArray[1] = 102;
                    cardsArray[2] = 103;
                    cardsArray[3] = 201;
                    cardsArray[4] = 202;
                    cardsArray[5] = 203;
                    rastgele();
                    imageViews.add(binding.kart10);
                    imageViews.add(binding.kart11);
                    imageViews.add(binding.kart14);
                    imageViews.add(binding.kart15);
                    imageViews.add(binding.kart6);
                    imageViews.add(binding.kart7);
                    Collections.shuffle(imageViews);
                    imageViews.get(0).setTag("0");
                    imageViews.get(1).setTag("1");
                    imageViews.get(2).setTag("2");
                    imageViews.get(3).setTag("3");
                    imageViews.get(4).setTag("4");
                    imageViews.get(5).setTag("5");
                    imageViews.get(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(0), theCard);
                        }
                    });
                    imageViews.get(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(1), theCard);
                        }
                    });
                    imageViews.get(2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(2), theCard);
                        }
                    });
                    imageViews.get(3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(3), theCard);
                        }
                    });
                    imageViews.get(4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(4), theCard);
                        }
                    });
                    imageViews.get(5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(5), theCard);
                        }
                    });
                    viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                geriSayim();
                            }
                        });
                    });
                }
                else if(tur == 3){
                    imageViews.get(0).setVisibility(View.VISIBLE);
                    imageViews.get(1).setVisibility(View.VISIBLE);
                    imageViews.get(2).setVisibility(View.VISIBLE);
                    imageViews.get(3).setVisibility(View.VISIBLE);
                    imageViews.get(4).setVisibility(View.VISIBLE);
                    imageViews.get(5).setVisibility(View.VISIBLE);
                    binding.kart2.setVisibility(View.VISIBLE);
                    binding.kart3.setVisibility(View.VISIBLE);
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews = new ArrayList<>();
                    sayilar = new ArrayList<>();
                    cardsArray = new int[8];
                    cardsArray[0] = 101;
                    cardsArray[1] = 102;
                    cardsArray[2] = 103;
                    cardsArray[3] = 104;
                    cardsArray[4] = 201;
                    cardsArray[5] = 202;
                    cardsArray[6] = 203;
                    cardsArray[7] = 204;
                    rastgele();
                    imageViews.add(binding.kart10);
                    imageViews.add(binding.kart11);
                    imageViews.add(binding.kart14);
                    imageViews.add(binding.kart15);
                    imageViews.add(binding.kart6);
                    imageViews.add(binding.kart7);
                    imageViews.add(binding.kart2);
                    imageViews.add(binding.kart3);
                    Collections.shuffle(imageViews);
                    imageViews.get(0).setTag("0");
                    imageViews.get(1).setTag("1");
                    imageViews.get(2).setTag("2");
                    imageViews.get(3).setTag("3");
                    imageViews.get(4).setTag("4");
                    imageViews.get(5).setTag("5");
                    imageViews.get(6).setTag("6");
                    imageViews.get(7).setTag("7");
                    imageViews.get(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(0), theCard);
                        }
                    });
                    imageViews.get(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(1), theCard);
                        }
                    });
                    imageViews.get(2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(2), theCard);
                        }
                    });
                    imageViews.get(3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(3), theCard);
                        }
                    });
                    imageViews.get(4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(4), theCard);
                        }
                    });
                    imageViews.get(5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(5), theCard);
                        }
                    });
                    imageViews.get(6).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(6), theCard);
                        }
                    });
                    imageViews.get(7).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(7), theCard);
                        }
                    });
                    viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getSenseOneImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                geriSayim();
                            }
                        });
                    });
                }
                else if(tur == 4){
                    imageViews.get(0).setVisibility(View.VISIBLE);
                    imageViews.get(1).setVisibility(View.VISIBLE);
                    imageViews.get(2).setVisibility(View.VISIBLE);
                    imageViews.get(3).setVisibility(View.VISIBLE);
                    imageViews.get(4).setVisibility(View.VISIBLE);
                    imageViews.get(5).setVisibility(View.VISIBLE);
                    imageViews.get(6).setVisibility(View.VISIBLE);
                    imageViews.get(7).setVisibility(View.VISIBLE);
                    binding.kart1.setVisibility(View.VISIBLE);
                    binding.kart4.setVisibility(View.VISIBLE);
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews = new ArrayList<>();
                    sayilar = new ArrayList<>();
                    cardsArray = new int[10];
                    cardsArray[0] = 101;
                    cardsArray[1] = 102;
                    cardsArray[2] = 103;
                    cardsArray[3] = 104;
                    cardsArray[4] = 105;
                    cardsArray[5] = 201;
                    cardsArray[6] = 202;
                    cardsArray[7] = 203;
                    cardsArray[8] = 204;
                    cardsArray[9] = 205;
                    rastgele();
                    imageViews.add(binding.kart10);
                    imageViews.add(binding.kart11);
                    imageViews.add(binding.kart14);
                    imageViews.add(binding.kart15);
                    imageViews.add(binding.kart6);
                    imageViews.add(binding.kart7);
                    imageViews.add(binding.kart2);
                    imageViews.add(binding.kart3);
                    imageViews.add(binding.kart1);
                    imageViews.add(binding.kart4);
                    Collections.shuffle(imageViews);
                    imageViews.get(0).setTag("0");
                    imageViews.get(1).setTag("1");
                    imageViews.get(2).setTag("2");
                    imageViews.get(3).setTag("3");
                    imageViews.get(4).setTag("4");
                    imageViews.get(5).setTag("5");
                    imageViews.get(6).setTag("6");
                    imageViews.get(7).setTag("7");
                    imageViews.get(8).setTag("8");
                    imageViews.get(9).setTag("9");
                    imageViews.get(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(0), theCard);
                        }
                    });
                    imageViews.get(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(1), theCard);
                        }
                    });
                    imageViews.get(2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(2), theCard);
                        }
                    });
                    imageViews.get(3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(3), theCard);
                        }
                    });
                    imageViews.get(4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(4), theCard);
                        }
                    });
                    imageViews.get(5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(5), theCard);
                        }
                    });
                    imageViews.get(6).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(6), theCard);
                        }
                    });
                    imageViews.get(7).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(7), theCard);
                        }
                    });
                    imageViews.get(8).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(8), theCard);
                        }
                    });
                    imageViews.get(9).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(9), theCard);
                        }
                    });
                    viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getSenseOneImage());
                        Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getSenseOneImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(8).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(9).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                geriSayim();
                            }
                        });
                    });
                }
                else if(tur == 5){
                    imageViews.get(0).setVisibility(View.VISIBLE);
                    imageViews.get(1).setVisibility(View.VISIBLE);
                    imageViews.get(2).setVisibility(View.VISIBLE);
                    imageViews.get(3).setVisibility(View.VISIBLE);
                    imageViews.get(4).setVisibility(View.VISIBLE);
                    imageViews.get(5).setVisibility(View.VISIBLE);
                    imageViews.get(6).setVisibility(View.VISIBLE);
                    imageViews.get(7).setVisibility(View.VISIBLE);
                    imageViews.get(8).setVisibility(View.VISIBLE);
                    imageViews.get(9).setVisibility(View.VISIBLE);
                    binding.kart5.setVisibility(View.VISIBLE);
                    binding.kart8.setVisibility(View.VISIBLE);
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                    imageViews = new ArrayList<>();
                    sayilar = new ArrayList<>();
                    cardsArray = new int[12];
                    cardsArray[0] = 101;
                    cardsArray[1] = 102;
                    cardsArray[2] = 103;
                    cardsArray[3] = 104;
                    cardsArray[4] = 105;
                    cardsArray[5] = 106;
                    cardsArray[6] = 201;
                    cardsArray[7] = 202;
                    cardsArray[8] = 203;
                    cardsArray[9] = 204;
                    cardsArray[10] = 205;
                    cardsArray[11] = 206;
                    rastgele();
                    imageViews.add(binding.kart10);
                    imageViews.add(binding.kart11);
                    imageViews.add(binding.kart14);
                    imageViews.add(binding.kart15);
                    imageViews.add(binding.kart6);
                    imageViews.add(binding.kart7);
                    imageViews.add(binding.kart2);
                    imageViews.add(binding.kart3);
                    imageViews.add(binding.kart1);
                    imageViews.add(binding.kart4);
                    imageViews.add(binding.kart5);
                    imageViews.add(binding.kart8);
                    Collections.shuffle(imageViews);
                    imageViews.get(0).setTag("0");
                    imageViews.get(1).setTag("1");
                    imageViews.get(2).setTag("2");
                    imageViews.get(3).setTag("3");
                    imageViews.get(4).setTag("4");
                    imageViews.get(5).setTag("5");
                    imageViews.get(6).setTag("6");
                    imageViews.get(7).setTag("7");
                    imageViews.get(8).setTag("8");
                    imageViews.get(9).setTag("9");
                    imageViews.get(10).setTag("10");
                    imageViews.get(11).setTag("11");
                    imageViews.get(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(0), theCard);
                        }
                    });
                    imageViews.get(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(1), theCard);
                        }
                    });
                    imageViews.get(2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(2), theCard);
                        }
                    });
                    imageViews.get(3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(3), theCard);
                        }
                    });
                    imageViews.get(4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(4), theCard);
                        }
                    });
                    imageViews.get(5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(5), theCard);
                        }
                    });
                    imageViews.get(6).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(6), theCard);
                        }
                    });
                    imageViews.get(7).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(7), theCard);
                        }
                    });
                    imageViews.get(8).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(8), theCard);
                        }
                    });
                    imageViews.get(9).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(9), theCard);
                        }
                    });
                    imageViews.get(10).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(10), theCard);
                        }
                    });
                    imageViews.get(11).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(11), theCard);
                        }
                    });
                    viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getSenseOneImage());
                        Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getSenseOneImage());
                        Bitmap image6 = Base64Util.decodeToBitmap(model.get(sayilar.get(5)).getSenseOneImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(8).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(9).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(10).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(11).setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
                                geriSayim();
                            }
                        });
                    });
                }
                else if(tur == 6){
                    imageViews.get(0).setVisibility(View.VISIBLE);
                    imageViews.get(1).setVisibility(View.VISIBLE);
                    imageViews.get(2).setVisibility(View.VISIBLE);
                    imageViews.get(3).setVisibility(View.VISIBLE);
                    imageViews.get(4).setVisibility(View.VISIBLE);
                    imageViews.get(5).setVisibility(View.VISIBLE);
                    imageViews.get(6).setVisibility(View.VISIBLE);
                    imageViews.get(7).setVisibility(View.VISIBLE);
                    imageViews.get(8).setVisibility(View.VISIBLE);
                    imageViews.get(9).setVisibility(View.VISIBLE);
                    imageViews.get(10).setVisibility(View.VISIBLE);
                    imageViews.get(11).setVisibility(View.VISIBLE);
                    binding.kart9.setVisibility(View.VISIBLE);
                    binding.kart12.setVisibility(View.VISIBLE);
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                    imageViews.get(10).setImageResource(R.drawable.background2);
                    imageViews.get(11).setImageResource(R.drawable.background2);
                    imageViews = new ArrayList<>();
                    sayilar = new ArrayList<>();
                    cardsArray = new int[14];
                    cardsArray[0] = 101;
                    cardsArray[1] = 102;
                    cardsArray[2] = 103;
                    cardsArray[3] = 104;
                    cardsArray[4] = 105;
                    cardsArray[5] = 106;
                    cardsArray[6] = 107;
                    cardsArray[7] = 201;
                    cardsArray[8] = 202;
                    cardsArray[9] = 203;
                    cardsArray[10] = 204;
                    cardsArray[11] = 205;
                    cardsArray[12] = 206;
                    cardsArray[13] = 207;
                    rastgele();
                    imageViews.add(binding.kart10);
                    imageViews.add(binding.kart11);
                    imageViews.add(binding.kart14);
                    imageViews.add(binding.kart15);
                    imageViews.add(binding.kart6);
                    imageViews.add(binding.kart7);
                    imageViews.add(binding.kart2);
                    imageViews.add(binding.kart3);
                    imageViews.add(binding.kart1);
                    imageViews.add(binding.kart4);
                    imageViews.add(binding.kart5);
                    imageViews.add(binding.kart8);
                    imageViews.add(binding.kart9);
                    imageViews.add(binding.kart12);
                    Collections.shuffle(imageViews);
                    imageViews.get(0).setTag("0");
                    imageViews.get(1).setTag("1");
                    imageViews.get(2).setTag("2");
                    imageViews.get(3).setTag("3");
                    imageViews.get(4).setTag("4");
                    imageViews.get(5).setTag("5");
                    imageViews.get(6).setTag("6");
                    imageViews.get(7).setTag("7");
                    imageViews.get(8).setTag("8");
                    imageViews.get(9).setTag("9");
                    imageViews.get(10).setTag("10");
                    imageViews.get(11).setTag("11");
                    imageViews.get(12).setTag("12");
                    imageViews.get(13).setTag("13");
                    imageViews.get(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(0), theCard);
                        }
                    });
                    imageViews.get(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(1), theCard);
                        }
                    });
                    imageViews.get(2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(2), theCard);
                        }
                    });
                    imageViews.get(3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(3), theCard);
                        }
                    });
                    imageViews.get(4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(4), theCard);
                        }
                    });
                    imageViews.get(5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(5), theCard);
                        }
                    });
                    imageViews.get(6).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(6), theCard);
                        }
                    });
                    imageViews.get(7).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(7), theCard);
                        }
                    });
                    imageViews.get(8).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(8), theCard);
                        }
                    });
                    imageViews.get(9).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(9), theCard);
                        }
                    });
                    imageViews.get(10).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(10), theCard);
                        }
                    });
                    imageViews.get(11).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(11), theCard);
                        }
                    });
                    imageViews.get(12).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(12), theCard);
                        }
                    });
                    imageViews.get(13).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(13), theCard);
                        }
                    });
                    viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getSenseOneImage());
                        Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getSenseOneImage());
                        Bitmap image6 = Base64Util.decodeToBitmap(model.get(sayilar.get(5)).getSenseOneImage());
                        Bitmap image7 = Base64Util.decodeToBitmap(model.get(sayilar.get(6)).getSenseOneImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image7, 175, 175, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(8).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(9).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(10).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(11).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(12).setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
                                imageViews.get(13).setImageBitmap(Bitmap.createScaledBitmap(image7, 175, 175, false));
                                geriSayim();
                            }
                        });
                    });
                }
                else if(tur == 7){
                    imageViews.get(0).setVisibility(View.VISIBLE);
                    imageViews.get(1).setVisibility(View.VISIBLE);
                    imageViews.get(2).setVisibility(View.VISIBLE);
                    imageViews.get(3).setVisibility(View.VISIBLE);
                    imageViews.get(4).setVisibility(View.VISIBLE);
                    imageViews.get(5).setVisibility(View.VISIBLE);
                    imageViews.get(6).setVisibility(View.VISIBLE);
                    imageViews.get(7).setVisibility(View.VISIBLE);
                    imageViews.get(8).setVisibility(View.VISIBLE);
                    imageViews.get(9).setVisibility(View.VISIBLE);
                    imageViews.get(10).setVisibility(View.VISIBLE);
                    imageViews.get(11).setVisibility(View.VISIBLE);
                    imageViews.get(12).setVisibility(View.VISIBLE);
                    imageViews.get(13).setVisibility(View.VISIBLE);
                    binding.kart13.setVisibility(View.VISIBLE);
                    binding.kart16.setVisibility(View.VISIBLE);
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                    imageViews.get(10).setImageResource(R.drawable.background2);
                    imageViews.get(11).setImageResource(R.drawable.background2);
                    imageViews.get(12).setImageResource(R.drawable.background2);
                    imageViews.get(13).setImageResource(R.drawable.background2);
                    imageViews = new ArrayList<>();
                    sayilar = new ArrayList<>();
                    cardsArray = new int[16];
                    cardsArray[0] = 101;
                    cardsArray[1] = 102;
                    cardsArray[2] = 103;
                    cardsArray[3] = 104;
                    cardsArray[4] = 105;
                    cardsArray[5] = 106;
                    cardsArray[6] = 107;
                    cardsArray[7] = 108;
                    cardsArray[8] = 201;
                    cardsArray[9] = 202;
                    cardsArray[10] = 203;
                    cardsArray[11] = 204;
                    cardsArray[12] = 205;
                    cardsArray[13] = 206;
                    cardsArray[14] = 207;
                    cardsArray[15] = 208;
                    rastgele();
                    imageViews.add(binding.kart10);
                    imageViews.add(binding.kart11);
                    imageViews.add(binding.kart14);
                    imageViews.add(binding.kart15);
                    imageViews.add(binding.kart6);
                    imageViews.add(binding.kart7);
                    imageViews.add(binding.kart2);
                    imageViews.add(binding.kart3);
                    imageViews.add(binding.kart1);
                    imageViews.add(binding.kart4);
                    imageViews.add(binding.kart5);
                    imageViews.add(binding.kart8);
                    imageViews.add(binding.kart9);
                    imageViews.add(binding.kart12);
                    imageViews.add(binding.kart13);
                    imageViews.add(binding.kart16);
                    Collections.shuffle(imageViews);
                    imageViews.get(0).setTag("0");
                    imageViews.get(1).setTag("1");
                    imageViews.get(2).setTag("2");
                    imageViews.get(3).setTag("3");
                    imageViews.get(4).setTag("4");
                    imageViews.get(5).setTag("5");
                    imageViews.get(6).setTag("6");
                    imageViews.get(7).setTag("7");
                    imageViews.get(8).setTag("8");
                    imageViews.get(9).setTag("9");
                    imageViews.get(10).setTag("10");
                    imageViews.get(11).setTag("11");
                    imageViews.get(12).setTag("12");
                    imageViews.get(13).setTag("13");
                    imageViews.get(14).setTag("14");
                    imageViews.get(15).setTag("15");
                    imageViews.get(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(0), theCard);
                        }
                    });
                    imageViews.get(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(1), theCard);
                        }
                    });
                    imageViews.get(2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(2), theCard);
                        }
                    });
                    imageViews.get(3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(3), theCard);
                        }
                    });
                    imageViews.get(4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(4), theCard);
                        }
                    });
                    imageViews.get(5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(5), theCard);
                        }
                    });
                    imageViews.get(6).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(6), theCard);
                        }
                    });
                    imageViews.get(7).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(7), theCard);
                        }
                    });
                    imageViews.get(8).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(8), theCard);
                        }
                    });
                    imageViews.get(9).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(9), theCard);
                        }
                    });
                    imageViews.get(10).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(10), theCard);
                        }
                    });
                    imageViews.get(11).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(11), theCard);
                        }
                    });
                    imageViews.get(12).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(12), theCard);
                        }
                    });
                    imageViews.get(13).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(13), theCard);
                        }
                    });
                    imageViews.get(14).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(14), theCard);
                        }
                    });
                    imageViews.get(15).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int theCard = Integer.parseInt((String) view.getTag());
                            doStuff(imageViews.get(15), theCard);
                        }
                    });
                    viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getSenseOneImage());
                        Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getSenseOneImage());
                        Bitmap image6 = Base64Util.decodeToBitmap(model.get(sayilar.get(5)).getSenseOneImage());
                        Bitmap image7 = Base64Util.decodeToBitmap(model.get(sayilar.get(6)).getSenseOneImage());
                        Bitmap image8 = Base64Util.decodeToBitmap(model.get(sayilar.get(7)).getSenseOneImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image7, 175, 175, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image8, 175, 175, false));
                                imageViews.get(8).setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
                                imageViews.get(9).setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
                                imageViews.get(10).setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
                                imageViews.get(11).setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
                                imageViews.get(12).setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
                                imageViews.get(13).setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
                                imageViews.get(14).setImageBitmap(Bitmap.createScaledBitmap(image7, 175, 175, false));
                                imageViews.get(15).setImageBitmap(Bitmap.createScaledBitmap(image8, 175, 175, false));
                                geriSayim();
                            }
                        });
                    });
                }
            }
        });
    }

    /*private void frontOfCardsResources(){
        image101 = R.drawable.colors;
        image102 = R.drawable.dimensions;
        image201 = R.drawable.colors;
        image202 = R.drawable.dimensions;
    }*/

    private void doStuff(ImageView iv, int card){
        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getSenseOneImage());
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getSenseOneImage());
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getSenseOneImage());
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getSenseOneImage());
            Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getSenseOneImage());
            Bitmap image6 = Base64Util.decodeToBitmap(model.get(sayilar.get(5)).getSenseOneImage());
            Bitmap image7 = Base64Util.decodeToBitmap(model.get(sayilar.get(6)).getSenseOneImage());
            Bitmap image8 = Base64Util.decodeToBitmap(model.get(sayilar.get(7)).getSenseOneImage());
            if(cardsArray[card] == 101){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
            }
            else if(cardsArray[card] == 102){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
            }
            else if(cardsArray[card] == 103){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
            }
            else if(cardsArray[card] == 104){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
            }
            else if(cardsArray[card] == 105){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
            }
            else if(cardsArray[card] == 106){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
            }
            else if(cardsArray[card] == 107){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image7, 175, 175, false));
            }
            else if(cardsArray[card] == 108){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image8, 175, 175, false));
            }
            else if(cardsArray[card] == 201){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image1, 175, 175, false));
            }
            else if(cardsArray[card] == 202){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image2, 175, 175, false));
            }
            else if(cardsArray[card] == 203){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image3, 175, 175, false));
            }
            else if(cardsArray[card] == 204){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image4, 175, 175, false));
            }
            else if(cardsArray[card] == 205){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image5, 175, 175, false));
            }
            else if(cardsArray[card] == 206){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image6, 175, 175, false));
            }
            else if(cardsArray[card] == 207){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image7, 175, 175, false));
            }
            else if(cardsArray[card] == 208){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image8, 175, 175, false));
            }
            if(cardNumber == 1){
                firstCard = cardsArray[card];
                if(firstCard > 200){
                    firstCard = firstCard - 100;
                }
                cardNumber = 2;
                clickedFirst = card;
                iv.setEnabled(false);
                binding.ipucu.setEnabled(false);
            }
            else if(cardNumber == 2){
                secondCard = cardsArray[card];
                if(secondCard > 200){
                    secondCard = secondCard - 100;
                }
                cardNumber = 1;
                clickedSecond = card;
                /*binding.kart10.setEnabled(false);
                binding.kart11.setEnabled(false);
                binding.kart14.setEnabled(false);
                binding.kart15.setEnabled(false);*/
                if(tur == 1){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                }
                else if(tur == 2){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                    imageViews.get(4).setEnabled(false);
                    imageViews.get(5).setEnabled(false);
                }
                else if(tur == 3){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                    imageViews.get(4).setEnabled(false);
                    imageViews.get(5).setEnabled(false);
                    imageViews.get(6).setEnabled(false);
                    imageViews.get(7).setEnabled(false);
                }
                else if(tur == 4){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                    imageViews.get(4).setEnabled(false);
                    imageViews.get(5).setEnabled(false);
                    imageViews.get(6).setEnabled(false);
                    imageViews.get(7).setEnabled(false);
                    imageViews.get(8).setEnabled(false);
                    imageViews.get(9).setEnabled(false);
                }
                else if(tur == 5){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                    imageViews.get(4).setEnabled(false);
                    imageViews.get(5).setEnabled(false);
                    imageViews.get(6).setEnabled(false);
                    imageViews.get(7).setEnabled(false);
                    imageViews.get(8).setEnabled(false);
                    imageViews.get(9).setEnabled(false);
                    imageViews.get(10).setEnabled(false);
                    imageViews.get(11).setEnabled(false);
                }
                else if(tur == 6){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                    imageViews.get(4).setEnabled(false);
                    imageViews.get(5).setEnabled(false);
                    imageViews.get(6).setEnabled(false);
                    imageViews.get(7).setEnabled(false);
                    imageViews.get(8).setEnabled(false);
                    imageViews.get(9).setEnabled(false);
                    imageViews.get(10).setEnabled(false);
                    imageViews.get(11).setEnabled(false);
                    imageViews.get(12).setEnabled(false);
                    imageViews.get(13).setEnabled(false);
                }
                else if(tur == 7){
                    imageViews.get(0).setEnabled(false);
                    imageViews.get(1).setEnabled(false);
                    imageViews.get(2).setEnabled(false);
                    imageViews.get(3).setEnabled(false);
                    imageViews.get(4).setEnabled(false);
                    imageViews.get(5).setEnabled(false);
                    imageViews.get(6).setEnabled(false);
                    imageViews.get(7).setEnabled(false);
                    imageViews.get(8).setEnabled(false);
                    imageViews.get(9).setEnabled(false);
                    imageViews.get(10).setEnabled(false);
                    imageViews.get(11).setEnabled(false);
                    imageViews.get(12).setEnabled(false);
                    imageViews.get(13).setEnabled(false);
                    imageViews.get(14).setEnabled(false);
                    imageViews.get(15).setEnabled(false);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calculate();
                    }
                }, 500);
            }
        });

    }

    private void calculate(){
        if(firstCard == secondCard){
            if(clickedFirst == 0){
                //binding.kart10.setVisibility(View.GONE);
                imageViews.get(0).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 1){
                //binding.kart11.setVisibility(View.GONE);
                imageViews.get(1).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 2){
                //binding.kart14.setVisibility(View.GONE);
                imageViews.get(2).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 3){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(3).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 4){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(4).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 5){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(5).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 6){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(6).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 7){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(7).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 8){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(8).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 9){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(9).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 10){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(10).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 11){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(11).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 12){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(12).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 13){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(13).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 14){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(14).setVisibility(View.INVISIBLE);
            }
            else if(clickedFirst == 15){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(15).setVisibility(View.INVISIBLE);
            }
            if(clickedSecond == 0){
                //binding.kart10.setVisibility(View.GONE);
                imageViews.get(0).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 1){
                //binding.kart11.setVisibility(View.GONE);
                imageViews.get(1).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 2){
                //binding.kart14.setVisibility(View.GONE);
                imageViews.get(2).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 3){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(3).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 4){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(4).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 5){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(5).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 6){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(6).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 7){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(7).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 8){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(8).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 9){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(9).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 10){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(10).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 11){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(11).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 12){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(12).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 13){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(13).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 14){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(14).setVisibility(View.INVISIBLE);
            }
            else if(clickedSecond == 15){
                //binding.kart15.setVisibility(View.GONE);
                imageViews.get(15).setVisibility(View.INVISIBLE);
            }
            skor+=5;
            binding.skor.setText("Skorunuz: " + skor);
            Scores.updateScore(requireContext(), Scores.SENSE_SCORE);
            binding.ipucu.setEnabled(true);
        }
        else{
            /*binding.kart10.setImageResource(R.drawable.background2);
            binding.kart11.setImageResource(R.drawable.background2);
            binding.kart14.setImageResource(R.drawable.background2);
            binding.kart15.setImageResource(R.drawable.background2);*/
            if(tur == 1){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }
            else if(tur == 2){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                imageViews.get(4).setImageResource(R.drawable.background2);
                imageViews.get(5).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }
            else if(tur == 3){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                imageViews.get(4).setImageResource(R.drawable.background2);
                imageViews.get(5).setImageResource(R.drawable.background2);
                imageViews.get(6).setImageResource(R.drawable.background2);
                imageViews.get(7).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }
            else if(tur == 4){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                imageViews.get(4).setImageResource(R.drawable.background2);
                imageViews.get(5).setImageResource(R.drawable.background2);
                imageViews.get(6).setImageResource(R.drawable.background2);
                imageViews.get(7).setImageResource(R.drawable.background2);
                imageViews.get(8).setImageResource(R.drawable.background2);
                imageViews.get(9).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }
            else if(tur == 5){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                imageViews.get(4).setImageResource(R.drawable.background2);
                imageViews.get(5).setImageResource(R.drawable.background2);
                imageViews.get(6).setImageResource(R.drawable.background2);
                imageViews.get(7).setImageResource(R.drawable.background2);
                imageViews.get(8).setImageResource(R.drawable.background2);
                imageViews.get(9).setImageResource(R.drawable.background2);
                imageViews.get(10).setImageResource(R.drawable.background2);
                imageViews.get(11).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }
            else if(tur == 6){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                imageViews.get(4).setImageResource(R.drawable.background2);
                imageViews.get(5).setImageResource(R.drawable.background2);
                imageViews.get(6).setImageResource(R.drawable.background2);
                imageViews.get(7).setImageResource(R.drawable.background2);
                imageViews.get(8).setImageResource(R.drawable.background2);
                imageViews.get(9).setImageResource(R.drawable.background2);
                imageViews.get(10).setImageResource(R.drawable.background2);
                imageViews.get(11).setImageResource(R.drawable.background2);
                imageViews.get(12).setImageResource(R.drawable.background2);
                imageViews.get(13).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }
            else if(tur == 7){
                imageViews.get(0).setImageResource(R.drawable.background2);
                imageViews.get(1).setImageResource(R.drawable.background2);
                imageViews.get(2).setImageResource(R.drawable.background2);
                imageViews.get(3).setImageResource(R.drawable.background2);
                imageViews.get(4).setImageResource(R.drawable.background2);
                imageViews.get(5).setImageResource(R.drawable.background2);
                imageViews.get(6).setImageResource(R.drawable.background2);
                imageViews.get(7).setImageResource(R.drawable.background2);
                imageViews.get(8).setImageResource(R.drawable.background2);
                imageViews.get(9).setImageResource(R.drawable.background2);
                imageViews.get(10).setImageResource(R.drawable.background2);
                imageViews.get(11).setImageResource(R.drawable.background2);
                imageViews.get(12).setImageResource(R.drawable.background2);
                imageViews.get(13).setImageResource(R.drawable.background2);
                imageViews.get(14).setImageResource(R.drawable.background2);
                imageViews.get(15).setImageResource(R.drawable.background2);
                binding.ipucu.setEnabled(true);
            }

        }
        /*binding.kart10.setEnabled(true);
        binding.kart11.setEnabled(true);
        binding.kart14.setEnabled(true);
        binding.kart15.setEnabled(true);*/
        if(tur == 1){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            checkEnd();
        }
        else if(tur == 2){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            imageViews.get(4).setEnabled(true);
            imageViews.get(5).setEnabled(true);
            checkEnd();
        }
        else if(tur == 3){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            imageViews.get(4).setEnabled(true);
            imageViews.get(5).setEnabled(true);
            imageViews.get(6).setEnabled(true);
            imageViews.get(7).setEnabled(true);
            checkEnd();
        }
        else if(tur == 4){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            imageViews.get(4).setEnabled(true);
            imageViews.get(5).setEnabled(true);
            imageViews.get(6).setEnabled(true);
            imageViews.get(7).setEnabled(true);
            imageViews.get(8).setEnabled(true);
            imageViews.get(9).setEnabled(true);
            checkEnd();
        }
        else if(tur == 5){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            imageViews.get(4).setEnabled(true);
            imageViews.get(5).setEnabled(true);
            imageViews.get(6).setEnabled(true);
            imageViews.get(7).setEnabled(true);
            imageViews.get(8).setEnabled(true);
            imageViews.get(9).setEnabled(true);
            imageViews.get(10).setEnabled(true);
            imageViews.get(11).setEnabled(true);
            checkEnd();
        }
        else if(tur == 6){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            imageViews.get(4).setEnabled(true);
            imageViews.get(5).setEnabled(true);
            imageViews.get(6).setEnabled(true);
            imageViews.get(7).setEnabled(true);
            imageViews.get(8).setEnabled(true);
            imageViews.get(9).setEnabled(true);
            imageViews.get(10).setEnabled(true);
            imageViews.get(11).setEnabled(true);
            imageViews.get(12).setEnabled(true);
            imageViews.get(13).setEnabled(true);
            checkEnd();
        }
        else if(tur == 7){
            imageViews.get(0).setEnabled(true);
            imageViews.get(1).setEnabled(true);
            imageViews.get(2).setEnabled(true);
            imageViews.get(3).setEnabled(true);
            imageViews.get(4).setEnabled(true);
            imageViews.get(5).setEnabled(true);
            imageViews.get(6).setEnabled(true);
            imageViews.get(7).setEnabled(true);
            imageViews.get(8).setEnabled(true);
            imageViews.get(9).setEnabled(true);
            imageViews.get(10).setEnabled(true);
            imageViews.get(11).setEnabled(true);
            imageViews.get(12).setEnabled(true);
            imageViews.get(13).setEnabled(true);
            imageViews.get(14).setEnabled(true);
            imageViews.get(15).setEnabled(true);
            checkEnd();
        }
    }

    private void checkEnd(){
        if(tur == 1){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.ipucu.setEnabled(false);
            }
        }
        else if(tur == 2){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE
                    && binding.kart6.getVisibility() == View.INVISIBLE && binding.kart7.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.ipucu.setEnabled(false);
            }
        }
        else if(tur == 3){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE
                    && binding.kart6.getVisibility() == View.INVISIBLE && binding.kart7.getVisibility() == View.INVISIBLE
                    && binding.kart2.getVisibility() == View.INVISIBLE && binding.kart3.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.ipucu.setEnabled(false);
            }
        }
        else if(tur == 4){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE
                    && binding.kart6.getVisibility() == View.INVISIBLE && binding.kart7.getVisibility() == View.INVISIBLE
                    && binding.kart2.getVisibility() == View.INVISIBLE && binding.kart3.getVisibility() == View.INVISIBLE
                    && binding.kart1.getVisibility() == View.INVISIBLE && binding.kart4.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.ipucu.setEnabled(false);
            }
        }
        else if(tur == 5){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE
                    && binding.kart6.getVisibility() == View.INVISIBLE && binding.kart7.getVisibility() == View.INVISIBLE
                    && binding.kart2.getVisibility() == View.INVISIBLE && binding.kart3.getVisibility() == View.INVISIBLE
                    && binding.kart1.getVisibility() == View.INVISIBLE && binding.kart4.getVisibility() == View.INVISIBLE
                    && binding.kart5.getVisibility() == View.INVISIBLE && binding.kart8.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.ipucu.setEnabled(false);
            }
        }
        else if(tur == 6){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE
                    && binding.kart6.getVisibility() == View.INVISIBLE && binding.kart7.getVisibility() == View.INVISIBLE
                    && binding.kart2.getVisibility() == View.INVISIBLE && binding.kart3.getVisibility() == View.INVISIBLE
                    && binding.kart1.getVisibility() == View.INVISIBLE && binding.kart4.getVisibility() == View.INVISIBLE
                    && binding.kart5.getVisibility() == View.INVISIBLE && binding.kart8.getVisibility() == View.INVISIBLE
                    && binding.kart9.getVisibility() == View.INVISIBLE && binding.kart12.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.ipucu.setEnabled(false);
            }
        }
        else if(tur == 7){
            if(binding.kart10.getVisibility() == View.INVISIBLE && binding.kart11.getVisibility() == View.INVISIBLE
                    && binding.kart14.getVisibility() == View.INVISIBLE && binding.kart15.getVisibility() == View.INVISIBLE
                    && binding.kart6.getVisibility() == View.INVISIBLE && binding.kart7.getVisibility() == View.INVISIBLE
                    && binding.kart2.getVisibility() == View.INVISIBLE && binding.kart3.getVisibility() == View.INVISIBLE
                    && binding.kart1.getVisibility() == View.INVISIBLE && binding.kart4.getVisibility() == View.INVISIBLE
                    && binding.kart5.getVisibility() == View.INVISIBLE && binding.kart8.getVisibility() == View.INVISIBLE
                    && binding.kart9.getVisibility() == View.INVISIBLE && binding.kart12.getVisibility() == View.INVISIBLE
                    && binding.kart13.getVisibility() == View.INVISIBLE && binding.kart16.getVisibility() == View.INVISIBLE){
                alkissesi.start();
                tur++;
                binding.kalanIpucu.setVisibility(View.INVISIBLE);
                binding.ipucu.setVisibility(View.INVISIBLE);
                binding.skor.setVisibility(View.INVISIBLE);
                binding.tebrikler.setVisibility(View.VISIBLE);
                binding.sonSkor.setVisibility(View.VISIBLE);
                binding.sonSkor.setText("Skorunuz: " + skor);
            }
        }
    }

    public void rastgele(){
        for (int i = 0; i < 23; i++) {
            sayilar.add(i);
        }
        Collections.shuffle(sayilar);
    }

    public void geriSayim() {
        new CountDownTimer(250, 1000) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {
                if(tur == 1){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                }
                else if(tur == 2){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                }
                else if(tur == 3){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                }
                else if(tur == 4){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                }
                else if(tur == 5){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                    imageViews.get(10).setImageResource(R.drawable.background2);
                    imageViews.get(11).setImageResource(R.drawable.background2);
                }
                else if(tur == 6){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                    imageViews.get(10).setImageResource(R.drawable.background2);
                    imageViews.get(11).setImageResource(R.drawable.background2);
                    imageViews.get(12).setImageResource(R.drawable.background2);
                    imageViews.get(13).setImageResource(R.drawable.background2);
                }
                else if(tur == 7){
                    imageViews.get(0).setImageResource(R.drawable.background2);
                    imageViews.get(1).setImageResource(R.drawable.background2);
                    imageViews.get(2).setImageResource(R.drawable.background2);
                    imageViews.get(3).setImageResource(R.drawable.background2);
                    imageViews.get(4).setImageResource(R.drawable.background2);
                    imageViews.get(5).setImageResource(R.drawable.background2);
                    imageViews.get(6).setImageResource(R.drawable.background2);
                    imageViews.get(7).setImageResource(R.drawable.background2);
                    imageViews.get(8).setImageResource(R.drawable.background2);
                    imageViews.get(9).setImageResource(R.drawable.background2);
                    imageViews.get(10).setImageResource(R.drawable.background2);
                    imageViews.get(11).setImageResource(R.drawable.background2);
                    imageViews.get(12).setImageResource(R.drawable.background2);
                    imageViews.get(13).setImageResource(R.drawable.background2);
                    imageViews.get(14).setImageResource(R.drawable.background2);
                    imageViews.get(15).setImageResource(R.drawable.background2);
                }
                /*if(ipucuSayisi <= 3){
                    ipucuSayisi--;
                    binding.kalanIpucu.setText("Kalan İpucu: " + ipucuSayisi);
                    if(ipucuSayisi == 0){
                        ipucuSayisi = 4;
                        binding.ipucu.setClickable(false);
                        binding.ipucu.startAnimation(animasyonGorunurluk);
                    }
                }*/
            }
        }.start();
    }
}
