package com.sudnya.ecomm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sudnya.ecomm.Model.Todays_Offer_Model;
import com.sudnya.ecomm.R;

import java.util.List;

/**
 * @ Created by Dell on 07-Sep-17.
 */

public class Todays_Offer_Adapter extends RecyclerView.Adapter<Todays_Offer_Adapter.ViewHolder> {

    //List of todays_offer_models
    private List<Todays_Offer_Model> todays_offer_models;
    private Context context;

    public Todays_Offer_Adapter(List<Todays_Offer_Model> todays_offer_models, Context context) {
        super();
        //Getting all the todays_offer_models
        this.todays_offer_models = todays_offer_models;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mainpage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Todays_Offer_Model todaysoffers = todays_offer_models.get(position);
        holder.textViewName.setText(todaysoffers.getProductName());
        holder.textViewRank.setText(todaysoffers.getProductPrice());
        holder.textViewRealName.setText(todaysoffers.getSubCategory());
        Picasso.with(context.getApplicationContext()).load(todaysoffers.getProductImage1()).resize(100, 100).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return todays_offer_models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewRank;
        TextView textViewRealName;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.productimage);
            textViewName = (TextView) itemView.findViewById(R.id.name);
            textViewRank = (TextView) itemView.findViewById(R.id.price);
            textViewRealName = (TextView) itemView.findViewById(R.id.color);
        }
    }
}