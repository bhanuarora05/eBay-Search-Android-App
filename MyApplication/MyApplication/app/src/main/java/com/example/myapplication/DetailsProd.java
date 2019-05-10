package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsProd extends AppCompatActivity implements ProductAdapter.communicate {
    RecyclerView recyclerView;
    List<Product> productList;
    TextView no_records,number,keywrd;
    View progressgroup,prod_details;
    ProductAdapter adapter;
    JSONArray resultArray;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_details_prod);
        no_records = findViewById(R.id.textView14);
        progressgroup = findViewById(R.id.group1);
        number = findViewById(R.id.textView11);
        keywrd = findViewById(R.id.textView13);
        prod_details = findViewById(R.id.group2);
        productList = new ArrayList<>();
        String category="all";
        Intent intent = getIntent();
        progressgroup.setVisibility(View.VISIBLE);

        final String keyword = intent.getStringExtra("keyword");
        int cat = intent.getIntExtra("category",0);
        if(cat==0){
            category="all";
        }
        else if(cat==1){
            category="550";

        }
        else if(cat==2){
            category="2984";

        }
        else if(cat==3){
            category="267";

        }
        else if(cat==4){
            category="11450";

        }
        else if(cat==5){
            category="58058";

        }
        else if(cat==6){
            category="26395";

        }
        else if(cat==7){
            category="11233";

        }
        else if(cat==8){
            category="1249";

        }

        String otherdist = intent.getStringExtra("otherdist");
        String ziphere = intent.getStringExtra("ziphere");
        String inputdist = intent.getStringExtra("milesfrom");
        String freeshipping = intent.getStringExtra("freeshipping");
        String local = intent.getStringExtra("localpickup");
        String New  = intent.getStringExtra("new");
        String used  = intent.getStringExtra("used");
        String unspecified  = intent.getStringExtra("unspecified");
        String enablenearby = intent.getStringExtra("nearbyshipping");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        adapter = new ProductAdapter(this, new ArrayList<Product>());

        adapter.setCommunicator(this);

        recyclerView.setAdapter(adapter);


        ApiCall.ebayresults(this,keyword,category,otherdist,ziphere,inputdist,freeshipping,local,New,used,unspecified,enablenearby,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    if (!responseObject.has("findItemsAdvancedResponse")) {
                        no_records.setVisibility(View.VISIBLE);
                        prod_details.setVisibility(View.GONE);
                        progressgroup.setVisibility(View.GONE);
                        return;
                    } else if (responseObject.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("ack").getString(0).equals("Failure")) {

                        no_records.setVisibility(View.VISIBLE);
                        prod_details.setVisibility(View.GONE);
                        progressgroup.setVisibility(View.GONE);
                        return;
                    } else if (responseObject.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getString("@count").equals("0"))
                    {
                    no_records.setVisibility(View.VISIBLE);
                    prod_details.setVisibility(View.GONE);
                        progressgroup.setVisibility(View.GONE);
                    return;}


                    else{ resultArray = responseObject.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
                        for(int i=0;i<resultArray.length();i++){
                            Product result = new Product();
                            if(!resultArray.getJSONObject(i).has("galleryURL")){
                                result.setImageurl("N/A");
                            }
                            else{ result.setImageurl(resultArray.getJSONObject(i).getJSONArray("galleryURL").getString(0));}
                            if(!resultArray.getJSONObject(i).has("title")){
                                result.setTitle("N/A");
                            }
                            else{
                                if(resultArray.getJSONObject(i).getJSONArray("title").getString(0).length()>35){
                                    result.setTitle(resultArray.getJSONObject(i).getJSONArray("title").getString(0));
                                    String shortitle = result.title.substring(0,35);
                                    int last=shortitle.lastIndexOf(' ');
                                    shortitle=shortitle.substring(0,last+1);
                                    shortitle=shortitle+"...";
                                    result.setShort_title(shortitle);

                                }
                                else{
                                    result.setTitle(resultArray.getJSONObject(i).getJSONArray("title").getString(0));
                                    result.setShort_title(resultArray.getJSONObject(i).getJSONArray("title").getString(0));

                                }
                            }
                            if(!resultArray.getJSONObject(i).has("itemId")){
                                result.setItemid("N/A");
                            }
                            else{
                                result.setItemid(resultArray.getJSONObject(i).getJSONArray("itemId").getString(0));
                            }



                            if(!resultArray.getJSONObject(i).has("sellingStatus")){
                                result.setPrice("N/A");
                            }
                            else{
                                result.setPrice(resultArray.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));
                            }
                            if(!resultArray.getJSONObject(i).has("postalCode")){
                                result.setZipcode("N/A");
                            }
                            else{
                                result.setZipcode(resultArray.getJSONObject(i).getJSONArray("postalCode").getString(0));
                            }
                            if(!resultArray.getJSONObject(i).has("shippingInfo")){
                                result.setShipping_cost("N/A");
                            }
                            else if(resultArray.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost")){
                                if(resultArray.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__").equals("0.0")){
                                    result.setShipping_cost("Free Shipping");
                                }
                                else{
                                    result.setShipping_cost("$"+resultArray.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__"));
                                }

                            }
                            else{
                                result.setShipping_cost("N/A");
                            }

                            if(!resultArray.getJSONObject(i).has("condition")){
                                result.setCondition("N/A");
                            }
                            else{
                                result.setCondition(resultArray.getJSONObject(i).getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0));
                            }


                            //console.log(result);
                            result.encode();
                            productList.add(result);
                        }

                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                    progressgroup.setVisibility(View.GONE);
                    no_records.setVisibility(View.VISIBLE);
                    prod_details.setVisibility(View.GONE);

                    return;
                }

                adapter.setData(productList);
                keywrd.setText(keyword);
                number.setText(String.valueOf(productList.size()));

                adapter.notifyDataSetChanged();
                progressgroup.setVisibility(View.GONE);
                prod_details.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressgroup.setVisibility(View.GONE);
                no_records.setVisibility(View.VISIBLE);
                prod_details.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void communicate(int i) {
        adapter.notifyItemChanged(i);
    }
}
