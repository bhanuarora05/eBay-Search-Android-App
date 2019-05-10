package com.example.myapplication;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Photos extends Fragment implements ProductInfo_Details.checkCallBackPhotos{
    TextView text_err;
    LinearLayout photo_display;
    View group6;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProductInfo_Details caller = (ProductInfo_Details )getActivity();
        caller.callbackcheckphotos(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text_err=view.findViewById(R.id.textView19);
        photo_display=view.findViewById(R.id.photo_display);
        group6=view.findViewById(R.id.group6);
    }

    @Override
    public void checkPhotosChanged(JSONObject prodimages) {

        group6.setVisibility(View.GONE);

        try {
            if (prodimages==null || prodimages.length() == 0) {
                text_err.setVisibility(View.VISIBLE);
            } else if (!prodimages.has("items")) {
                text_err.setVisibility(View.VISIBLE);

            } else {
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.setMargins(0,0,0,56);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                for(int i=0;i<prodimages.getJSONArray("items").length();i++){
                    String image=prodimages.getJSONArray("items").getJSONObject(i).getString("link");
                    CardView card = new CardView(getContext());
                    card.setLayoutParams(params1);
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(params2);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.with(getContext()).load(image).placeholder(R.drawable.junkimage).error(R.drawable.junkimage).into(imageView);
                    card.addView(imageView);
                    photo_display.addView(card);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            text_err.setVisibility(View.VISIBLE);
        }
    }

    public static Photos newInstance(int page) {
        Photos fragmentPhotos = new Photos();

        return fragmentPhotos;
    }


    public Photos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

}
