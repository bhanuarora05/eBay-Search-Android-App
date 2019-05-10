package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ModelSimilarAdapter extends RecyclerView.Adapter<ModelSimilarAdapter.ProductViewHolder>{

    private Context mCtx;



    private List<Similar.ModelSimilar> productList;


    public ModelSimilarAdapter(Context mCtx, List<Similar.ModelSimilar> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    public void setData(List<Similar.ModelSimilar> list){
        productList.clear();
        productList.addAll(list);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products2, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Similar.ModelSimilar similarModel = this.productList.get(position);
        holder.similarTitleText.setText(similarModel.title);
        if(Float.parseFloat(similarModel.shipping_cost)== 0.0){
            holder.similarShippingText.setText("Free Shipping");
        }
        else{
            holder.similarShippingText.setText("$"+similarModel.shipping_cost);
        }

        holder.similarDaysText.setText(similarModel.days+" Days Left");
        holder.similarPriceText.setText("$"+String.format ("%.2f", similarModel.price));
        Picasso.with(holder.mctx).load(similarModel.image).placeholder(R.drawable.junkimage)
                .error(R.drawable.junkimage).into(holder.similarImageView);
        holder.url = similarModel.url;
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView similarImageView;
        TextView similarTitleText,similarShippingText,similarDaysText,similarPriceText;
        String url;
        Context mctx;
        public ProductViewHolder(View itemView) {
            super(itemView);

            similarImageView = itemView.findViewById(R.id.similarImg);
            similarTitleText = itemView.findViewById(R.id.similarTitleText);
            similarShippingText = itemView.findViewById(R.id.similarShippingText);
            similarDaysText = itemView.findViewById(R.id.similarDaysText);
            similarPriceText = itemView.findViewById(R.id.similarPriceText);
            mctx = itemView.getContext();
            itemView.findViewById(R.id.similarCard).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    Activity activity = (Activity) v.getContext();
                    activity.startActivity(browserIntent);
                }
            });
        }


    }
}
