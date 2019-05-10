package com.example.myapplication;


import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Similar extends Fragment implements ProductInfo_Details.checkCallBackSimilar{

    Spinner spinner_criteria,spinner_order;
    RecyclerView similar_recycle;
    ModelSimilarAdapter adapter;
    TextView text_err;
    View group8;
    List<ModelSimilar> productList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProductInfo_Details caller = (ProductInfo_Details )getActivity();
        caller.callbackchecksimilar(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner_criteria = (Spinner) view.findViewById(R.id.spinner2);
        spinner_order = (Spinner) view.findViewById(R.id.spinner3);
        similar_recycle=view.findViewById(R.id.similar_recycle);
        group8=view.findViewById(R.id.group8);
        text_err=view.findViewById(R.id.textView22);
        productList = new ArrayList<>();

        similar_recycle.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ModelSimilarAdapter(getContext(), new ArrayList<ModelSimilar>());

        similar_recycle.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinner2Adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item);
        spinner2Adapter.add("Default");
        spinner2Adapter.add("Name");
        spinner2Adapter.add("Price");
        spinner2Adapter.add("Days");

        spinner_criteria.setAdapter(spinner2Adapter);

        spinner_criteria.setEnabled(false);
        spinner_criteria.setClickable(false);

        ArrayAdapter<CharSequence> spinner3Adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item);
        spinner3Adapter.add("Ascending");
        spinner3Adapter.add("Descending");

        spinner_order.setAdapter(spinner3Adapter);
        spinner_order.setEnabled(false);
        spinner_order.setClickable(false);

        spinner_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
                update_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_criteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    spinner_order.setEnabled(false);
                    spinner_order.setClickable(false);
                }
                else{
                    spinner_order.setEnabled(true);
                    spinner_order.setClickable(true);
                }
                update_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public  void  update_list(){
        if(spinner_criteria.getSelectedItemPosition()==0){
            Collections.sort(productList, new Comparator<ModelSimilar>() {
                @Override
                public int compare(ModelSimilar o1, ModelSimilar o2) {
                    return o1.index-o2.index;
                }
            });
        }
        else if(spinner_criteria.getSelectedItemPosition()==1){
            Collections.sort(productList, new Comparator<ModelSimilar>() {
                @Override
                public int compare(ModelSimilar o1, ModelSimilar o2) {
                    return (spinner_order.getSelectedItemPosition()==0?1:-1)*o1.title.compareTo(o2.title);
                }
            });
        }
        else if(spinner_criteria.getSelectedItemPosition()==2){
            Collections.sort(productList, new Comparator<ModelSimilar>() {
                @Override
                public int compare(ModelSimilar o1, ModelSimilar o2) {
                    return (spinner_order.getSelectedItemPosition()==0?1:-1)*(int)(o1.price - o2.price);
                }
            });
        }
        else{
            Collections.sort(productList, new Comparator<ModelSimilar>() {
                @Override
                public int compare(ModelSimilar o1, ModelSimilar o2) {
                    return (spinner_order.getSelectedItemPosition()==0?1:-1)*(o1.days-o2.days);
                }
            });
        }
        adapter.setData(productList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void checkSimilarChanged(JSONObject simdetails) {
        group8.setVisibility(View.GONE);
        try {
            if(simdetails==null || simdetails.length()==0){
                text_err.setVisibility(View.VISIBLE);}

            else if(simdetails.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item").length()==0){
                text_err.setVisibility(View.VISIBLE);
            }

            else{

                JSONArray json_sim_data = simdetails.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item");
                for(int i=0;i<json_sim_data.length();i++){
                    ModelSimilar obj = new ModelSimilar();
                    JSONObject line=json_sim_data.getJSONObject(i);

                    obj.index = i;
                    if(line.has("imageURL")){obj.image=line.getString("imageURL");}
                    else{obj.image="http://thumbs1.ebaystatic.com/pict/04040_0.jpg";}

                    if(line.has("title")){obj.title=line.getString("title");}
                    else{obj.title="N/A";}

                    if(line.has("buyItNowPrice")){obj.price= Float.parseFloat(line.getJSONObject("buyItNowPrice").getString("__value__"));}
                    else{obj.price = (float)0.0;}
                    if(line.has("shippingCost")){obj.shipping_cost= line.getJSONObject("shippingCost").getString("__value__");}
                    else{obj.shipping_cost="N/A";}

                    if(line.has("timeLeft")){obj.days=Integer.parseInt(line.getString("timeLeft").substring(line.getString("timeLeft").indexOf('P')+1,line.getString("timeLeft").indexOf('D')));}
                    else{obj.days=0;}

                    if(line.has("viewItemURL")){obj.url=line.getString("viewItemURL");}
                    else{obj.url="";}
                    productList.add(obj);

                }
                adapter.setData(productList);
                adapter.notifyDataSetChanged();
                spinner_criteria.setEnabled(true);
                spinner_criteria.setClickable(true);
            }
        }
        catch (Exception e){
            text_err.setVisibility(View.VISIBLE);
        }
    }

    public static Similar newInstance(int page) {
        Similar fragmentSimilar = new Similar();

        return  fragmentSimilar;
    }


    public Similar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_similar, container, false);
    }

    class ModelSimilar{
        public String image, title, shipping_cost,url;
        public Integer days,index;
        public Float price;

        public void SimilarModel(){
        }
    }

}
