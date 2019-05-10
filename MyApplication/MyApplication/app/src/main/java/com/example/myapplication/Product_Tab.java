package com.example.myapplication;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Product_Tab extends Fragment implements ProductInfo_Details.checkCallBackProduct {

    View group5,row1,row2,row3,groupspec,grouphighlights,group_upper;
    TextView text_err,text_title,text_price,text_ship,text_currentprice,text_subtitle,text_brand,text_spec;
    LinearLayout prod_images;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProductInfo_Details caller = (ProductInfo_Details )getActivity();
        caller.callbackcheckproduct(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group5 =  view.findViewById(R.id.group5);
        text_err = view.findViewById(R.id.textView16);
        text_title = view.findViewById(R.id.textView23);
        text_price = view.findViewById(R.id.textView25);
        text_ship = view.findViewById(R.id.textView27);
        text_currentprice=view.findViewById(R.id.textView35);
        text_subtitle=view.findViewById(R.id.textView33);
        text_spec=view.findViewById(R.id.textView31);
        text_brand=view.findViewById(R.id.textView37);
        row1=view.findViewById(R.id.row1);
        row2=view.findViewById(R.id.row2);
        row3=view.findViewById(R.id.row3);
        groupspec=view.findViewById(R.id.groupspec);
        grouphighlights=view.findViewById(R.id.grouphighlights);
        prod_images=view.findViewById(R.id.prod_images);
        group_upper=view.findViewById(R.id.group_upper);
    }

    public void checkProductChanged(JSONObject prodetails, String shipcost) {



        group5.setVisibility(View.GONE);

        try {
            if(prodetails==null || prodetails.length()==0){
                text_err.setVisibility(View.VISIBLE);
            }

            else if(prodetails.getString("Ack").equals("Failure")){
                text_err.setVisibility(View.VISIBLE);

            }
            else{
                grouphighlights.setVisibility(View.VISIBLE);
                groupspec.setVisibility(View.VISIBLE);
                group_upper.setVisibility(View.VISIBLE);


                boolean flag=false;
                JSONObject json_prod_data = prodetails.getJSONObject("Item");
                if(json_prod_data.has("Title")){

                    String title= json_prod_data.getString("Title");

                    text_title.setText(title);

                }
                else{text_title.setVisibility(View.GONE);}

                if(json_prod_data.has("CurrentPrice")){

                    String price= json_prod_data.getJSONObject("CurrentPrice").getString("Value");

                    text_price.setText("$"+price);
                    text_currentprice.setText("$"+price);
                    flag=true;

                }
                else{text_price.setVisibility(View.GONE);
                row2.setVisibility(View.GONE);}

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.setMargins(0,0,0,56);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                JSONArray pictureURL = json_prod_data.getJSONArray("PictureURL");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1050, 1050);
                layoutParams.setMargins(105, 0, 0, 0);
                for(int i=0;i<pictureURL.length();i++){
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.with(getContext()).load(pictureURL.getString(i)).placeholder(R.drawable.junkimage)
                            .error(R.drawable.junkimage).into(imageView);
                    prod_images.addView(imageView);
                }

                if(json_prod_data.has("Subtitle")){

                    String subtitle= json_prod_data.getString("Subtitle");

                    text_subtitle.setText(subtitle);

                    flag=true;

                }
                else{row1.setVisibility(View.GONE);
                 }

                if(shipcost.equals("N/A")){
                    text_ship.setVisibility(View.GONE);
                }
                else if(shipcost.equals("Free Shipping")){
                    String ship = "With Free Shipping";
                    text_ship.setText(ship);
                }
                else{
                    String ship = "With "+shipcost+" Shipping";
                    text_ship.setText(ship);

                }

                if(json_prod_data.has("ItemSpecifics")){
                    JSONArray result = json_prod_data.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
                    StringBuilder string = new StringBuilder();
                    String string_brand = "";
                    for (int i = 0; i < result.length(); i++) {

                        if(result.getJSONObject(i).getString("Name").equals("Brand")){
                            text_brand.setText(result.getJSONObject(i).getJSONArray("Value").getString(0));
                            string_brand= ("&#8226;"+result.getJSONObject(i).getJSONArray("Value").getString(0).substring(0,1).toUpperCase()+result.getJSONObject(i).getJSONArray("Value").getString(0).substring(1)+"<br/>");
                            continue;
                        }
                        JSONArray temp = result.getJSONObject(i).getJSONArray("Value");

                        for(int j=0;j<temp.length();j++){
                            String str = temp.getString(j);
                            string.append("&#8226;"+str.substring(0,1).toUpperCase()+str.substring(1)+"<br/>");
                        }
                    }
                    text_spec.setText(Html.fromHtml(string_brand+string.toString(),Html.FROM_HTML_MODE_LEGACY));

                }

                else{
                    groupspec.setVisibility(View.GONE);
                }

                if(flag==false){
                    grouphighlights.setVisibility(View.GONE);
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
            grouphighlights.setVisibility(View.GONE);
            groupspec.setVisibility(View.GONE);
            group_upper.setVisibility(View.GONE);
            text_err.setVisibility(View.VISIBLE);
        }



    }

    public static Product_Tab newInstance(int page) {
        Product_Tab fragmentProd = new Product_Tab();

        return fragmentProd;
    }


    public Product_Tab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product__tab, container, false);
    }

}
