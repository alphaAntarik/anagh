package com.etechnie.anagh.adapters;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.etechnie.anagh.R;

import java.util.List;

import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.customs.CircularImageView;
import com.etechnie.anagh.fragment.FragmentDoctor;
import com.etechnie.anagh.models.medical_problem_model.MedicalProblemModel;


/**
 * CategoryListAdapter is the adapter class of RecyclerView holding List of Categories in MainCategories
 **/

public class CategoryListAdapter_6 extends RecyclerView.Adapter<CategoryListAdapter_6.MyViewHolder> {

    boolean isSubCategory;

    Context context;
    List<MedicalProblemModel> categoriesList;
    //List<CategoryDetails> allCategoriesList;


    public CategoryListAdapter_6(Context context, List<MedicalProblemModel> categoriesList, boolean isSubCategory) {
        this.context = context;
        this.isSubCategory = isSubCategory;
        this.categoriesList = categoriesList;

       // allCategoriesList = ((App) getContext().getApplicationContext()).getCategoriesList();
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_categories_6, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on Position
        final MedicalProblemModel categoryDetails = categoriesList.get(position);

        holder.category_title.setText(categoryDetails.getName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(categoryDetails.getImage())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle categoryInfo = new Bundle();
                categoryInfo.putInt("CategoryID", categoryDetails.getId());

                Fragment fragment;

                if (isSubCategory) {
                    // Navigate to Products Fragment
                    fragment = new FragmentDoctor();
                } else {
                    // Navigate to SubCategories Fragment
                    fragment = new FragmentDoctor();
                }

                fragment.setArguments(categoryInfo);
                FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
            }
        });

//        List<CategoryDetails> subCategoriesList = new ArrayList<>();
//
//        for (int i=0;  i<allCategoriesList.size();  i++) {
//            // Get Subcategories from all Categories
//            if (allCategoriesList.get(i).getParentId().equalsIgnoreCase(categoryDetails.getId())) {
//                subCategoriesList.add(allCategoriesList.get(i));
//            }
//        }
//
//        // Initialize the SubCategoryListAdapter for RecyclerView
//        SubCategoryListAdapter subCategoryListAdapter = new SubCategoryListAdapter(context, subCategoriesList);
//
//        holder.sub_categories_list.setAdapter(subCategoryListAdapter);



    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ListView sub_categories_list;
        TextView category_title;
        CircularImageView imageView;


        public MyViewHolder(final View itemView) {
            super(itemView);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            sub_categories_list = (ListView) itemView.findViewById(R.id.sub_categories_list);
            imageView = itemView.findViewById(R.id.category_image);
        }

    }

}

