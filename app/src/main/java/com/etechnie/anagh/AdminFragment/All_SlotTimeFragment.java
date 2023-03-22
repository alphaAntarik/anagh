package com.etechnie.anagh.AdminFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.etechnie.anagh.R;
import com.etechnie.anagh.adapters.ViewPagerCustomAdapter;
import com.etechnie.anagh.fragment.DaySlot;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.category_model.CategoryDetails;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.filter_model.post_filters.FilterModel;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.models.slot_model.DateSlot;
import com.etechnie.anagh.models.slot_model.DoctorSlot;
import com.etechnie.anagh.network.APIClient;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;


public class All_SlotTimeFragment extends Fragment {

    String sortBy = "Newest";
    boolean isMenuItem = false;
    boolean isSubFragment = false;
    int parentCategoryID;
    int selectedTabIndex = 0;
    String selectedTabText = "";

    TabLayout product_tabs;
    ViewPager product_viewpager;
int doctor_id=0;
String service_type="";
    ViewPagerCustomAdapter viewPagerCustomAdapter;
    int id=0;
    List<CategoryDetails> allCategoriesList = new ArrayList<>();
    List<CategoryDetails> allSubCategoriesList = new ArrayList<>();
    List<DoctorSlot> finalCategoriesList = new ArrayList<>();
    DoctorModel doctorModel;
    ArrayList<String> mStringList = new ArrayList<String>();
    public void invalidateProducts(){
        viewPagerCustomAdapter.notifyDataSetChanged();
    }
    AppCompatButton add;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_slottime_fragment, container, false);

        if (getArguments() != null) {

            if (getArguments().containsKey("id")) {
                id = getArguments().getInt("id");
            }
            if (getArguments().containsKey("service_type")) {
                service_type = getArguments().getString("service_type");
            }
        }
        add=(AppCompatButton) rootView.findViewById(R.id.add);
        add.setText("Create "+service_type+ "Slot");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("service_type", service_type);
                bundle.putInt("id", id);
                bundle.putInt("sortBy", 1);
                bundle.putBoolean("isMenuItem", true);
                Fragment fragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // Navigate to Products Fragment
                fragment = new Create_DoctorSlot();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(getString(R.string.actionHome)).commit();

            }
        });



        mStringList.add("Monday");
        mStringList.add("Tuesday");
        mStringList.add("Wednesday");
        mStringList.add("Thursday");
        mStringList.add("Friday");
        mStringList.add("Saturday");
        mStringList.add("Sunday");
        FetchDoctorSlot();
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


        for (int i=0;  i < mStringList.size();  i++) {
            String title=mStringList.get(i);

            // Initialize Category_Products Fragment with specified arguments
            Fragment categoryProducts = new All_DoctorSlot();
            Bundle categoryInfo = new Bundle();
            List<DoctorSlot>  doctorSlotList=   finalCategoriesList.stream()
                    .filter(c -> c.getDay().equalsIgnoreCase(title))
                    .collect(Collectors.toList());
            categoryInfo.putString("data",new Gson().toJson(doctorSlotList) );
            categoryInfo.putString("service_type",service_type);
            categoryInfo.putString("title",title);
            categoryProducts.setArguments(categoryInfo);

           // Add the Fragments to the ViewPagerAdapter with TabHeader
            viewPagerCustomAdapter.addFragment(categoryProducts, title);


            if (selectedTabText.equalsIgnoreCase(title)) {
                selectedTabIndex = i + 1;
            }
        }

    }


    public void FetchDoctorSlot() {

        List<FilterModel> filterModelList=new ArrayList<>();
        FilterModel filterModel=new FilterModel();
        filterModel.setColumnName("doctor_id");
        filterModel.setColumnValue(String.valueOf(id));
        filterModelList.add(filterModel);
        filterModel=new FilterModel();
        filterModel.setColumnName("slot_type");
        filterModel.setColumnValue("'"+service_type+"'");
        filterModelList.add(filterModel);

        Call<ResponseData<Document<DoctorSlot>>> call = APIClient.getInstance()
                .getFilterDoctorSlot("AND",1,10,filterModelList)
                ;

        call.enqueue(new Callback<ResponseData<Document<DoctorSlot>>>() {
            @Override
            public void onResponse(Call<ResponseData<Document<DoctorSlot>>> call, retrofit2.Response<ResponseData<Document<DoctorSlot>>> response) {

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
            public void onFailure(Call<ResponseData<Document<DoctorSlot>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    private void CallData(List<DoctorSlot> data) {
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

