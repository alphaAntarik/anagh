package com.etechnie.anagh.AdminFragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.anagh.R;
import com.etechnie.anagh.app.App;
import com.etechnie.anagh.customs.CircularImageView;
import com.etechnie.anagh.customs.DialogLoader;
import com.etechnie.anagh.databases.User_Info_DB;
import com.etechnie.anagh.imagepicker.ImagePicker;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.chamber_model.ChamberModel;
import com.etechnie.anagh.models.clinic_model.ClinicModel;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.doctor_model.RequestDoctor;
import com.etechnie.anagh.models.doctor_model.RequestDoctorAppointment;
import com.etechnie.anagh.models.medical_problem_model.MedicalProblemModel;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.models.user_model.UserDetails;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.CheckPermissions;
import com.etechnie.anagh.utils.Utilities;
import com.etechnie.anagh.utils.ValidateInputs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;


public class Create_Doctor extends Fragment {

    View rootView;
    @BindView(R.id.doctor_name)
    EditText doctor_name;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.experience)
    EditText experience;
    @BindView(R.id.contact_no)
    EditText contact_no;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.fees)
    EditText fees;
    @BindView(R.id.speciality)
    MaterialBetterSpinner speciality_spinner;
    @BindView(R.id.degree)
    EditText degree;
    @BindView(R.id.chamber_spinner)
    MaterialBetterSpinner chamber_spinner;
    @BindView(R.id.clinic_spinner)
    MaterialBetterSpinner clinic_spinner;
    Integer chamber_id=0;
    Integer clinic_id=0;
    Integer id=0;
    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_doctor, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.actionAccount));
        chamber_spinner.setVisibility(View.GONE);
        clinic_spinner.setVisibility(View.GONE);
        mContext=getContext();
        if (getArguments() != null) {

           if (getArguments().containsKey("data")) {
              DoctorModel  d =(DoctorModel ) getArguments().getSerializable("data");
               if(d!=null){
                   doctor_name.setText(d.getName());
                   description.setText(d.getDescription());
                   doctor_name.setText(d.getName());
                   experience.setText(d.getExperience());
                   //contact_no.setText(d.getContact_no());
                   email.setText(d.getEmail());
                   address.setText(d.getAddress());
                   double fee= Double.parseDouble(d.getFees());
                   fees.setText(""+(int)fee);
                   speciality_spinner.setText(d.getSpeciality());
                   degree.setText(d.getDegree());
                   //chamber_id=d.getChamberType();

                   id=d.getId();


               }

            }
        }
        FetchAllChamberType();
        FetchAllClinic();
        FetchAllMedicalProblems();
        Log.e("sqldate",Utilities.getSqlDateTime());
        return rootView;

    }





    
    //*********** Validate User Info Form Inputs ********//
    
    private boolean validateInfoForm() {

        if (!ValidateInputs.isValidName(doctor_name.getText().toString().trim())) {
            doctor_name.setError("Enter Doctor Name");
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





    public void FetchAllChamberType() {



        Call<ResponseData<Document<ChamberModel>>> call = APIClient.getInstance()
                .getAllChamber(1,10)
                ;

        call.enqueue(new Callback<ResponseData<Document<ChamberModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<Document<ChamberModel>>> call, retrofit2.Response<ResponseData<Document<ChamberModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        chamberspinner(response.body().getData().getRecords());
                    }
                    else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        // emptyRecord.setVisibility(View.VISIBLE);
                        //  Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                    else {
                        // Unable to get Success status
                        //emptyRecord.setVisibility(View.VISIBLE);
                        // Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<Document<ChamberModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    public void FetchAllClinic() {



        Call<ResponseData<Document<ClinicModel>>> call = APIClient.getInstance()
                .getAllClinic(1,10)
                ;

        call.enqueue(new Callback<ResponseData<Document<ClinicModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<Document<ClinicModel>>> call, retrofit2.Response<ResponseData<Document<ClinicModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        clinicspinner(response.body().getData().getRecords());
                    }
                    else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        // emptyRecord.setVisibility(View.VISIBLE);
                        //  Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                    else {
                        // Unable to get Success status
                        //emptyRecord.setVisibility(View.VISIBLE);
                        // Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<Document<ClinicModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    public void FetchAllMedicalProblems() {



        Call<ResponseData<Document<MedicalProblemModel>>> call = APIClient.getInstance()
                .getAllMedicalproblem(1,10)
                ;

        call.enqueue(new Callback<ResponseData<Document<MedicalProblemModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<Document<MedicalProblemModel>>> call, retrofit2.Response<ResponseData<Document<MedicalProblemModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        Specialityspinner(response.body().getData().getRecords());
                    }
                    else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        // emptyRecord.setVisibility(View.VISIBLE);
                        //  Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                    else {
                        // Unable to get Success status
                        //emptyRecord.setVisibility(View.VISIBLE);
                        // Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<Document<MedicalProblemModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }
    private  void chamberspinner(List<ChamberModel> chamberModels){
        ArrayList<String> mStringList = new ArrayList<String>();
        chamber_spinner.setVisibility(View.VISIBLE);

        for (ChamberModel route : chamberModels) {

            mStringList.add(route.getTitle());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, mStringList);


        chamber_spinner.setAdapter(adapter);
       // int spinnerPosition = adapter.getPosition(unitId);

        chamber_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChamberModel model = new ChamberModel();

                model = chamberModels.get(i);
                chamber_id=model.getId();
            }
        });

    }

    private  void clinicspinner(List<ClinicModel> chamberModels){
        ArrayList<String> mStringList = new ArrayList<String>();
        clinic_spinner.setVisibility(View.VISIBLE);

        for (ClinicModel route : chamberModels) {

            mStringList.add(route.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, mStringList);


        clinic_spinner.setAdapter(adapter);
        // int spinnerPosition = adapter.getPosition(unitId);

        clinic_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClinicModel model = new ClinicModel();

                model = chamberModels.get(i);
                clinic_id=model.getId();
            }
        });

    }
    private  void Specialityspinner(List<MedicalProblemModel> chamberModels){
        ArrayList<String> mStringList = new ArrayList<String>();
        speciality_spinner.setVisibility(View.VISIBLE);

        for (MedicalProblemModel route : chamberModels) {

            mStringList.add(route.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, mStringList);


        speciality_spinner.setAdapter(adapter);
        // int spinnerPosition = adapter.getPosition(unitId);



    }
    public void RequestDoctor() {

        RequestDoctor requestDoctor=new RequestDoctor();
        requestDoctor.setName(doctor_name.getText().toString());

        requestDoctor.setDescription(description.getText().toString());
        requestDoctor.setExperience(Integer.parseInt(experience.getText().toString()));
        requestDoctor.setContactNo(contact_no.getText().toString());
        requestDoctor.setEmail(email.getText().toString());
        requestDoctor.setAddress(address.getText().toString());
        requestDoctor.setFees(String.valueOf(Integer.parseInt(fees.getText().toString())));
        requestDoctor.setSpeciality(speciality_spinner.getText().toString());
        requestDoctor.setDegree(degree.getText().toString());
        requestDoctor.setChamberType(chamber_id);
        requestDoctor.setClinicId(clinic_id);
        requestDoctor.setCreatedDate(Utilities.getSqlDateTime());
        requestDoctor.setStatus(1);
        requestDoctor.setIcon("");
        requestDoctor.setImage("");
        requestDoctor.setId(id);

        Log.e("doctor",new Gson().toJson(requestDoctor));
        Call<ResponseData<Integer>> call = APIClient.getInstance()
                .RequestDoctor
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

                        Fragment fragment;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        // Navigate to Products Fragment
                        fragment = new All_Doctor();
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
