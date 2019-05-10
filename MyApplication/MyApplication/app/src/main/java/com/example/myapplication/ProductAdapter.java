package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{


    private Context mCtx;



    private List<Product> productList;

    communicate communicator;

    public interface communicate{
        public void communicate(int i);
    }

    public void setCommunicator(communicate communicator) {
        this.communicator = communicator;
    }

    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    public void setData(List<Product> list){
        productList.clear();
        productList.addAll(list);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.itemPosition = position;
        holder.titletext.setText(product.short_title);
        holder.zipcode.setText("Zip:"+product.zipcode);
        holder.shipping.setText(product.shipping_cost);
        holder.condition.setText(product.condition);
        holder.price.setText("$"+product.price);
        holder.Itemid=product.itemid;
        holder.encoded = product.encoded;
        holder.title=product.title;
        holder.short_title=product.short_title;
        holder.ship_cost=product.shipping_cost;
        Picasso.with(holder.mCtx).load(product.imageurl).placeholder(R.drawable.junkimage)
                .error(R.drawable.junkimage).into(holder.imageurl);

        if(ApiCall.getLocalstorage().contains(product.itemid)){
            holder.wishbutton.setImageResource(R.drawable.cartremove);
        }
        else {
            holder.wishbutton.setImageResource(R.drawable.cartadd);
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        CardView cardProduct;

        TextView titletext,zipcode,shipping,condition,price;
        ImageView imageurl,wishbutton;
        Context mCtx;
        String Itemid,title,short_title,ship_cost,encoded;
        int itemPosition;
        public ProductViewHolder(View itemView) {
            super(itemView);

            titletext = itemView.findViewById(R.id.titletext);
            zipcode = itemView.findViewById(R.id.zipcode);
            shipping = itemView.findViewById(R.id.shipping);
            condition = itemView.findViewById(R.id.condition);
            price = itemView.findViewById(R.id.price);
            wishbutton=itemView.findViewById(R.id.wishbutton);
            imageurl = itemView.findViewById(R.id.imageurl);
            mCtx = itemView.getContext();
            cardProduct=itemView.findViewById(R.id.cardProduct);
            cardProduct.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, ProductInfo_Details.class);
                    intent.putExtra("itemid",Itemid );
                    intent.putExtra("title", title);
                    intent.putExtra("short_title", short_title);
                    intent.putExtra("shipcost", ship_cost);
                    intent.putExtra("encoded", encoded);
                    Activity activity = (Activity)v.getContext();
                    activity.startActivityForResult(intent,100);
                }
            });

            wishbutton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(ApiCall.getLocalstorage().contains(Itemid)){
                        ApiCall.getHandler().remove(Itemid);
                        ApiCall.getHandler().apply();
                        Toast.makeText(v.getContext(),short_title+" was removed from wish list",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ApiCall.getHandler().putString(Itemid,encoded);
                        ApiCall.getHandler().apply();
                        Toast.makeText(v.getContext(),short_title+" was added to wish list",Toast.LENGTH_SHORT).show();
                    }
                    communicator.communicate(itemPosition);
                }
            });
        }


    }
}
