package com.etechnie.anagh.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.etechnie.anagh.R;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.prescrition_order.PrescriptionHistory;
import com.etechnie.anagh.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;


public class PrescriptionOrder extends Fragment {

    @BindView(R.id.txt_resent)
    TextView txtResent;
    @BindView(R.id.txt_delivered)
    TextView txtDelivered;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    StaggeredGridLayoutManager gridLayoutManager;

    @BindView(R.id.txt_notfount)
    TextView txtNotfount;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    ItemAdp itemAdp;
    List<PrescriptionHistory> orderHistories = new ArrayList<>();
    List<PrescriptionHistory> resent = new ArrayList<>();
    List<PrescriptionHistory> delivery = new ArrayList<>();
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_prescriptionorder, container, false);

        mContext=getContext();
        ButterKnife.bind(this, rootView);
        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        getOrder();
        return rootView;
    }


    private void getOrder() {
        Call<ResponseData<List<PrescriptionHistory>>> call = APIClient.getInstance()
                .getPrescriptionOrder
                        (
                                1
                        );

        call.enqueue(new Callback<ResponseData<List<PrescriptionHistory>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PrescriptionHistory>>> call, retrofit2.Response<ResponseData<List<PrescriptionHistory>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        orderHistories = response.body().getData();
                        resent = new ArrayList<>();
                        delivery = new ArrayList<>();
                        for (int i = 0; i < orderHistories.size(); i++) {
                            if (orderHistories.get(i).getStatus().equalsIgnoreCase("Completed")) {
                                delivery.add(orderHistories.get(i));
                            } else {
                                resent.add(orderHistories.get(i));
                            }
                        }
                        dataset(resent);
                    }
                    else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        myRecyclerView.setVisibility(View.GONE);
                        lvlNotfound.setVisibility(View.VISIBLE);
                        txtNotfount.setText("Your orders will be displayed hear.");
                    }
                    else {

                        myRecyclerView.setVisibility(View.GONE);
                        lvlNotfound.setVisibility(View.VISIBLE);
                        txtNotfount.setText("Your orders will be displayed hear.");
                               }
                }
                else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();

                    myRecyclerView.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                    txtNotfount.setText("Your orders will be displayed hear.");
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<List<PrescriptionHistory>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
                myRecyclerView.setVisibility(View.GONE);
                lvlNotfound.setVisibility(View.VISIBLE);
                txtNotfount.setText("Your orders will be displayed hear.");
            }
        });
    }


    @OnClick({ R.id.txt_resent, R.id.txt_delivered})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_resent:
                dataset(resent);
                txtResent.setTextColor(getResources().getColor(R.color.white));
                txtDelivered.setTextColor(getResources().getColor(R.color.black));
                txtResent.setBackground(getResources().getDrawable(R.drawable.orderbox));
                txtDelivered.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                break;
            case R.id.txt_delivered:
                dataset(delivery);
                txtResent.setTextColor(getResources().getColor(R.color.black));
                txtDelivered.setTextColor(getResources().getColor(R.color.white));
                txtResent.setBackground(getResources().getDrawable(R.drawable.orderbox_white));
                txtDelivered.setBackground(getResources().getDrawable(R.drawable.orderbox));

                break;
            default:
                break;
        }
    }


    public void dataset(List<PrescriptionHistory> s) {

        if (s.size() != 0) {
            lvlNotfound.setVisibility(View.GONE);
            myRecyclerView.setVisibility(View.VISIBLE);
            gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
            myRecyclerView.setLayoutManager(gridLayoutManager);
            itemAdp = new ItemAdp(mContext, s);
            myRecyclerView.setAdapter(itemAdp);
        } else {
            myRecyclerView.setVisibility(View.GONE);
            lvlNotfound.setVisibility(View.VISIBLE);
            txtNotfount.setText("Your orders will be displayed hear.");
        }
    }

    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {


        private LayoutInflater mInflater;
        Context mContext;
        List<PrescriptionHistory> lists;

        public ItemAdp(Context context, List<PrescriptionHistory> s) {
            this.mInflater = LayoutInflater.from(context);
            this.lists = s;
            this.mContext = context;
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.custome_orderitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {

            PrescriptionHistory history = lists.get(i);
            holder.txtOrder.setText("" + history.getId());
            holder.txtOrderdate.setText("" + history.getOrderDate());
            holder.txtOrderstatus.setText("" + history.getStatus());

            if (history.getStatus().equalsIgnoreCase("Pending")) {
                holder.txtCancel.setVisibility(View.VISIBLE);
            } else {
                holder.txtCancel.setVisibility(View.INVISIBLE);

            }
            holder.txtInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.txtCancel.setOnClickListener(v -> {
                AlertDialog myDelete = new AlertDialog.Builder(mContext)
                        .setTitle("Order Cancel")
                        .setMessage("Are you sure you want to Order Cancel?")
                        .setPositiveButton("Yes", (dialog, whichButton) -> {
                            Log.d("sdj", "" + whichButton);
                            dialog.dismiss();
                            history.setStatus("Cancelled");
                            lists.set(i, history);
                            //getCancel(history.getId());
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            Log.d("sdj", "" + which);
                            dialog.dismiss();
                        })
                        .create();
                myDelete.show();

            });


        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_order)
            TextView txtOrder;
            @BindView(R.id.txt_total)
            TextView txtTotal;
            @BindView(R.id.txt_orderstatus)
            TextView txtOrderstatus;
            @BindView(R.id.txt_orderdate)
            TextView txtOrderdate;
            @BindView(R.id.txt_info)
            TextView txtInfo;
            @BindView(R.id.txt_cancel)
            TextView txtCancel;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }
}

