package com.etechnie.anagh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etechnie.anagh.R;

import com.etechnie.anagh.models.slot_model.DoctorSlot;
import com.suke.widget.SwitchButton;

import java.util.List;


/**
 * OrdersListAdapter is the adapter class of RecyclerView holding List of Orders in My_Orders
 **/

public class DoctorSlotListAdapter extends RecyclerView.Adapter<DoctorSlotListAdapter.MyViewHolder> {

    Context context;
    String customerID;
    List<DoctorSlot> ordersList;

    public interface OnItemClickListener {
        void onItemClick(DoctorSlot item, int id);
    }

    private OnItemClickListener listener;

    public DoctorSlotListAdapter(Context context, String customerID, List<DoctorSlot> ordersList, OnItemClickListener listener) {
        this.context = context;
        this.customerID = customerID;
        this.ordersList = ordersList;
        this.listener = listener;
    }


    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_doctor_slot_items, parent, false);

        return new MyViewHolder(itemView);
    }


    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // Get the data model based on Position
        final DoctorSlot ProductDetails = ordersList.get(position);


        holder.day.setText(ProductDetails.getDay());
        holder.time.setText("" + ProductDetails.getTime());
        if(ProductDetails.getTime()>=12){

            holder.time.setText((ProductDetails.getTime()-12)+" PM");
        }
        else {

            holder.time.setText(ProductDetails.getTime()+" AM");
        }

        holder.cart_item_removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(ProductDetails, 2);
            }
        });
//

    }


    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return ordersList.size();
    }


    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public static class MyViewHolder extends RecyclerView.ViewHolder {




        private TextView day, time;
        ImageButton cart_item_removeBtn;


        public MyViewHolder(final View itemView) {
            super(itemView);


            day = (TextView) itemView.findViewById(R.id.day);
            time = (TextView) itemView.findViewById(R.id.time);


            cart_item_removeBtn = (ImageButton) itemView.findViewById(R.id.cart_item_removeBtn);




        }
    }
}

