package com.etechnie.anagh.adapters;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.fragment.Categories_6;
import com.etechnie.anagh.fragment.Products;
import com.etechnie.anagh.fragment.SubCategories_1;
import com.etechnie.anagh.models.category_model.CategoryDetails;
import com.etechnie.anagh.models.medical_type_model.MedicalTypeModel;

import java.util.List;


/**
 * CategoryListAdapter is the adapter class of RecyclerView holding List of Categories in MainCategories
 **/

public class MedicalTypeListAdapter extends RecyclerView.Adapter<MedicalTypeListAdapter.MyViewHolder> {

    boolean isSubCategory;

    Context context;
    List<MedicalTypeModel> categoriesList;


    public MedicalTypeListAdapter(Context context, List<MedicalTypeModel> categoriesList, boolean isSubCategory) {
        this.context = context;
        this.isSubCategory = isSubCategory;
        this.categoriesList = categoriesList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories_1, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on Position
        final MedicalTypeModel categoryDetails = categoriesList.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        // Set OrderProductCategory Image on ImageView with Glide Library
        Glide.with(context)
                .load(categoryDetails.getImage())
                .apply(options)
                .into(holder.category_image);


        holder.category_title.setText(categoryDetails.getName());
        holder.category_products.setText(categoryDetails.getDescription());

    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
        RelativeLayout category_card;
        ImageView category_image;
        TextView category_title, category_products;


        public MyViewHolder(final View itemView) {
            super(itemView);
            category_card = (RelativeLayout) itemView.findViewById(R.id.category_card);
            category_image = (ImageView) itemView.findViewById(R.id.category_image);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_products = (TextView) itemView.findViewById(R.id.category_products);

            category_card.setOnClickListener(this);
        }


        // Handle Click Listener on OrderProductCategory item
        @Override
        public void onClick(View view) {
            // Get OrderProductCategory Info
            Bundle categoryInfo = new Bundle();
            categoryInfo.putInt("CategoryID", categoriesList.get(getAdapterPosition()).getId());
            categoryInfo.putString("CategoryName", categoriesList.get(getAdapterPosition()).getName());
            categoryInfo.putString("data", categoriesList.get(getAdapterPosition()).getData());
            Fragment fragment;

            if (isSubCategory) {
                // Navigate to Products Fragment
                fragment = new Categories_6();
            } else {
                // Navigate to SubCategories Fragment
                fragment = new Categories_6();
            }

            fragment.setArguments(categoryInfo);
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        }
    }

}
