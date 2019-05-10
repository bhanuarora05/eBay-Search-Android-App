package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ProductInfo_Details extends AppCompatActivity {

    JSONObject prodetails,simdetails,prodimages;
    String shipcost;
    String fburl;
    FloatingActionButton wishButton;

    public interface checkCallBackProduct{
        void checkProductChanged(JSONObject response,String shipcost);}
    public interface checkCallBackPhotos{void checkPhotosChanged(JSONObject response);}
    public interface checkCallBackShipping{void checkShippingChanged(JSONObject response,String shipcost);}
    public interface checkCallBackSimilar{void checkSimilarChanged(JSONObject response);}

    checkCallBackProduct callbackprod ;
    checkCallBackPhotos callbackphotos;
    checkCallBackShipping callbackshipping;
    checkCallBackSimilar callbacksimilar;

    public void callbackcheckproduct(checkCallBackProduct caller)
    {
        callbackprod = caller;
    }

    public void callbackcheckphotos(checkCallBackPhotos caller){ callbackphotos = caller;}

    public void callbackcheckshipping(checkCallBackShipping caller){callbackshipping=caller ;}

    public void callbackchecksimilar(checkCallBackSimilar caller){callbacksimilar=caller ;}

    String productId,encoded,short_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        productId = intent.getStringExtra("itemid");
        shipcost =  intent.getStringExtra("shipcost");
        short_title =  intent.getStringExtra("short_title");
        encoded =  intent.getStringExtra("encoded");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_product_info__details);
        ViewPager prodPager = (ViewPager) findViewById(R.id.prod_pager);
        wishButton = findViewById(R.id.floatingActionButton);
        prodPager.setOffscreenPageLimit(4);
        ProdPagerAdapter adapterViewPager = new ProdPagerAdapter(getSupportFragmentManager());
        prodPager.setAdapter(adapterViewPager);
        TabLayout vp_tab = (TabLayout) findViewById(R.id.prod_tab);
        vp_tab.setupWithViewPager(prodPager);
        vp_tab.getTabAt(0).setIcon(R.drawable.product);
        vp_tab.getTabAt(1).setIcon(R.drawable.shipping);
        vp_tab.getTabAt(2).setIcon(R.drawable.photos);
        vp_tab.getTabAt(3).setIcon(R.drawable.similar);
        fburl = "";

        if(ApiCall.getLocalstorage().contains(productId)){
            wishButton.setImageResource(R.drawable.cartremove2);
        }
        else{
            wishButton.setImageResource(R.drawable.cartadd2);
        }

        wishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApiCall.getLocalstorage().contains(productId)){
                    ApiCall.getHandler().remove(productId);
                    ApiCall.getHandler().apply();
                    wishButton.setImageResource(R.drawable.cartadd2);
                    Toast.makeText(v.getContext(),short_title+" was removed from wish list",Toast.LENGTH_SHORT).show();
                }
                else{
                    ApiCall.getHandler().putString(productId,encoded);
                    ApiCall.getHandler().apply();
                    wishButton.setImageResource(R.drawable.cartremove2);
                    Toast.makeText(v.getContext(),short_title+" was added to wish list",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ApiCall.fetchprodetails(this, title,productId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    prodetails= responseObject.getJSONObject("prodetails");
                    simdetails= responseObject.getJSONObject("simdetails");
                    prodimages= responseObject.getJSONObject("prodimages");
                    callbackprod.checkProductChanged(prodetails,shipcost);
                    callbackphotos.checkPhotosChanged(prodimages);
                    callbackshipping.checkShippingChanged(prodetails,shipcost);
                    callbacksimilar.checkSimilarChanged(simdetails);
                    if(!prodetails.has("Ack") || prodetails.getString("Ack").equals("Failure")){
                    }
                    else{
                        fburl = "https://www.facebook.com/dialog/share?app_id=2280722032022712&display=popup&href="
                                + URLEncoder.encode(prodetails.getJSONObject("Item").getString("ViewItemURLForNaturalSearch"),
                                "UTF-8") + "&quote=" + URLEncoder.encode("Buy "
                                +prodetails.getJSONObject("Item").getString("Title") +" at $"
                                +prodetails.getJSONObject("Item").getJSONObject("CurrentPrice").getDouble("Value")
                                +" from link below", "UTF-8")+"&hashtag=%23CSCI571Spring2019Ebay";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callbackprod.checkProductChanged(null,shipcost);
                    callbackphotos.checkPhotosChanged(null);
                    callbackshipping.checkShippingChanged(null,shipcost);
                    callbacksimilar.checkSimilarChanged(null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callbackprod.checkProductChanged(null,shipcost);
                callbackphotos.checkPhotosChanged(null);
                callbackshipping.checkShippingChanged(null,shipcost);
                callbacksimilar.checkSimilarChanged(null);
            }
        });

    }

    public static class ProdPagerAdapter extends FragmentPagerAdapter {

        public ProdPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }



        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Product_Tab.newInstance(0);
                case 1:
                    return Shipping.newInstance(0);
                case 2:
                    return Photos.newInstance(0);
                case 3:
                    return Similar.newInstance(0);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0)
                return "PRODUCT";

            else if(position==1) return "SHIPPING";

            else if(position==2) return "PHOTOS";

            return "SIMILAR";

        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.icFacebook && !fburl.equals("")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fburl));
            startActivity(browserIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fb_icon, menu);
        return true;
    }


}
