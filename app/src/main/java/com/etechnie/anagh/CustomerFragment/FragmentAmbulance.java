package com.etechnie.anagh.CustomerFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.anagh.CustomerAdapter.AmbulanceAdapter;
import com.etechnie.anagh.R;
import com.etechnie.anagh.adapters.HospitalAdapter;
import com.etechnie.anagh.app.App;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.ambulance_model.AmbulanceModel;
import com.etechnie.anagh.models.category_model.CategoryDetails;
import com.etechnie.anagh.models.hospital_model.HospitalModel;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class FragmentAmbulance extends Fragment {
    
    Boolean isMenuItem = true;
    Boolean isHeaderVisible = false;

    TextView emptyText, headerText;
    RecyclerView category_recycler;

    AmbulanceAdapter categoryListAdapter;
    String service_type="";
    List<CategoryDetails> allCategoriesList;
    List<AmbulanceModel> mainCategoriesList;
    int CategoryID=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories, container, false);

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        //noInternetDialog.show();

        if (getArguments() != null) {
            if (getArguments().containsKey("isHeaderVisible")) {
                isHeaderVisible = getArguments().getBoolean("isHeaderVisible");
            }

            if (getArguments().containsKey("CategoryName")) {
                service_type = getArguments().getString("CategoryName", "");
            }
            if (getArguments().containsKey("CategoryID")) {
                CategoryID = getArguments().getInt("CategoryID", 0);
            }
        }
    
    
        if (isMenuItem) {
            // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
            //MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.categories));
        }


        allCategoriesList = new ArrayList<>();

        // Get CategoriesList from ApplicationContext
        allCategoriesList = ((App) getContext().getApplicationContext()).getCategoriesList();


        // Binding Layout Views
        emptyText = (TextView) rootView.findViewById(R.id.empty_record_text);
        headerText = (TextView) rootView.findViewById(R.id.categories_header);
        category_recycler = (RecyclerView) rootView.findViewById(R.id.categories_recycler);
        NestedScrollView scroll_container = (NestedScrollView) rootView.findViewById(R.id.scroll_container);
        scroll_container.setNestedScrollingEnabled(true);
        category_recycler.setNestedScrollingEnabled(false);


        // Hide some of the Views
        emptyText.setVisibility(View.GONE);

        // Check if Header must be Invisible or not
        if (!isHeaderVisible) {
            // Hide the Header of CategoriesList
            headerText.setVisibility(View.GONE);
        } else {
            headerText.setText(getString(R.string.categories));
        }



        mainCategoriesList= new ArrayList<>();

//        for (int i=0;  i<allCategoriesList.size();  i++) {
//            if (allCategoriesList.get(i).getParentId().equalsIgnoreCase("0")) {
//                mainCategoriesList.add(allCategoriesList.get(i));
//            }
//        }


        // Initialize the CategoryListAdapter for RecyclerView
        categoryListAdapter = new AmbulanceAdapter(getContext(), mainCategoriesList, service_type);

        // Set the Adapter and LayoutManager to the RecyclerView
        category_recycler.setAdapter(categoryListAdapter);
        category_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryListAdapter.notifyDataSetChanged();

        RequestDoctor();
        return rootView;
    }

    public void RequestDoctor() {


        Call<ResponseData<Document<AmbulanceModel>>> call = APIClient.getInstance()
                .getAllAmbulance(1,10)
                ;

        call.enqueue(new Callback<ResponseData<Document<AmbulanceModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<Document<AmbulanceModel>>> call, retrofit2.Response<ResponseData<Document<AmbulanceModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        CallData(response.body().getData().getRecords());
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
            public void onFailure(Call<ResponseData<Document<AmbulanceModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    private void CallData(List<AmbulanceModel> data) {
        mainCategoriesList.clear();
        mainCategoriesList.addAll(data);
        categoryListAdapter.notifyDataSetChanged();
    }
}

