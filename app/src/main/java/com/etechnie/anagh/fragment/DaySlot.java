package com.etechnie.anagh.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.anagh.CustomerFragment.AppointmentAmbulanceFragment;
import com.etechnie.anagh.CustomerFragment.AppointmentHospitalFragment;
import com.etechnie.anagh.CustomerFragment.AppointmentPathologyFragment;
import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.adapters.ProductAdapter;
import com.etechnie.anagh.adapters.TimeSlotAdapter;
import com.etechnie.anagh.app.App;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.customs.EndlessRecyclerViewScroll;
import com.etechnie.anagh.customs.FilterDialog;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.filter_model.get_filters.FilterData;
import com.etechnie.anagh.models.filter_model.get_filters.FilterDetails;
import com.etechnie.anagh.models.filter_model.post_filters.PostFilterData;
import com.etechnie.anagh.models.product_model.GetAllProducts;
import com.etechnie.anagh.models.product_model.ProductData;
import com.etechnie.anagh.models.product_model.ProductDetails;
import com.etechnie.anagh.models.slot_model.TimeSlot;
import com.etechnie.anagh.network.APIClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class DaySlot extends Fragment {

    View rootView;
    
    int pageNo = 0;
    double maxPrice = 0;
    boolean isVisible;
    boolean isGridView;
    boolean isFilterApplied;
    List<TimeSlot>  timeSlots;
    int categoryID;
    String customerID;
    String data = "";
    String title="";
    String service_type="";
    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;
    String doctorModel;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    
        isVisible = isVisibleToUser;
    }
    
    
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_timeslot, container, false);

        ButterKnife.bind(this, rootView);
        categoryID = getArguments().getInt("CategoryID");
        timeSlots=new ArrayList<>();
        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());
        // Get sortBy from bundle arguments
        if (getArguments().containsKey("sortBy")) {
            data = getArguments().getString("sortBy");
            if(data!=null && !data.equalsIgnoreCase("") ){
                Type listType = new TypeToken<ArrayList<TimeSlot>>(){}.getType();
                 timeSlots= new Gson().fromJson(data, listType);
            }
        }
        if (getArguments().containsKey("data")) {
            doctorModel =  getArguments().getString("data","");
        }
        if (getArguments().containsKey("title")) {
            title = getArguments().getString("title");

        }
        if (getArguments().containsKey("service_type")) {
            service_type = getArguments().getString("service_type");

        }

        TimeSlotAdapter categoryAdapter = new TimeSlotAdapter(getActivity(), timeSlots, new TimeSlotAdapter.RecyclerTouchListener() {
            @Override
            public void onClickCategoryItem(TimeSlot item) {
                if(service_type.equalsIgnoreCase("Medical")) {
                    Fragment fragment = new AppointmentFragment();
                    Bundle categoryInfo = new Bundle();
                    categoryInfo.putSerializable("timeslot", item);
                    categoryInfo.putString("title", title);
                    categoryInfo.putString("data", doctorModel);
                    categoryInfo.putString("service_type", service_type);
                    fragment.setArguments(categoryInfo);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();
                }
               else if(service_type.equalsIgnoreCase("Pathology")) {
                    Fragment fragment = new AppointmentPathologyFragment();
                    Bundle categoryInfo = new Bundle();
                    categoryInfo.putSerializable("timeslot", item);
                    categoryInfo.putString("title", title);
                    categoryInfo.putString("data", doctorModel);
                    categoryInfo.putString("service_type", service_type);
                    fragment.setArguments(categoryInfo);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();
                }
                else if(service_type.equalsIgnoreCase("Ambulance")) {
                    Fragment fragment = new AppointmentAmbulanceFragment();
                    Bundle categoryInfo = new Bundle();
                    categoryInfo.putSerializable("timeslot", item);
                    categoryInfo.putString("title", title);
                    categoryInfo.putString("data", doctorModel);
                    categoryInfo.putString("service_type", service_type);
                    fragment.setArguments(categoryInfo);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();
                }
                else if(service_type.equalsIgnoreCase("Hospital")) {
                    Fragment fragment = new AppointmentHospitalFragment();
                    Bundle categoryInfo = new Bundle();
                    categoryInfo.putSerializable("timeslot", item);
                    categoryInfo.putString("title", title);
                    categoryInfo.putString("data", doctorModel);
                    categoryInfo.putString("service_type", service_type);
                    fragment.setArguments(categoryInfo);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.main_fragment, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null).commit();
                }
            }
        }, "single");
        recyclerCategory.setAdapter(categoryAdapter);


        return rootView;
    }
    


}