package com.etechnie.anagh.AdminFragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etechnie.anagh.R;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.chamber_model.ChamberModel;
import com.etechnie.anagh.models.clinic_model.ClinicModel;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.doctor_model.RequestDoctor;
import com.etechnie.anagh.models.medical_problem_model.MedicalProblemModel;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.Utilities;
import com.etechnie.anagh.utils.ValidateInputs;
import com.google.android.material.snackbar.Snackbar;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

;


public class Create_Clinic extends Fragment {

    View rootView;
    @BindView(R.id.clinic_name)
    EditText clinic_name;
    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.lng)
    EditText lng;

    Integer id=0;
    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_clinic, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.actionAccount));

        mContext=getContext();
        if (getArguments() != null) {

            if (getArguments().containsKey("data")) {
                ClinicModel d =(ClinicModel ) getArguments().getSerializable("data");
                if(d!=null){
                    clinic_name.setText(d.getName());
                    description.setText(d.getDescription());
                    lat.setText(d.getLat());
                    lng.setText(d.getLng());
                    address.setText(d.getAddress());

                    id=d.getId();


                }

            }
        }

        Log.e("sqldate",Utilities.getSqlDateTime());
        return rootView;

    }





    
    //*********** Validate User Info Form Inputs ********//
    
    private boolean validateInfoForm() {

        if (!ValidateInputs.isValidName(clinic_name.getText().toString().trim())) {
            clinic_name.setError("Enter Clinic Name");
            return false;
        }
            return true;

    }







    @OnClick({R.id.updateInfoBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateInfoBtn:
                if(validateInfoForm()){
                    RequestDoctor();
                }

                break;

            default:
                break;
        }
    }





    public void RequestDoctor() {

        ClinicModel request=new ClinicModel();
        request.setName(clinic_name.getText().toString());

        request.setDescription(description.getText().toString());

        request.setLat(lat.getText().toString());
        request.setLng(lng.getText().toString());
        request.setAddress(address.getText().toString());

        request.setId(id);
        request.setCreatedDate(Utilities.getSqlDateTime());
        request.setStatus(1);

        request.setImage("");

        Call<ResponseData<Integer>> call = APIClient.getInstance()
                .RequestClinic
                        (
                                request
                        );

        call.enqueue(new Callback<ResponseData<Integer>>() {
            @Override
            public void onResponse(Call<ResponseData<Integer>> call, retrofit2.Response<ResponseData<Integer>> response) {



                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        Integer data =   response.body().getData();
                        if(data>0){
                            Bundle bundle = new Bundle();

                        Fragment fragment;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        // Navigate to Products Fragment
                        fragment = new All_Clinic();
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_fragment, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(getString(R.string.actionHome)).commit();
                        Snackbar.make(rootView, "Success", Snackbar.LENGTH_SHORT).show();

                        }

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

}
