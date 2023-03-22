package com.etechnie.anagh.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.etechnie.anagh.CustomerFragment.FragmentAmbulance;
import com.etechnie.anagh.CustomerFragment.FragmentHospital;
import com.etechnie.anagh.CustomerFragment.FragmentPathology;
import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.AddressActivity;
import com.etechnie.anagh.activities.Login;
import com.etechnie.anagh.activities.LoginActivity;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.app.MyAppPrefsManager;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.fragment.Categories_1;
import com.etechnie.anagh.fragment.PrescriptionFragment;
import com.etechnie.anagh.fragment.SubCategories_1;
import com.etechnie.anagh.models.service_model.ServiceModel;

import java.util.List;


/**
 * CategoryListAdapter is the adapter class of RecyclerView holding List of Categories in MainCategories
 **/

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    boolean isSubCategory;

    Context context;
    List<ServiceModel> categoriesList;
    MyAppPrefsManager myAppPrefsManager;

    public ServiceListAdapter(Context context, List<ServiceModel> categoriesList, boolean isSubCategory) {
        this.context = context;
        this.isSubCategory = isSubCategory;
        this.categoriesList = categoriesList;
        this.myAppPrefsManager = new MyAppPrefsManager(context);
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on Position
        final ServiceModel categoryDetails = categoriesList.get(position);

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
        //holder.category_products.setText(categoryDetails.getDescription());

    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    

        ImageView category_image;
        TextView category_title;
        CardView category_card;

        public MyViewHolder(final View itemView) {
            super(itemView);
            category_card = (CardView) itemView.findViewById(R.id.category_card);
            category_image = (ImageView) itemView.findViewById(R.id.imageView);
            category_title = (TextView) itemView.findViewById(R.id.txt_title);
           // category_products = (TextView) itemView.findViewById(R.id.category_products);

           category_card.setOnClickListener(this);
        }


        // Handle Click Listener on OrderProductCategory item
        @Override
        public void onClick(View view) {
            // Get OrderProductCategory Info

            if(myAppPrefsManager.isAddressLoggedIn()){
                Bundle categoryInfo = new Bundle();
                categoryInfo.putInt("CategoryID", categoriesList.get(getAdapterPosition()).getId());
                categoryInfo.putString("CategoryName", categoriesList.get(getAdapterPosition()).getName());

                Fragment fragment;
             // ServiceModel serviceModel=  categoriesList.get(getAdapterPosition());
                if (categoriesList.get(getAdapterPosition()).getName().equalsIgnoreCase("Medical")) {
                    // Navigate to Products Fragment
                    fragment = new Categories_1();
                    fragment.setArguments(categoryInfo);
                    FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();
                }
                else if (categoriesList.get(getAdapterPosition()).getName().equalsIgnoreCase("Medicine")) {
                    // Navigate to Products Fragment
                    if (ConstantValues.IS_USER_LOGGED_IN) {
                        fragment = new PrescriptionFragment();
                        fragment.setArguments(categoryInfo);
                        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.main_fragment, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                    }
                    else {

                        ((MainActivity) context).startActivity(new Intent(((MainActivity) context), LoginActivity.class));
                        ((MainActivity) context).finish();
                        ((MainActivity) context).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                    }

                }
                else if (categoriesList.get(getAdapterPosition()).getName().equalsIgnoreCase("Hospital")) {
                    // Navigate to Products Fragment
                    if (ConstantValues.IS_USER_LOGGED_IN) {
                        fragment = new FragmentHospital();
                        fragment.setArguments(categoryInfo);
                        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.main_fragment, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                    }
                    else {

                        ((MainActivity) context).startActivity(new Intent(((MainActivity) context), LoginActivity.class));
                        ((MainActivity) context).finish();
                        ((MainActivity) context).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                    }

                }
                else if (categoriesList.get(getAdapterPosition()).getName().equalsIgnoreCase("Ambulance")) {
                    // Navigate to Products Fragment
                    if (ConstantValues.IS_USER_LOGGED_IN) {
                        fragment = new FragmentAmbulance();
                        fragment.setArguments(categoryInfo);
                        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.main_fragment, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                    }
                    else {

                        ((MainActivity) context).startActivity(new Intent(((MainActivity) context), LoginActivity.class));
                        ((MainActivity) context).finish();
                        ((MainActivity) context).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                    }

                }
                else if (categoriesList.get(getAdapterPosition()).getName().equalsIgnoreCase("Pathology")) {
                    // Navigate to Products Fragment
                    if (ConstantValues.IS_USER_LOGGED_IN) {
                        fragment = new FragmentPathology();
                        fragment.setArguments(categoryInfo);
                        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.main_fragment, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                    }
                    else {

                        ((MainActivity) context).startActivity(new Intent(((MainActivity) context), LoginActivity.class));
                        ((MainActivity) context).finish();
                        ((MainActivity) context).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                    }

                }

                else {

                }
            }
            else {
                int LAUNCH_SECOND_ACTIVITY = 101;
                Intent i = new Intent(((MainActivity) context), AddressActivity.class);
                ((MainActivity) context).startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
               // ((MainActivity) context).startActivity(new Intent(((MainActivity) context), AddressActivity.class));

            }




        }
    }

}

