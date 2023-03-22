package com.etechnie.anagh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.adapters.CouponsAdapter;
import com.etechnie.anagh.adapters.OrderedProductsListAdapter;
import com.etechnie.anagh.adapters.ViewPagerCustomAdapter;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.customs.CircularImageView;
import com.etechnie.anagh.customs.DividerItemDecoration;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.coupons_model.CouponsInfo;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.order_model.OrderDetails;
import com.etechnie.anagh.models.order_model.OrderProducts;
import com.etechnie.anagh.models.slot_model.DateSlot;
import com.etechnie.anagh.network.APIClient;
import com.google.android.material.tabs.TabLayout;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class Doctor_Description extends Fragment {

    View rootView;
    
    DoctorModel orderDetails;
    int selectedTabIndex = 0;
    CardView buyer_comments_card, seller_comments_card;
    RecyclerView checkout_items_recycler, checkout_coupons_recycler;
    TextView checkout_subtotal, checkout_tax, checkout_shipping, checkout_discount, checkout_total;
    TextView clinic_name, clinic_address, fees, distance, shipping_address;
    TextView order_price,doctor_degree,doctor_experience,doctor_speciality,doctor_title, order_products_count, order_date, shipping_method, payment_method, buyer_comments, seller_comments, order_status;
    String selectedTabText = "";
    List<CouponsInfo> couponsList;
    List<OrderProducts> orderProductsList;
    List<DateSlot> finalCategoriesList = new ArrayList<>();
    CouponsAdapter couponsAdapter;
    OrderedProductsListAdapter orderedProductsAdapter;
    ViewPagerCustomAdapter viewPagerCustomAdapter;
    TabLayout product_tabs;
    ViewPager product_viewpager;
    List<String> items;
    public StateProgressBar stateProgressBar;
    CircularImageView doctor_image;
    ReadMoreTextView doctor_description;
    Context mContext;
    String slot_type="";
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.doctor_details, container, false);

        ((MainActivity)getActivity()).toggleNavigaiton(false);
        mContext=getContext();
        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        //noInternetDialog.show();
        orderDetails = (DoctorModel) getArguments().getSerializable("data");

        doctor_image = (CircularImageView) rootView.findViewById(R.id.category_image);
        doctor_title = (TextView) rootView.findViewById(R.id.category_title);
        doctor_speciality = (TextView) rootView.findViewById(R.id.category_products);
        doctor_experience = (TextView) rootView.findViewById(R.id.experience);
        doctor_degree = (TextView) rootView.findViewById(R.id.degree);
        clinic_name= (TextView) rootView.findViewById(R.id.clinic_name);
        clinic_address= (TextView) rootView.findViewById(R.id.clinic_address);
        product_tabs = (TabLayout) rootView.findViewById(R.id.product_tabs);
        product_viewpager = (ViewPager) rootView.findViewById(R.id.product_viewpager);
        fees=(TextView) rootView.findViewById(R.id.fees);
        distance=(TextView) rootView.findViewById(R.id.distance);
        doctor_description=(ReadMoreTextView) rootView.findViewById(R.id.doctor_description);
        doctor_speciality.setText(orderDetails.getSpeciality());
        doctor_experience.setText(orderDetails.getExperience()+"");

        // Get orderDetails from bundle arguments
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        // Set OrderProductCategory Image on ImageView with Glide Library
        Glide.with(mContext)
                .setDefaultRequestOptions(options)
                .load(orderDetails.getImage())
                .into(doctor_image);
        doctor_title.setText(orderDetails.getName());
        doctor_degree.setText(orderDetails.getDegree());
        clinic_name.setText(orderDetails.getClinic());
        clinic_address.setText(orderDetails.getClinicAddress());
        fees.setText("₹ "+orderDetails.getFees());
        distance.setText("5 km");
        doctor_description.setText(orderDetails.getDescription());
        RequestDateTimeSlot();
        return rootView;

    }




    public void RequestDateTimeSlot() {



        Call<ResponseData<List<DateSlot>>> call = APIClient.getInstance()
                .getDateTimeSlot
                        (
                                orderDetails.getId(),slot_type
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

            // Initialize Category_Products Fragment with specified arguments
            Fragment categoryProducts = new DaySlot();
            Bundle categoryInfo = new Bundle();
            categoryInfo.putString("sortBy", finalCategoriesList.get(i).getData());
            categoryInfo.putInt("CategoryID",finalCategoriesList.get(i).getDay());
            categoryProducts.setArguments(categoryInfo);

            String title=finalCategoriesList.get(i).getWeekday()+", "+finalCategoriesList.get(i).getDay()+" "+finalCategoriesList.get(i).getMonth();
            // Add the Fragments to the ViewPagerAdapter with TabHeader
            viewPagerCustomAdapter.addFragment(categoryProducts, title);


            if (selectedTabText.equalsIgnoreCase(title)) {
                selectedTabIndex = i + 1;
            }
        }

    }

}



