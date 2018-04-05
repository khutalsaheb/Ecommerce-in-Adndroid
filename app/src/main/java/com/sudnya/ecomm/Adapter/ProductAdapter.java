package com.sudnya.ecomm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sudnya.ecomm.Activity.HomeScreenActivity;
import com.sudnya.ecomm.Model.HomeModel;
import com.sudnya.ecomm.R;

import java.util.List;

/**
 * @ Created by Dell on 8/9/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public static final String KEY_NAME = "Name";
    public static final String KEY_ID = "Id";
    private Context context;
    private List<HomeModel> homeModelList;

    public ProductAdapter(Context context, List<HomeModel> objects) {
        super();
        this.context = context;
        this.homeModelList = objects;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        final HomeModel homeModel = homeModelList.get(position);
        Viewholder.NameView.setText(homeModel.getName());
        Viewholder.setClickListener(new ItemClickListener() {

            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    HomeScreenActivity.getInstance().addNewModule(homeModel);
                } else {
                    HomeScreenActivity.getInstance().addNewModule(homeModel);
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return homeModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView NameView;
        //  ImageView ImageView;
        private ItemClickListener clickListener;

        ViewHolder(View itemView) {

            super(itemView);

            NameView = (TextView) itemView.findViewById(R.id.name);
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