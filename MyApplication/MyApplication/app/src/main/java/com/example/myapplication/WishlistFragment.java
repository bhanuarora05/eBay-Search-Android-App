package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishlistFragment extends Fragment implements ProductAdapter.communicate, LaunchActivity.launch{

    private int page;
    RecyclerView recyclerView;
    List<Product> productList;
    TextView no_records,count,cost;
    ProductAdapter adapter;
    JSONArray resultArray;

    public static WishlistFragment newInstance(int page) {
        WishlistFragment fragmentWishlist = new WishlistFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentWishlist.setArguments(args);
        return fragmentWishlist;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        LaunchActivity activity = (LaunchActivity) getActivity();
        activity.setLauncher(this);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        return view;
    }
    float price;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.wish_recycle);
        no_records=view.findViewById(R.id.textView63);
        count=view.findViewById(R.id.textView45);
        cost=view.findViewById(R.id.textView62);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        adapter = new ProductAdapter(getContext(), new ArrayList<Product>());

        adapter.setCommunicator(this);


        productList = new ArrayList<>();
        Map<String,?> local = ApiCall.getLocalstorage().getAll();
        price=0;
        for( Map.Entry<String,?> entry : local.entrySet()){
            Product result = new Product();
            result.decode((String) entry.getValue());
            productList.add(result);
            price += Float.parseFloat(result.price);
        }
        adapter.setData(productList);
        recyclerView.setAdapter(adapter);
        if(productList.size()==0){
            no_records.setVisibility(View.VISIBLE);
        }
        count.setText("Wishlist total("+productList.size()+" items):");
        cost.setText("$"+String.format("%.2f",price));
    }

    @Override
    public void communicate(int i) {
        price = price - Float.parseFloat(productList.get(i).price);
        productList.remove(i);
        adapter.setData(productList);
        adapter.notifyItemRemoved(i);
        adapter.notifyItemRangeChanged(i, productList.size());
        count.setText("Wishlist total("+productList.size()+" items):");
        cost.setText("$"+String.format("%.2f",price));
        if(productList.size()==0){
            no_records.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void launch() {
        no_records.setVisibility(View.GONE);
        productList = new ArrayList<>();
        Map<String,?> local = ApiCall.getLocalstorage().getAll();
        price=0;
        for( Map.Entry<String,?> entry : local.entrySet()){
            Product result = new Product();
            result.decode((String) entry.getValue());
            productList.add(result);
            price += Float.parseFloat(result.price);
        }
        adapter.setData(productList);
        adapter.notifyDataSetChanged();
        if(productList.size()==0){ no_records.setVisibility(View.VISIBLE); }
        count.setText("Wishlist total("+productList.size()+" items):");
        cost.setText("$"+String.format("%.2f",price));
    }
}
