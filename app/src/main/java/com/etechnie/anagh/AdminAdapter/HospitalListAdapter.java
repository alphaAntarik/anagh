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
import com.bumptech.glide.request.RequestOptions;
import com.etechnie.anagh.R;
import com.etechnie.anagh.models.clinic_model.ClinicModel;
import com.etechnie.anagh.models.hospital_model.HospitalModel;
import com.suke.widget.SwitchButton;

import java.util.List;


/**
 * OrdersListAdapter is the adapter class of RecyclerView holding List of Orders in My_Orders
 **/

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.MyViewHolder> {

    Context context;
    String customerID;
    List<HospitalModel> ordersList;

    public interface OnItemClickListener {
        void onItemClick(HospitalModel item, int id);
    }

    private OnItemClickListener listener;

    public HospitalListAdapter(Context context, String customerID, List<HospitalModel> ordersList, OnItemClickListener listener) {
        this.context = context;
        this.customerID = customerID;
        this.ordersList = ordersList;
        this.listener = listener;
    }


    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_hospital_items, parent, false);

        return new MyViewHolder(itemView);
    }


    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // Get the data model based on Position
        final HospitalModel ProductDetails = ordersList.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher);
        requestOptions.error(R.drawable.ic_launcher);
        Glide.with(context).
                setDefaultRequestOptions(requestOptions)
                .load( ProductDetails.getImage()).into(holder.cart_item_cover);
//
        holder.cart_item_title.setText(ProductDetails.getName());
        holder.cart_item_category.setText("" + ProductDetails.getDescription());
        holder.cart_item_sub_price.setText(ProductDetails.getLat()+", "+ProductDetails.getLng());
        holder.cart_item_base_price.setText(ProductDetails.getAddress());

        holder.bed.setText(ProductDetails.getBed()+"");
        holder.icu.setText(ProductDetails.getIcu()+"");
        holder.ot.setText(ProductDetails.getOt()+"");
        holder.sergion.setText(ProductDetails.getSergion()+"");
        if(ProductDetails.getEmergency()==1){
            holder.emergency.setText("Available");
        }
        else {
            holder.emergency.setText("Not Available");
        }
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

        private TextView cart_item_title, cart_item_category, cart_item_base_price, cart_item_sub_price, quantity,
        bed,icu,ot,sergion,emergency;
        ImageButton cart_item_editBtn, cart_item_removeBtn;
        SwitchButton switchButton;

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
            bed = (TextView) itemView.findViewById(R.id.bed);
            icu = (TextView) itemView.findViewById(R.id.icu);
            ot = (TextView) itemView.findViewById(R.id.ot);
            sergion = (TextView) itemView.findViewById(R.id.sergion);
            emergency = (TextView) itemView.findViewById(R.id.emergency);
            switchButton = (SwitchButton)
                    itemView.findViewById(R.id.switch_button);


        }
    }
}

