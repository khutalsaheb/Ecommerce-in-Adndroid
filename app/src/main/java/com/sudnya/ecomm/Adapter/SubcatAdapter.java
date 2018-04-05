package com.sudnya.ecomm.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sudnya.ecomm.Activity.SubCatagoryActivity;
import com.sudnya.ecomm.Model.HomeModel;
import com.sudnya.ecomm.R;

import java.util.List;

public class SubcatAdapter extends RecyclerView.Adapter<SubcatAdapter.ViewHolder> {
    private Context context;
    private List<HomeModel> homeModelList;

    public SubcatAdapter(List<HomeModel> homeModelList, Context context) {
        super();
        this.context = context;
        this.homeModelList = homeModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        final HomeModel homeModel = homeModelList.get(position);

        Viewholder.NameView.setText(homeModel.getName());
        Viewholder.dprice.setText("Rs." + homeModel.getProductPrice());
        Viewholder.price.setText("Rs." + homeModel.getProductPriceBeforeDiscount());
        Viewholder.offer.setText(homeModel.getOffers());
        Picasso.with(context.getApplicationContext()).load(homeModel.getProductImage1()).resize(100, 100).into(Viewholder.imageView);
        Viewholder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubCatagoryActivity.getInstance().addtoWishlist(homeModel);

            }
        });
        Viewholder.setClickListener(new ItemClickListener() {
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    SubCatagoryActivity.getInstance().addNewModules(homeModel);
                } else {
                    SubCatagoryActivity.getInstance().addNewModules(homeModel);
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return homeModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView NameView, dprice, price, offer;
        ImageView imageView, wishlist;
        private ItemClickListener clickListener;

        ViewHolder(View itemView) {
            super(itemView);
            NameView = (TextView) itemView.findViewById(R.id.name);
            dprice = (TextView) itemView.findViewById(R.id.dprice);
            price = (TextView) itemView.findViewById(R.id.price);
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            offer = (TextView) itemView.findViewById(R.id.offer);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            wishlist = (ImageView) itemView.findViewById(R.id.wishlist);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);

            return true;
        }
    }

    private static class ItemClickListener {
        public void onClick(View view, int position, boolean b) {
        }
    }
}