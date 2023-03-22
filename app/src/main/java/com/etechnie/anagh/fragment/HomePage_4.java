package com.etechnie.anagh.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etechnie.anagh.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.etechnie.anagh.app.App;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.banner_model.BannerDetails;
import com.etechnie.anagh.models.category_model.CategoryData;
import com.etechnie.anagh.models.category_model.CategoryDetails;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.etechnie.anagh.models.medical_type_model.MedicalTypeModel;
import com.etechnie.anagh.models.service_model.ServiceModel;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.network.StartAppRequests;
import com.etechnie.anagh.utils.Utilities;
import com.google.gson.Gson;

public class HomePage_4 extends Fragment {


    StartAppRequests startAppRequests;
    List<BannerDetails> bannerImages = new ArrayList<>();
    List<CategoryDetails> allCategoriesList = new ArrayList<>();
    List<MedicalTypeModel> medicalTypeModelList = new ArrayList<>();
    FragmentManager fragmentManager;
    List<ServiceModel> allserviceList = new ArrayList<>();
    Top_Seller topSeller;
    Special_Deals specialDeals;
    Most_Liked mostLiked;
    RecentlyViewed recentlyViewed;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (topSeller != null && specialDeals != null && mostLiked != null && recentlyViewed != null) {
                topSeller.invalidateProducts();
                specialDeals.invalidateProducts();
                mostLiked.invalidateProducts();
                recentlyViewed.invalidateProducts();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.homepage_4, container, false);

        // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
        //MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(ConstantValues.APP_HEADER);

        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(getContext()).build();
        // noInternetDialog.show();

        startAppRequests = new StartAppRequests(getContext());
        // Get BannersList from ApplicationContext
        bannerImages = ((App) getContext().getApplicationContext()).getBannersList();
        medicalTypeModelList = ((App) getContext().getApplicationContext()).getMedicalTypeList();

        allserviceList=((App) getContext().getApplicationContext()).getServiceList();
        fragmentManager = getFragmentManager();


        // Add Top_Seller Fragment to specified FrameLayout
//        topSeller = new Top_Seller();
//        topSeller.setArguments(bundle);
//        fragmentManager.beginTransaction().replace(R.id.home4_top_seller_fragment, topSeller).commit();

        // Add Special_Deals Fragment to specified FrameLayout
//        specialDeals = new Special_Deals();
//        specialDeals.setArguments(bundle);
//        fragmentManager.beginTransaction().replace(R.id.home4_super_deals_fragment, specialDeals).commit();

        // Add Most_Liked Fragment to specified FrameLayout
//        mostLiked = new Most_Liked();
//        mostLiked.setArguments(bundle);
//        fragmentManager.beginTransaction().replace(R.id.home4_most_liked_fragment, mostLiked).commit();

        // Add Recently Viewes products to specified FrameLayout
//        RecentlyViewed recentlyViewed = new RecentlyViewed();
//        fragmentManager.beginTransaction().replace(R.id.home4_recentProducts, recentlyViewed).commit();

        if (!medicalTypeModelList.isEmpty() && !bannerImages.isEmpty() && !allserviceList.isEmpty()) {
            continueSetup();
        } else {
            if (medicalTypeModelList.isEmpty()) {
                RequestMyMedical();
            }
            if (bannerImages.isEmpty()) {
                RequestBanner();
            }
            if (allserviceList.isEmpty()) {
                RequestService();
            }
        }


        return rootView;

    }

    private void continueSetup() {
        // Setup BannerSlider
        setupBannerSlider(bannerImages);


        // Add MainCategories Fragment to specified FrameLayout
        Bundle categoryBundle = new Bundle();
        categoryBundle.putBoolean("isHeaderVisible", false);
        categoryBundle.putBoolean("isMenuItem", false);
        Fragment categories = new ServiceFragment();
        categories.setArguments(categoryBundle);
        fragmentManager.beginTransaction().replace(R.id.home4_all_categories_fragment, categories).commit();

    }

    //*********** Setup the BannerSlider with the given List of BannerImages ********//

    private void setupBannerSlider(final List<BannerDetails> bannerImages) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment bannerStyle = null;

        switch (ConstantValues.DEFAULT_BANNER_STYLE) {
            case 0:
                bannerStyle = new BannerStyle1(bannerImages, allCategoriesList);
                break;
            case 1:
                bannerStyle = new BannerStyle1(bannerImages, allCategoriesList);
                break;
            case 2:
                bannerStyle = new BannerStyle2(bannerImages, allCategoriesList);
                break;
            case 3:
                bannerStyle = new BannerStyle3(bannerImages, allCategoriesList);
                break;
            case 4:
                bannerStyle = new BannerStyle4(bannerImages, allCategoriesList);
                break;
            case 5:
                bannerStyle = new BannerStyle5(bannerImages, allCategoriesList);
                break;
            case 6:
                bannerStyle = new BannerStyle6(bannerImages, allCategoriesList);
                break;
        }

        if (bannerStyle != null)
            fragmentManager.beginTransaction().replace(R.id.bannerFrameHome4, bannerStyle).commit();
    }


    public void RequestMyMedical() {


        Call<ResponseData<List<MedicalTypeModel>>> call = APIClient.getInstance()
                .getMedicalType
                        (

                        );

        call.enqueue(new Callback<ResponseData<List<MedicalTypeModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<MedicalTypeModel>>> call, retrofit2.Response<ResponseData<List<MedicalTypeModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        ((App) getContext().getApplicationContext()).setMedicalTypeList(response.body().getData());
                    } else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        // emptyRecord.setVisibility(View.VISIBLE);
                        //  Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    } else {
                    }
                } else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<List<MedicalTypeModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    public void RequestBanner() {


        Call<ResponseData<List<BannerDetails>>> call = APIClient.getInstance()
                .getBanner
                        (

                        );

        call.enqueue(new Callback<ResponseData<List<BannerDetails>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<BannerDetails>>> call, retrofit2.Response<ResponseData<List<BannerDetails>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        ((App) getContext().getApplicationContext()).setBannersList(response.body().getData());
                        bannerImages = ((App) getContext().getApplicationContext()).getBannersList();
                        setupBannerSlider(bannerImages);

                    } else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        // emptyRecord.setVisibility(View.VISIBLE);
                        //  Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    } else {
                        // Unable to get Success status
                        //emptyRecord.setVisibility(View.VISIBLE);
                        // Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<List<BannerDetails>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }

    public void RequestService() {


        Call<ResponseData<List<ServiceModel>>> call = APIClient.getInstance()
                .getService
                        (

                        );

        call.enqueue(new Callback<ResponseData<List<ServiceModel>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ServiceModel>>> call, retrofit2.Response<ResponseData<List<ServiceModel>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        // Orders have been returned. Add Orders to the ordersList
                        ((App) getContext().getApplicationContext()).setServiceList(response.body().getData());
                        Bundle categoryBundle = new Bundle();
                        categoryBundle.putBoolean("isHeaderVisible", false);
                        categoryBundle.putBoolean("isMenuItem", false);
                        Fragment categories = new ServiceFragment();
                        categories.setArguments(categoryBundle);
                        fragmentManager.beginTransaction().replace(R.id.home4_all_categories_fragment, categories).commit();


                    } else if (String.valueOf(response.body().getCode()).equalsIgnoreCase("0")) {
                        // emptyRecord.setVisibility(View.VISIBLE);
                        //  Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    } else {
                        // Unable to get Success status
                        //emptyRecord.setVisibility(View.VISIBLE);
                        // Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    // Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                //dialogLoader.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseData<List<ServiceModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }
}

