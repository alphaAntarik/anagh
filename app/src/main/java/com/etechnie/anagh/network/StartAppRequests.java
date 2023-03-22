package com.etechnie.anagh.network;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.banner_model.BannerDetails;
import com.etechnie.anagh.models.medical_type_model.MedicalTypeModel;
import com.etechnie.anagh.models.service_model.ServiceModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.etechnie.anagh.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.List;

import com.etechnie.anagh.app.App;
import com.etechnie.anagh.utils.Utilities;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.models.user_model.UserData;
import com.etechnie.anagh.models.banner_model.BannerData;
import com.etechnie.anagh.models.category_model.CategoryData;
import com.etechnie.anagh.models.device_model.AppSettingsData;
import com.etechnie.anagh.models.device_model.DeviceInfo;
import com.etechnie.anagh.models.pages_model.PagesDetails;
import com.etechnie.anagh.models.pages_model.PagesData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * StartAppRequests contains some Methods and API Requests, that are Executed on Application Startup
 **/

public class StartAppRequests {

    private App app = new App();


    public StartAppRequests(Context context) {
        app = ((App) context.getApplicationContext());
    }



    //*********** Contains all methods to Execute on Startup ********//

    public void StartRequests(){

        //RequestMyMedical();
        //RequestAllCategories();
       // RequestService();
       // RequestBanner();
      // RequestMyMedical();
        
    }
    
    
    
    //*********** Register Device to Admin Panel with the Device's Info ********//
    
    public static void RegisterDeviceForFCM(final Context context) {
    
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
    
        String deviceID = "";
        DeviceInfo device = Utilities.getDeviceInfo(context);
        String customer_ID = sharedPreferences.getString("userID", "");
        
        
        if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("onesignal")) {
            deviceID = OneSignal.getPermissionSubscriptionState ().getSubscriptionStatus().getUserId();
        }
        else if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("fcm")) {
            deviceID = FirebaseInstanceId.getInstance().getToken();
        }
        
        
        
        Call<UserData> call = APIClient.getInstance()
                .registerDeviceToFCM
                        (
                                deviceID,
                                device.getDeviceType(),
                                device.getDeviceRAM(),
                                device.getDeviceProcessors(),
                                device.getDeviceAndroidOS(),
                                device.getDeviceLocation(),
                                device.getDeviceModel(),
                                device.getDeviceManufacturer(),
                                customer_ID
                        );
        
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        
                        Log.i("notification", response.body().getMessage());
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    
                    }
                    else {
                        
                        Log.i("notification", response.body().getMessage());
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.i("notification", response.errorBody().toString());
                }
            }
            
            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
//                Toast.makeText(context, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
        
    }



    //*********** API Request Method to Fetch App Banners ********//

    public void RequestBanners() {
    
        Call<BannerData> call = APIClient.getInstance()
                .getBanners();
    
        try {
            Response<BannerData> response = call.execute();
    
            BannerData bannerData = new BannerData();
        
            if (response.isSuccessful()) {
    
                bannerData = response.body();
    
                if (!TextUtils.isEmpty(bannerData.getSuccess()))
                    app.setBannersList(bannerData.getData());
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    //*********** API Request Method to Fetch All Categories ********//
    
    public void RequestAllCategories() {
    
        Call<CategoryData> call = APIClient.getInstance()
                .getAllCategories
                        (
                                ConstantValues.LANGUAGE_ID
                        );
        
        try {
            Response<CategoryData> response = call.execute();

            CategoryData categoryData = new CategoryData();
            
            if (response.isSuccessful()) {

                String json= new Gson().toJson(response.body());
                categoryData = response.body();

                if (!TextUtils.isEmpty(categoryData.getSuccess()))
                    app.setCategoriesList(categoryData.getData());
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }



    //*********** Request App Settings from the Server ********//
    
    private void RequestAppSetting() {
    
        Call<AppSettingsData> call = APIClient.getInstance()
                .getAppSetting();
    
        try {
            Response<AppSettingsData> response = call.execute();
        
            if (response.isSuccessful()) {
    

                AppSettingsData appSettingsData = null;
    
                appSettingsData = response.body();
                String strJson = new Gson().toJson(appSettingsData);
                if (!TextUtils.isEmpty(appSettingsData.getSuccess()))
                    app.setAppSettingsDetails(appSettingsData.getAppDetails());

            }
           else {
               
               Log.e("Appsettings","Response is not successful");
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    //*********** Request Static Pages Data from the Server ********//
    
    private void RequestStaticPagesData() {
    
        ConstantValues.ABOUT_US = app.getString(R.string.lorem_ipsum);
        ConstantValues.TERMS_SERVICES = app.getString(R.string.lorem_ipsum);
        ConstantValues.PRIVACY_POLICY = app.getString(R.string.lorem_ipsum);
        ConstantValues.REFUND_POLICY = app.getString(R.string.lorem_ipsum);
        ConstantValues.A_Z = app.getString(R.string.lorem_ipsum);
    
    
        Call<PagesData> call = APIClient.getInstance()
                .getStaticPages
                        (
                                ConstantValues.LANGUAGE_ID
                        );
    
        try {
            Response<PagesData> response = call.execute();
    
            PagesData pages = new PagesData();
            
            if (response.isSuccessful()) {
                
                pages = response.body();
    
                if (pages.getSuccess().equalsIgnoreCase("1")) {
        
                    app.setStaticPagesDetails(pages.getPagesData());
        
                    for (int i=0;  i<pages.getPagesData().size();  i++) {
                        PagesDetails page = pages.getPagesData().get(i);
            
                        if (page.getSlug().equalsIgnoreCase("about-us")) {
                            ConstantValues.ABOUT_US = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("term-services")) {
                            ConstantValues.TERMS_SERVICES = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("privacy-policy")) {
                            ConstantValues.PRIVACY_POLICY = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("refund-policy")) {
                            ConstantValues.REFUND_POLICY = page.getDescription();
                        }
                        else if(page.getSlug().equalsIgnoreCase("A-Z")){
                            ConstantValues.A_Z = page.getDescription();
                        }
                    }
                }
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
                        app.setMedicalTypeList(response.body().getData());
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
                        app.setBannersList(response.body().getData());
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
                        app.setServiceList(response.body().getData());
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
            public void onFailure(Call<ResponseData<List<ServiceModel>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }
    
}
