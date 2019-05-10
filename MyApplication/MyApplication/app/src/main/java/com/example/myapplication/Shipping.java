package com.example.myapplication;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.PorterDuff.Mode.SRC_IN;


/**
 * A simple {@link Fragment} subclass.
 */
public class Shipping extends Fragment implements ProductInfo_Details.checkCallBackShipping{

    View group7,row7,row3,row4,row5,row6,row8,row9,row10,row11,row12,row13,row14,group_soldby,group_shipping,group_return;
    TextView text_err,text_ship,text_storeurl,text_feedbackscore,text_globalshipping,text_handlingtime,text_condition,text_policy,text_returns,text_refund,text_shipped;
    CircularScoreView text_popularity;
    ImageView text_feedbackstar;
    Map<String,String> colors = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProductInfo_Details caller = (ProductInfo_Details )getActivity();
        caller.callbackcheckshipping(this);
        colors.put("None","#000000");
        colors.put("SilverShooting","#C0C0C0");
        colors.put("Red","#FF0000");
        colors.put("TurquoiseShooting","#40E0D0");
        colors.put("YellowShooting","FFFF00");
        colors.put("Purple","#800080");
        colors.put("Blue","#0000FF");
        colors.put("GreenShooting","#008000");
        colors.put("RedShooting","#FF0000");
        colors.put("Turquoise","#40E0D0");
        colors.put("Yellow","#FFFF00");
        colors.put("Green","#008000");
        colors.put("PurpleShooting","#800080");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group7=view.findViewById(R.id.group7);
        text_err=view.findViewById(R.id.textView20);
        row7=view.findViewById(R.id.row7);
        row3=view.findViewById(R.id.row3);
        row4=view.findViewById(R.id.row4);
        row5=view.findViewById(R.id.row5);
        row6=view.findViewById(R.id.row6);
        row8=view.findViewById(R.id.row8);
        row9=view.findViewById(R.id.row9);
        row10=view.findViewById(R.id.row10);
        row11=view.findViewById(R.id.row11);
        row12=view.findViewById(R.id.row12);
        row13=view.findViewById(R.id.row13);
        row14=view.findViewById(R.id.row14);
        text_ship=view.findViewById(R.id.textView47);
        text_storeurl=view.findViewById(R.id.textView39);
        text_feedbackscore=view.findViewById(R.id.textView41);
        text_popularity=view.findViewById(R.id.textView43);
        text_feedbackstar=view.findViewById(R.id.imageView6);
        text_globalshipping=view.findViewById(R.id.textView49);
        text_handlingtime=view.findViewById(R.id.textView51);
        text_condition=view.findViewById(R.id.textView53);
        text_policy=view.findViewById(R.id.textView55);
        text_returns=view.findViewById(R.id.textView57);
        text_refund=view.findViewById(R.id.textView59);
        text_shipped=view.findViewById(R.id.textView61);
        group_soldby=view.findViewById(R.id.group_soldby);
        group_shipping=view.findViewById(R.id.group_shipping);
        group_return=view.findViewById(R.id.group_return);
    }


    public void checkShippingChanged(JSONObject prodetails, String shipcost) {

        group7.setVisibility(View.GONE);

        try {
            if (prodetails==null || prodetails.length() == 0) {
                text_err.setVisibility(View.VISIBLE);
            } else if (prodetails.getString("Ack").equals("Failure")) {
                text_err.setVisibility(View.VISIBLE);

            } else {
                group_soldby.setVisibility(View.VISIBLE);
                group_shipping.setVisibility(View.VISIBLE);
                group_return.setVisibility(View.VISIBLE);

                boolean flag_soldby = false,flag_shipping = true,flag_return=true;
                JSONObject json_prod_data = prodetails.getJSONObject("Item");

                if(shipcost.equals("N/A")){
                    row7.setVisibility(View.GONE);
                }
                else if(shipcost.equals("Free Shipping")){
                    String ship = "Free Shipping";
                    text_ship.setText(ship);
                    flag_shipping=true;
                }
                else{
                    text_ship.setText(shipcost);
                    flag_shipping=true;

                }

                if(json_prod_data.has("Storefront")){
                    flag_soldby=true;
                    text_storeurl.setText(Html.fromHtml("<a href=\""+ json_prod_data.getJSONObject("Storefront").getString("StoreURL")+"\">"
                            + json_prod_data.getJSONObject("Storefront").getString("StoreName")+"</a>",Html.FROM_HTML_MODE_LEGACY));
                    text_storeurl.setMovementMethod(LinkMovementMethod.getInstance());

                }
                else{
                    row3.setVisibility(View.GONE);

                }

                if(json_prod_data.has("Seller")){

                    flag_soldby=true;

                    text_feedbackscore.setText(json_prod_data.getJSONObject("Seller").getString("FeedbackScore"));
                    text_popularity.setScore((int) Float.parseFloat(json_prod_data.getJSONObject("Seller").getString("PositiveFeedbackPercent")));

                    if(Integer.valueOf(json_prod_data.getJSONObject("Seller").getString("FeedbackScore"))<10000){
                        text_feedbackstar.setImageResource(R.drawable.starempty);
                    }
                    else{
                        text_feedbackstar.setImageResource(R.drawable.starfill);
                    }

                    text_feedbackstar.setImageTintList(ColorStateList.valueOf(
                            Color.parseColor(colors.get(json_prod_data.getJSONObject("Seller").getString("FeedbackRatingStar")))));
                    text_feedbackstar.setImageTintMode(SRC_IN);

                }
                else{
                    row4.setVisibility(View.GONE);
                    row5.setVisibility(View.GONE);
                    row6.setVisibility(View.GONE);

                }

                if(!flag_soldby) group_soldby.setVisibility(View.GONE);

                if(json_prod_data.has("GlobalShipping")){
                    flag_shipping=true;
                    if(json_prod_data.getString("GlobalShipping").equals("true")){
                        text_globalshipping.setText("Yes");
                    }
                    else{
                        text_globalshipping.setText("No");

                    }

                }
                else{
                    row8.setVisibility(View.GONE);

                }

                if(json_prod_data.has("HandlingTime")){
                    flag_shipping=true;
                    if(Integer.parseInt(json_prod_data.getString("HandlingTime"))==0 || Integer.parseInt(json_prod_data.getString("HandlingTime"))==1){
                        text_handlingtime.setText(json_prod_data.getString("HandlingTime")+" day");
                    }
                    else{
                        text_handlingtime.setText(json_prod_data.getString("HandlingTime")+" days");

                    }

                }
                else{
                    row9.setVisibility(View.GONE);

                }

                if(json_prod_data.has("ConditionDisplayName")){
                    flag_shipping=true;
                   text_condition.setText(json_prod_data.getString("ConditionDisplayName"));
                }
                else{
                    row10.setVisibility(View.GONE);

                }

                if(!flag_shipping){
                    group_shipping.setVisibility(View.GONE);
                }

                if(json_prod_data.has("ReturnPolicy")){
                    flag_return=true;
                    if(json_prod_data.getJSONObject("ReturnPolicy").has("ReturnsAccepted")) {text_policy.setText(json_prod_data.getJSONObject("ReturnPolicy").getString("ReturnsAccepted"));}
                    else{row11.setVisibility(View.GONE);}
                    if(json_prod_data.getJSONObject("ReturnPolicy").has("ReturnsWithin")) {text_returns.setText(json_prod_data.getJSONObject("ReturnPolicy").getString("ReturnsWithin"));}
                    else {row12.setVisibility(View.GONE);}
                    if(json_prod_data.getJSONObject("ReturnPolicy").has("Refund")) {text_refund.setText(json_prod_data.getJSONObject("ReturnPolicy").getString("Refund"));}
                    else {row13.setVisibility(View.GONE);}
                    if(json_prod_data.getJSONObject("ReturnPolicy").has("ShippingCostPaidBy")) {text_shipped.setText(json_prod_data.getJSONObject("ReturnPolicy").getString("ShippingCostPaidBy"));}
                    else {row14.setVisibility(View.GONE);}
                }
                else{
                    row11.setVisibility(View.GONE);
                    row12.setVisibility(View.GONE);
                    row13.setVisibility(View.GONE);
                    row14.setVisibility(View.GONE);

                }

                if(!flag_return){
                    group_return.setVisibility(View.GONE);
                }


            }

        }
            catch (Exception e) {
                e.printStackTrace();
                text_err.setVisibility(View.VISIBLE);
                group_return.setVisibility(View.GONE);
                group_shipping.setVisibility(View.GONE);
                group_soldby.setVisibility(View.GONE);
            }



        }





    public static Shipping newInstance(int page) {
        Shipping fragmentShipping = new Shipping();

        return fragmentShipping;
    }


    public Shipping() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }

}
