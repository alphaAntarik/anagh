package com.etechnie.anagh.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.LoginActivity;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.activities.VerifyPhoneActivity;
import com.etechnie.anagh.app.MyAppPrefsManager;
import com.etechnie.anagh.customs.CircularImageView;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.doctor_model.RequestDoctorAppointment;
import com.etechnie.anagh.models.login_model.PostLogin;
import com.etechnie.anagh.models.login_model.ResponseLogin;
import com.etechnie.anagh.models.slot_model.TimeSlot;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.JWTUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;


public class AppointmentFragment extends Fragment {


    DoctorModel doctorModel;
    Context mContext;
    TimeSlot timeSlot;
    @BindView(R.id.category_image) CircularImageView doctor_image;
    @BindView(R.id.doctor_name) TextView doctor_name;
    @BindView(R.id.doctor_specilist) TextView doctor_specilist;
    @BindView(R.id.clinic_time) TextView clinic_time;
    @BindView(R.id.clinic_address) TextView clinic_address;
    @BindView(R.id.user_name) TextView user_name;
    @BindView(R.id.fees) TextView fees;
    @BindView(R.id.booking_fees) TextView booking_fees;
    @BindView(R.id.total) TextView total;

    String title="";
    MyAppPrefsManager myAppPrefsManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_appointment, container, false);
        mContext=getContext();
        myAppPrefsManager = new MyAppPrefsManager(mContext);
        if (getArguments().containsKey("data")) {
          String  data = getArguments().getString("data");
          doctorModel=new Gson().fromJson(data,DoctorModel.class);
        }
        if (getArguments().containsKey("timeslot")) {
            timeSlot = (TimeSlot) getArguments().getSerializable("timeslot");
        }

        if (getArguments().containsKey("title")) {
            title = getArguments().getString("title");

        }
        ButterKnife.bind(this, rootView);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        // Set OrderProductCategory Image on ImageView with Glide Library
        Glide.with(mContext)
                .setDefaultRequestOptions(options)
                .load(doctorModel.getImage())
                .into(doctor_image);
        doctor_name.setText(doctorModel.getName());
        doctor_specilist.setText(doctorModel.getSpeciality());
        String time="";
        if(timeSlot.getTime()>=12){
            time=(24-timeSlot.getTime())+" PM";

        }
        else {
            time=(timeSlot.getTime())+" AM";
        }
        clinic_time.setText(title+" , "+time);
        clinic_address.setText(doctorModel.getClinic()+"- "+doctorModel.getClinicAddress());
        user_name.setText(myAppPrefsManager.getUserName());
        fees.setText("₹ "+doctorModel.getFees());
        total.setText("₹ "+doctorModel.getFees());
        booking_fees.setText("₹ "+"0");
        return rootView;
    }


    public void RequestAllProducts() {

        RequestDoctorAppointment appointment=new RequestDoctorAppointment();
        Call<ResponseData<Integer>> call = APIClient.getInstance()
                .RequestAppointment
                        (
                                appointment
                        );

        call.enqueue(new Callback<ResponseData<Integer>>() {
            @Override
            public void onResponse(Call<ResponseData<Integer>> call, retrofit2.Response<ResponseData<Integer>> response) {



                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        Integer data =   response.body().getData();

                    }
                    else {
                        // Unable to get Success status
                        Toast.makeText(mContext, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Show the Error Message
                    String Str = response.message();
                    Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {

                String Str = "" + t;
                Toast.makeText(mContext, "NetworkCallFailure : " + t, Toast.LENGTH_LONG).show();

            }
        });

    }


    @OnClick({R.id.btn_proceed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_proceed:
                RequestDoctorAppointment appointment=new RequestDoctorAppointment();
                Fragment fragment = new FragmentPayment();
                Bundle categoryInfo = new Bundle();

                categoryInfo.putSerializable("data",appointment);
                fragment.setArguments(categoryInfo);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
                break;


            default:
                break;
        }
    }
}

