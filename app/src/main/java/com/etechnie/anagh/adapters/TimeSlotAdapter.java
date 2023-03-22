package com.etechnie.anagh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etechnie.anagh.R;
import com.etechnie.anagh.models.slot_model.TimeSlot;


import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {
    private Context mContext;
    private List<TimeSlot> mCatlist;
    private RecyclerTouchListener listener;
    private String typeview;

    public interface RecyclerTouchListener {
        public void onClickCategoryItem(TimeSlot item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,time;

        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            time = view.findViewById(R.id.time);
            lvlclick = view.findViewById(R.id.lvl_itemclick);
        }
    }

    public TimeSlotAdapter(Context mContext, List<TimeSlot> mCatlist, final RecyclerTouchListener listener, String viewtype) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;
        this.typeview = viewtype;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_time_slot, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        TimeSlot category = mCatlist.get(position);
        holder.time.setText(category.getTime()+"");
        if(category.getTime()>=12){
            holder.title.setText("PM");
            holder.time.setText((24-category.getTime())+"");
        }
        else {
            holder.title.setText("AM");
            holder.time.setText(category.getTime()+"");
        }
        holder.lvlclick.setOnClickListener(v -> {

                listener.onClickCategoryItem(category);

        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }
}