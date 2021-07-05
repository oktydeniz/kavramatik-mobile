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
import com.kavramatik.kavramatik.databinding.HafizaKartiSayiBinding;
import com.kavramatik.kavramatik.databinding.HafizaKartiYonBinding;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.viewModel.DirectionViewModel;
import com.kavramatik.kavramatik.viewModel.NumberViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HafizaKartiYon extends Fragment {

    public HafizaKartiYonBinding binding;
    int[] cardsArray = {101, 102, 201, 202};
    //int image101, image102, image201, image202;
    int firstCard, secondCard;
    int clickedFirst, clickedSecond;
    int cardNumber = 1;
    public DirectionViewModel viewModel;
    ArrayList<Integer> sayilar = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    //int ipucuSayisi = 3;
    Animation animasyonGorunurluk;
    MediaPlayer alkissesi;
    int tur;
    int skor;
    Random rastgele = new Random();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HafizaKartiYonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DirectionViewModel.class);
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
        viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
            binding.ipucu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                    imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                    imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                    imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
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
                    viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
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
                    viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getDirectionImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
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
                    viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getDirectionImage());
                        Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getDirectionImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image5, 150, 150, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(8).setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                imageViews.get(9).setImageBitmap(Bitmap.createScaledBitmap(image5, 150, 150, false));
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
                    viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
                        Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
                        Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
                        Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
                        Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getDirectionImage());
                        Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getDirectionImage());
                        Bitmap image6 = Base64Util.decodeToBitmap(model.get(sayilar.get(5)).getDirectionImage());
                        binding.ipucu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imageViews.get(0).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(1).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(2).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(3).setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                imageViews.get(4).setImageBitmap(Bitmap.createScaledBitmap(image5, 150, 150, false));
                                imageViews.get(5).setImageBitmap(Bitmap.createScaledBitmap(image6, 150, 150, false));
                                imageViews.get(6).setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
                                imageViews.get(7).setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
                                imageViews.get(8).setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
                                imageViews.get(9).setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
                                imageViews.get(10).setImageBitmap(Bitmap.createScaledBitmap(image5, 150, 150, false));
                                imageViews.get(11).setImageBitmap(Bitmap.createScaledBitmap(image6, 150, 150, false));
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
        viewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
            Bitmap image1 = Base64Util.decodeToBitmap(model.get(sayilar.get(0)).getDirectionImage());
            Bitmap image2 = Base64Util.decodeToBitmap(model.get(sayilar.get(1)).getDirectionImage());
            Bitmap image3 = Base64Util.decodeToBitmap(model.get(sayilar.get(2)).getDirectionImage());
            Bitmap image4 = Base64Util.decodeToBitmap(model.get(sayilar.get(3)).getDirectionImage());
            Bitmap image5 = Base64Util.decodeToBitmap(model.get(sayilar.get(4)).getDirectionImage());
            Bitmap image6 = Base64Util.decodeToBitmap(model.get(sayilar.get(5)).getDirectionImage());
            if(cardsArray[card] == 101){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
            }
            else if(cardsArray[card] == 102){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
            }
            else if(cardsArray[card] == 103){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
            }
            else if(cardsArray[card] == 104){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
            }
            else if(cardsArray[card] == 105){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image5, 150, 150, false));
            }
            else if(cardsArray[card] == 106){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image6, 150, 150, false));
            }
            else if(cardsArray[card] == 201){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image1, 150, 150, false));
            }
            else if(cardsArray[card] == 202){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image2, 150, 150, false));
            }
            else if(cardsArray[card] == 203){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image3, 150, 150, false));
            }
            else if(cardsArray[card] == 204){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image4, 150, 150, false));
            }
            else if(cardsArray[card] == 205){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image5, 150, 150, false));
            }
            else if(cardsArray[card] == 206){
                iv.setImageBitmap(Bitmap.createScaledBitmap(image6, 150, 150, false));
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
            skor+=5;
            binding.skor.setText("Skorunuz: " + skor);
            Scores.updateScore(requireContext(), Scores.DIRECTION_SCORE);
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
        int sayi = rastgele.nextInt(2);
        if(sayi == 0){
            sayilar.add(0);
            sayilar.add(4);
            sayilar.add(8);
            sayilar.add(12);
            sayilar.add(16);
            sayilar.add(20);
            Collections.shuffle(sayilar);
        }
        if(sayi == 1){
            sayilar.add(2);
            sayilar.add(6);
            sayilar.add(10);
            sayilar.add(14);
            sayilar.add(18);
            sayilar.add(22);
            Collections.shuffle(sayilar);
        }
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
