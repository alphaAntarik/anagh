package com.etechnie.anagh.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.adapters.ViewPagerCustomAdapter;
import com.etechnie.anagh.app.App;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.category_model.CategoryDetails;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.slot_model.DateSlot;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.Utilities;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class SlotTimeFragment extends Fragment {

    String sortBy = "";
    boolean isMenuItem = false;
    boolean isSubFragment = false;
    int parentCategoryID;
    int selectedTabIndex = 0;
    String selectedTabText = "";

    TabLayout product_tabs;
    ViewPager product_viewpager;
int doctor_id=0;
    ViewPagerCustomAdapter viewPagerCustomAdapter;

    List<CategoryDetails> allCategoriesList = new ArrayList<>();
    List<CategoryDetails> allSubCategoriesList = new ArrayList<>();
    List<DateSlot> finalCategoriesList = new ArrayList<>();
    String doctorModel;
    public void invalidateProducts(){
        viewPagerCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.products, container, false);

        if (getArguments() != null) {
            if (getArguments().containsKey("service_type")) {
                sortBy = getArguments().getString("service_type", "");
            }
            if (getArguments().containsKey("data")) {
                doctorModel = getArguments().getString("data");
            }
            if (getArguments().containsKey("id")) {
                doctor_id = getArguments().getInt("id",0);
            }
        }

        RequestDateTimeSlot();
        // Binding Layout Views
        product_tabs = (TabLayout) rootView.findViewById(R.id.product_tabs);
        product_viewpager = (ViewPager) rootView.findViewById(R.id.product_viewpager);



        

        return rootView;

    }



    //*********** Setup the ViewPagerAdapter ********//

    private void setupViewPagerAdapter() {

        // Initialize ViewPagerAdapter with ChildFragmentManager for ViewPager
        viewPagerCustomAdapter = new ViewPagerCustomAdapter(getChildFragmentManager());

        // Initialize All_Products Fragment with specified arguments
     //   Fragment allProducts = new All_Products();
      //  Bundle bundleInfo = new Bundle();
       // bundleInfo.putString("sortBy", sortBy);
       // allProducts.setArguments(bundleInfo);

        // Add the Fragments to the ViewPagerAdapter with TabHeader
     //   viewPagerCustomAdapter.addFragment(allProducts, getContext().getString(R.string.all));


        for (int i=0;  i < finalCategoriesList.size();  i++) {
            String title=finalCategoriesList.get(i).getWeekday()+", "+finalCategoriesList.get(i).getDay()+" "+finalCategoriesList.get(i).getMonth();

            // Initialize Category_Products Fragment with specified arguments
            Fragment categoryProducts = new DaySlot();
            Bundle categoryInfo = new Bundle();
            categoryInfo.putString("data", doctorModel);
            categoryInfo.putString("sortBy", finalCategoriesList.get(i).getData());
            categoryInfo.putInt("CategoryID",finalCategoriesList.get(i).getDay());
            categoryInfo.putString("title",title);
            categoryInfo.putString("service_type",sortBy);
            categoryProducts.setArguments(categoryInfo);

           // Add the Fragments to the ViewPagerAdapter with TabHeader
            viewPagerCustomAdapter.addFragment(categoryProducts, title);


            if (selectedTabText.equalsIgnoreCase(title)) {
                selectedTabIndex = i + 1;
            }
        }

    }


    public void RequestDateTimeSlot() {

        Log.e("slot",doctor_id+"_"+sortBy );

        Call<ResponseData<List<DateSlot>>> call = APIClient.getInstance()
                .getDateTimeSlot
                        (
                               doctor_id,sortBy
                        );

        call.enqueue(new Callback<ResponseData<List<DateSlot>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<DateSlot>>> call, retrofit2.Response<ResponseData<List<DateSlot>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        CallData(response.body().getData());
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
            public void onFailure(Call<ResponseData<List<DateSlot>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    private void CallData(List<DateSlot> data) {
        finalCategoriesList.clear();
        finalCategoriesList.addAll(data);
        setupViewPagerAdapter();


        product_viewpager.setOffscreenPageLimit(0);
        product_viewpager.setAdapter(viewPagerCustomAdapter);

        // Add corresponding ViewPagers to TabLayouts
        product_tabs.setupWithViewPager(product_viewpager);


        try {
            product_tabs.getTabAt(selectedTabIndex).select();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}

