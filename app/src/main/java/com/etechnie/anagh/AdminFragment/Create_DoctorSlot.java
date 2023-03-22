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
import com.etechnie.anagh.models.doctor_model.RequestDoctor;
import com.etechnie.anagh.models.medical_problem_model.MedicalProblemModel;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.models.slot_model.DoctorSlot;
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


public class Create_DoctorSlot extends Fragment {

    View rootView;
    @BindView(R.id.time)
    EditText time;

    @BindView(R.id.day_spinner)
    MaterialBetterSpinner day_spinner;

    Integer doctor_id=0;

    Context mContext;
    String service_type="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_doctor_slot, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.actionAccount));
        day_spinner.setVisibility(View.GONE);
        if (getArguments() != null) {

            if (getArguments().containsKey("id")) {
                doctor_id = getArguments().getInt("id");
            }
            if (getArguments().containsKey("service_type")) {
                service_type = getArguments().getString("service_type");
            }
        }
        mContext=getContext();
        dayspinner();

        return rootView;

    }





    
    //*********** Validate User Info Form Inputs ********//
    
    private boolean validateInfoForm() {

        if (!ValidateInputs.isValidName(day_spinner.getText().toString().trim())) {
            day_spinner.setError("Enter Doctor Name");
            return false;
        }
            return true;

    }







    @OnClick({R.id.updateInfoBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateInfoBtn:
                if(validateInfoForm()){
                    RequestDoctorSlot();
                }

                break;

            default:
                break;
        }
    }






    private  void dayspinner(){
        ArrayList<String> mStringList = new ArrayList<String>();
        day_spinner.setVisibility(View.VISIBLE);



        mStringList.add("Monday");
        mStringList.add("Tuesday");
        mStringList.add("Wednesday");
        mStringList.add("Thursday");
        mStringList.add("Friday");
        mStringList.add("Saturday");
        mStringList.add("Sunday");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, mStringList);


        day_spinner.setAdapter(adapter);
       // int spinnerPosition = adapter.getPosition(unitId);



    }


    public void RequestDoctorSlot() {

        DoctorSlot requestDoctor=new DoctorSlot();

        requestDoctor.setTime(Integer.parseInt(time.getText().toString()));

        requestDoctor.setDay(day_spinner.getText().toString());
        requestDoctor.setDoctor_id(doctor_id);
        requestDoctor.setCreated_date(Utilities.getSqlDateTime());
        requestDoctor.setStatus(1);
        requestDoctor.setSlot_type(service_type);

        Call<ResponseData<Integer>> call = APIClient.getInstance()
                .RequestDoctorSlot
                        (
                                requestDoctor
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
                            bundle.putInt("id", doctor_id);
                            bundle.putString("service_type",service_type);
                        Fragment fragment;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        // Navigate to Products Fragment
                        fragment = new All_SlotTimeFragment();
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
