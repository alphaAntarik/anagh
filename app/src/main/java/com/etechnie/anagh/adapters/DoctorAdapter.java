package com.etechnie.anagh.adapters;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.R;

import java.util.List;

import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.customs.CircularImageView;
import com.etechnie.anagh.fragment.Doctor_Description;
import com.etechnie.anagh.fragment.SlotTimeFragment;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.google.gson.Gson;


/**
 * CategoryListAdapter is the adapter class of RecyclerView holding List of Categories in MainCategories
 **/

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    boolean isSubCategory;

    Context context;
    List<DoctorModel> categoriesList;


    public DoctorAdapter(Context context, List<DoctorModel> categoriesList, boolean isSubCategory) {
        this.context = context;
        this.isSubCategory = isSubCategory;
        this.categoriesList = categoriesList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctor_adapter, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // Get the data model based on Position
        final DoctorModel categoryDetails = categoriesList.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        // Set OrderProductCategory Image on ImageView with Glide Library
        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(categoryDetails.getImage())
                .into(holder.category_image);


        holder.category_title.setText(categoryDetails.getName());
        holder.category_products.setText(categoryDetails.getSpeciality());
        holder.experience.setText(categoryDetails.getExperience()+" years experience");
        holder.clinic.setText(categoryDetails.getClinic());
        holder.distance.setText("5 km");
        holder.fees.setText(ConstantValues.CURRENCY_SYMBOL+" "+categoryDetails.getFees()+"");
        holder.address.setText(categoryDetails.getAddress());
    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    
        CircularImageView category_image;
        CardView category_card;
        TextView category_title, category_products,experience,clinic,distance,fees,address;
        AppCompatButton bookingBtn;

        public MyViewHolder(final View itemView) {
            super(itemView);
            category_card = (CardView) itemView.findViewById(R.id.category_card);
            category_image = (CircularImageView) itemView.findViewById(R.id.category_image);
            category_title = (TextView) itemView.findViewById(R.id.category_title);
            category_products = (TextView) itemView.findViewById(R.id.category_products);
            experience= (TextView) itemView.findViewById(R.id.experience);
            clinic=(TextView) itemView.findViewById(R.id.clinic);
            distance=(TextView) itemView.findViewById(R.id.distance);
            fees=(TextView) itemView.findViewById(R.id.fees);
            bookingBtn=(AppCompatButton) itemView.findViewById(R.id.bookingBtn);
            address=(TextView) itemView.findViewById(R.id.address);
            bookingBtn.setOnClickListener(this);
            category_image.setOnClickListener(this);
        }


        // Handle Click Listener on OrderProductCategory item
        @Override
        public void onClick(View v) {
            Bundle categoryInfo = new Bundle();
            categoryInfo.putString("data",new Gson().toJson(categoriesList.get(getAdapterPosition())));
            categoryInfo.putString("service_type", "Medical");
            categoryInfo.putInt("id", categoriesList.get(getAdapterPosition()).getId());
            Fragment fragment ;
            switch(v.getId()){

                // categoryInfo.putString("CategoryName", categoriesList.get(getAdapterPosition()).getName());


                case R.id.bookingBtn: /** Start a new Activity MyCards.java */

                    fragment = new SlotTimeFragment();
                    break;

                case R.id.category_image: /** AlerDialog when click on Exit */
                    fragment = new Doctor_Description();

                    break;
                default:
                    fragment = new SlotTimeFragment();
            }
            // Get OrderProductCategory Info
            fragment.setArguments(categoryInfo);
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();
        }
    }

}

