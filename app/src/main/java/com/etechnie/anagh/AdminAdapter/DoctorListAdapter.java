package com.etechnie.anagh.AdminAdapter;

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
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.suke.widget.SwitchButton;


import java.util.List;


/**
 * OrdersListAdapter is the adapter class of RecyclerView holding List of Orders in My_Orders
 **/

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MyViewHolder> {

    Context context;
    String customerID;
    List<DoctorModel> ordersList;

    public interface OnItemClickListener {
        void onItemClick(DoctorModel item, int id);
    }

    private OnItemClickListener listener;

    public DoctorListAdapter(Context context, String customerID, List<DoctorModel> ordersList, OnItemClickListener listener) {
        this.context = context;
        this.customerID = customerID;
        this.ordersList = ordersList;
        this.listener = listener;
    }


    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_doctor_items, parent, false);

        return new MyViewHolder(itemView);
    }


    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // Get the data model based on Position
        final DoctorModel ProductDetails = ordersList.get(position);


        Glide.with(context).load( ProductDetails.getImage()).into(holder.cart_item_cover);
//
        holder.cart_item_title.setText(ProductDetails.getName());
        holder.quantity.setText("" + ProductDetails.getAddress());
        holder.cart_item_sub_price.setText(ProductDetails.getSpeciality());
        holder.cart_item_base_price.setText(ProductDetails.getDegree());
       // holder.cart_item_viewBtn.setVisibility(View.GONE);
        holder.cart_item_viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(ProductDetails, 4);
            }
        });

        holder.cart_item_editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(ProductDetails, 1);
            }
        });
        holder.cart_item_removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(ProductDetails, 2);
            }
        });
//        if (ProductDetails.getProductsQuantity() > 0) {
//            holder.switchButton.setChecked(true);
//        } else {
//            holder.switchButton.setChecked(false);
//        }

        holder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked) {
                    listener.onItemClick(ProductDetails, 8);
                } else {
                    listener.onItemClick(ProductDetails, 9);
                }
            }
        });
    }


    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return ordersList.size();
    }


    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private Button cart_item_viewBtn;
        private ImageView cart_item_cover;
        private RecyclerView attributes_recycler;

        private TextView cart_item_title, cart_item_category, cart_item_base_price, cart_item_sub_price, quantity;
        ImageButton cart_item_editBtn, cart_item_removeBtn;
        com.suke.widget.SwitchButton switchButton;

        public MyViewHolder(final View itemView) {
            super(itemView);


            cart_item_title = (TextView) itemView.findViewById(R.id.cart_item_title);
            cart_item_base_price = (TextView) itemView.findViewById(R.id.cart_item_base_price);
            cart_item_sub_price = (TextView) itemView.findViewById(R.id.cart_item_sub_price);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            cart_item_category = (TextView) itemView.findViewById(R.id.cart_item_category);
            cart_item_cover = (ImageView) itemView.findViewById(R.id.cart_item_cover);
            cart_item_viewBtn = (Button) itemView.findViewById(R.id.cart_item_viewBtn);
            cart_item_editBtn = (ImageButton) itemView.findViewById(R.id.cart_item_editBtn);
            cart_item_removeBtn = (ImageButton) itemView.findViewById(R.id.cart_item_removeBtn);

            switchButton = (com.suke.widget.SwitchButton)
                    itemView.findViewById(R.id.switch_button);


        }
    }
}

