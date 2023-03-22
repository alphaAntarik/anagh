package com.etechnie.anagh.AdminFragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.anagh.AdminAdapter.AmbulanceListAdapter;
import com.etechnie.anagh.AdminAdapter.PathologyListAdapter;
import com.etechnie.anagh.R;
import com.etechnie.anagh.customs.DialogLoader;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.pathology_model.PathologyModel;
import com.etechnie.anagh.models.product_model.GetAllProducts;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.network.APIClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class All_Pathology extends Fragment {

    View rootView;
    String customerID;
    

    TextView emptyRecord;
    FrameLayout banner_adView;
    RecyclerView orders_recycler;

    DialogLoader dialogLoader;
    PathologyListAdapter ordersListAdapter;

    List<PathologyModel> ordersList = new ArrayList<>();
    int pageNo = 0;
int LANGUAGE_ID=0;
    String sortBy = "Newest";
    int ResId=0;
    AppCompatButton add;
    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_doctor, container, false);
        mContext=getContext();
        //MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.actionOrders));
        if (getArguments() != null) {
            if (getArguments().containsKey("sortBy")) {
                LANGUAGE_ID = getArguments().getInt("sortBy");
            }
            if (getArguments().containsKey("ResId")) {
                ResId = getArguments().getInt("ResId");
            }
        }
        // Get the CustomerID from SharedPreferences
        customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");
        
        
        // Binding Layout Views
        emptyRecord = (TextView) rootView.findViewById(R.id.empty_record);
        banner_adView = (FrameLayout) rootView.findViewById(R.id.banner_adView);
        orders_recycler = (RecyclerView) rootView.findViewById(R.id.orders_recycler);

        add=(AppCompatButton) rootView.findViewById(R.id.add);
        add.setText("Create Pathology");

        // Hide some of the Views
        emptyRecord.setVisibility(View.GONE);


        dialogLoader = new DialogLoader(getContext());

        ordersListAdapter = new PathologyListAdapter(getContext(), customerID, ordersList, new PathologyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PathologyModel item,int state) {
                if(state==1){
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String userJson = gson.toJson(item);
                    bundle.putInt("ResId", ResId);
                    bundle.putInt("sortBy", LANGUAGE_ID);
                    bundle.putSerializable("data", item);
                    Fragment fragment;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Navigate to Products Fragment
                    fragment = new Create_Pathology();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(getString(R.string.actionHome)).commit();
                }
                else if(state==2){
                    showDialog(item.getId());
                }
                else if(state==8){
                    Active(item.getId());
                }
                else if(state==9){
                    DeActive(item.getId());
                }
                else if(state==4){
                    Bundle bundle = new Bundle();

                    bundle.putInt("id", item.getId());
                    bundle.putString("service_type", "Pathology");
                    Fragment fragment;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Navigate to Products Fragment
                    fragment = new All_SlotTimeFragment();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(getString(R.string.actionHome)).commit();
                }
                else {
                }
            }
        });
        setRecyclerViewLayoutManager();
        // Set the Adapter and LayoutManager to the RecyclerView


        // Request for User's Orders
        FetchAllClinic();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Bundle bundle = new Bundle();

                    bundle.putInt("ResId", ResId);
                    bundle.putInt("sortBy", LANGUAGE_ID);
                    bundle.putBoolean("isMenuItem", true);
                    Fragment fragment;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    // Navigate to Products Fragment
                    fragment = new Create_Pathology();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(getString(R.string.actionHome)).commit();

            }
        });
        return rootView;
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        // If a LayoutManager has already been set, get current Scroll Position
        if (orders_recycler.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) orders_recycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

      //  productAdapter.toggleLayout(isGridView);

        orders_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        orders_recycler.setAdapter(ordersListAdapter);

        orders_recycler.scrollToPosition(scrollPosition);
    }

    //*********** Adds Orders returned from the Server to the OrdersList ********//

    private void addOrders(List<PathologyModel> orderData) {

        // Add Orders to ordersList from the List of OrderData
        //ordersList = orderData.getData();
           ordersList.clear();
            ordersList.addAll(orderData);

        ordersListAdapter.notifyDataSetChanged();
    }



    //*********** Request User's Orders from the Server ********//



    public void FetchAllClinic() {



        Call<ResponseData<Document<PathologyModel>>> call = APIClient.getInstance()
                .getAllPathology(1,10)
                ;

        call.enqueue(new Callback<ResponseData<Document<PathologyModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<Document<PathologyModel>>> call, retrofit2.Response<ResponseData<Document<PathologyModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        addOrders(response.body().getData().getRecords());
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
            public void onFailure(Call<ResponseData<Document<PathologyModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }
    public void showDialog(final int id) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_info, null);

        TextView mOk = (TextView) mView.findViewById(R.id.dialog_button_positive);
        TextView mCancel = (TextView) mView.findViewById(R.id.dialog_button_negative);
        TextView dialog_message = (TextView) mView.findViewById(R.id.dialog_message);
        TextView dialog_title = (TextView) mView.findViewById(R.id.dialog_title);
        dialog_message.setText("Item will not recover after delete");
        dialog_title.setText("Are you sure you want to delete?");
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete(id);
                dialog.dismiss();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    public void Delete(int id) {
        dialogLoader.showProgressDialog();
        Call<ResponseData<String>> call = APIClient.getInstance()
                .DeleteDoctor
                        (
                                id
                        );

        call.enqueue(new Callback<ResponseData<String>>() {
            @Override
            public void onResponse(Call<ResponseData<String>> call, retrofit2.Response<ResponseData<String>> response) {

                dialogLoader.hideProgressDialog();

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        FetchAllClinic();

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
            public void onFailure(Call<ResponseData<String>> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                String Str = "" + t;
                Toast.makeText(mContext, "NetworkCallFailure : " + t, Toast.LENGTH_LONG).show();

            }
        });


    }

    public void Active(int OrderId) {
        dialogLoader.showProgressDialog();
        GetAllProducts getAllProducts = new GetAllProducts();

        getAllProducts.setLanguageId(OrderId);
        getAllProducts.setCustomersId(customerID);
        getAllProducts.setType(sortBy);

//        Call<Boolean> call = APIClient.getInstance()
//                .ProductActive
//                        (
//                                getAllProducts
//                        );
//
//        call.enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
//
//                if (response.isSuccessful()) {
//                    if (response.body()) {
//
//                    }
//
//                    else {
//                        // Unable to get Success status
//                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
//                    }
//
//
//
//                }
//                else {
//                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
//
//                }
//                dialogLoader.hideProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
//                dialogLoader.hideProgressDialog();
//            }
//        });
    }

    public void DeActive(int OrderId) {
        dialogLoader.showProgressDialog();
        GetAllProducts getAllProducts = new GetAllProducts();

        getAllProducts.setLanguageId(OrderId);
        getAllProducts.setCustomersId(customerID);
        getAllProducts.setType(sortBy);

//        Call<Boolean> call = APIClient.getInstance()
//                .ProductDeActive
//                        (
//                                getAllProducts
//                        );
//
//        call.enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
//
//                if (response.isSuccessful()) {
//                    if (response.body()) {
//
//
//                    }
//
//                    else {
//                        // Unable to get Success status
//                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
//                    }
//
//
//
//                }
//                else {
//                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
//
//                }
//                dialogLoader.hideProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
//                dialogLoader.hideProgressDialog();
//            }
//        });
    }
}

