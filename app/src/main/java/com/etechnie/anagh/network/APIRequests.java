package com.etechnie.anagh.network;


import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.address_model.AddressData;
import com.etechnie.anagh.models.address_model.AddressDetails;
import com.etechnie.anagh.models.address_model.Countries;
import com.etechnie.anagh.models.address_model.Zones;
import com.etechnie.anagh.models.ambulance_model.AmbulanceModel;
import com.etechnie.anagh.models.banner_model.BannerData;
import com.etechnie.anagh.models.banner_model.BannerDetails;
import com.etechnie.anagh.models.category_model.CategoryData;
import com.etechnie.anagh.models.chamber_model.ChamberModel;
import com.etechnie.anagh.models.clinic_model.ClinicModel;
import com.etechnie.anagh.models.contact_model.ContactUsData;
import com.etechnie.anagh.models.currency_model.CurrencyModel;
import com.etechnie.anagh.models.device_model.AppSettingsData;
import com.etechnie.anagh.models.doctor_model.DoctorModel;
import com.etechnie.anagh.models.doctor_model.RequestDoctor;
import com.etechnie.anagh.models.doctor_model.RequestDoctorAppointment;
import com.etechnie.anagh.models.filter_model.get_filters.FilterData;
import com.etechnie.anagh.models.filter_model.post_filters.FilterModel;
import com.etechnie.anagh.models.googleMap.GoogleAPIResponse;
import com.etechnie.anagh.models.hospital_model.HospitalModel;
import com.etechnie.anagh.models.language_model.LanguageData;
import com.etechnie.anagh.models.login_model.PostLogin;
import com.etechnie.anagh.models.login_model.ResponseLogin;
import com.etechnie.anagh.models.medical_problem_model.MedicalProblemModel;
import com.etechnie.anagh.models.medical_type_model.MedicalTypeModel;
import com.etechnie.anagh.models.news_model.all_news.NewsData;
import com.etechnie.anagh.models.news_model.news_categories.NewsCategoryData;
import com.etechnie.anagh.models.pages_model.PagesData;
import com.etechnie.anagh.models.pathology_model.PathologyModel;
import com.etechnie.anagh.models.pay_tm.Checksum;
import com.etechnie.anagh.models.prescrition_order.PrescriptionHistory;
import com.etechnie.anagh.models.product_model.GetAllProducts;
import com.etechnie.anagh.models.coupons_model.CouponsData;
import com.etechnie.anagh.models.payment_model.PaymentMethodsData;
import com.etechnie.anagh.models.product_model.GetStock;
import com.etechnie.anagh.models.product_model.ProductStock;
import com.etechnie.anagh.models.ratings.GetRatings;
import com.etechnie.anagh.models.ratings.GiveRating;
import com.etechnie.anagh.models.response_document.Document;
import com.etechnie.anagh.models.service_model.ServiceModel;
import com.etechnie.anagh.models.shipping_model.PostTaxAndShippingData;
import com.etechnie.anagh.models.order_model.OrderData;
import com.etechnie.anagh.models.payment_model.GetBrainTreeToken;
import com.etechnie.anagh.models.order_model.PostOrder;
import com.etechnie.anagh.models.product_model.ProductData;
import com.etechnie.anagh.models.search_model.SearchData;
import com.etechnie.anagh.models.shipping_model.ShippingRateData;
import com.etechnie.anagh.models.slot_model.DateSlot;
import com.etechnie.anagh.models.slot_model.DoctorSlot;
import com.etechnie.anagh.models.subscription_model.SubscriptionModel;
import com.etechnie.anagh.models.uploadimage.UploadImageModel;
import com.etechnie.anagh.models.user_model.UserData;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * APIRequests contains all the Network Request Methods with relevant API Endpoints
 **/

public interface APIRequests {

    //******************** User Data ********************//

    @Multipart
    @POST("processregistration")
    Call<UserData> processRegistration(
            @Part("customers_firstname") RequestBody customers_firstname,
            @Part("customers_lastname") RequestBody customers_lastname,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("country_code") RequestBody country_code,
            @Part("customers_telephone") RequestBody customers_picture);

    @FormUrlEncoded
    @POST("processlogin")
    Call<UserData> processLogin(@Field("email") String customers_email_address,
                                @Field("password") String customers_password);

    @FormUrlEncoded
    @POST("facebookregistration")
    Call<UserData> facebookRegistration(@Field("access_token") String access_token);

    @FormUrlEncoded
    @POST("googleregistration")
    Call<UserData> googleRegistration(@Field("idToken") String idToken,
                                      @Field("customers_id") String userId,
                                      @Field("givenName") String givenName,
                                      @Field("familyName") String familyName,
                                      @Field("email") String email,
                                      @Field("imageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("processforgotpassword")
    Call<UserData> processForgotPassword(@Field("email") String customers_email_address);

    @FormUrlEncoded
    @POST("updatecustomerinfo")
    Call<UserData> updateCustomerInfo(@Field("customers_id") String customers_id,
                                      @Field("customers_firstname") String customers_firstname,
                                      @Field("customers_lastname") String customers_lastname,
                                      @Field("customers_gender") String customers_gender,
                                      @Field("customers_telephone") String customers_telephone,
                                      @Field("customers_dob") String customers_dob,
                                      @Field("image_id") String image_id);


    //******************** Address Data ********************//

    @POST("getcountries")
    Call<Countries> getCountries();

    @FormUrlEncoded
    @POST("getzones")
    Call<Zones> getZones(@Field("zone_country_id") String zone_country_id);

    @FormUrlEncoded
    @POST("getalladdress")
    Call<AddressData> getAllAddress(@Field("customers_id") String customers_id);

    @FormUrlEncoded
    @POST("addshippingaddress")
    Call<AddressData> addUserAddress(@Field("customers_id") String customers_id,
                                     @Field("entry_firstname") String entry_firstname,
                                     @Field("entry_lastname") String entry_lastname,
                                     @Field("entry_street_address") String entry_street_address,
                                     @Field("entry_postcode") String entry_postcode,
                                     @Field("entry_city") String entry_city,
                                     @Field("entry_country_id") String entry_country_id,
                                     @Field("entry_zone_id") String entry_zone_id,
                                     @Field("is_default") String customers_default_address_id);

    @FormUrlEncoded
    @POST("updateshippingaddress")
    Call<AddressData> updateUserAddress(@Field("customers_id") String customers_id,
                                        @Field("address_id") String address_id,
                                        @Field("entry_firstname") String entry_firstname,
                                        @Field("entry_lastname") String entry_lastname,
                                        @Field("entry_street_address") String entry_street_address,
                                        @Field("entry_postcode") String entry_postcode,
                                        @Field("entry_city") String entry_city,
                                        @Field("entry_country_id") String entry_country_id,
                                        @Field("entry_zone_id") String entry_zone_id,
                                        @Field("is_default") String customers_default_address_id);

    @FormUrlEncoded
    @POST("updatedefaultaddress")
    Call<AddressData> updateDefaultAddress(@Field("customers_id") String customers_id,
                                           @Field("address_book_id") String address_book_id);

    @FormUrlEncoded
    @POST("deleteshippingaddress")
    Call<AddressData> deleteUserAddress(@Field("customers_id") String customers_id,
                                        @Field("address_book_id") String address_book_id);


    //******************** OrderProductCategory Data ********************//

    @FormUrlEncoded
    @POST("allcategories")
    Call<CategoryData> getAllCategories(@Field("language_id") int language_id);

    //******************** Product Data ********************//

    @POST("getallproducts")
    Call<ProductData> getAllProducts(@Body GetAllProducts getAllProducts);

    @POST("getquantity")
    Call<ProductStock> getProductStock(@Body GetStock getStock);

    @FormUrlEncoded
    @POST("likeproduct")
    Call<ProductData> likeProduct(@Field("liked_products_id") int liked_products_id,
                                  @Field("liked_customers_id") String liked_customers_id);

    @FormUrlEncoded
    @POST("unlikeproduct")
    Call<ProductData> unlikeProduct(@Field("liked_products_id") int liked_products_id,
                                    @Field("liked_customers_id") String liked_customers_id);

    @FormUrlEncoded
    @POST("getfilters")
    Call<FilterData> getFilters(@Field("categories_id") int categories_id,
                                @Field("language_id") int language_id);

    @FormUrlEncoded
    @POST("getsearchdata")
    Call<SearchData> getSearchData(@Field("searchValue") String searchValue,
                                   @Field("language_id") int language_id,
                                   @Field("currency_code") String currency_code);

    //******************** News Data ********************//

    @FormUrlEncoded
    @POST("getallnews")
    Call<NewsData> getAllNews(@Field("language_id") int language_id,
                              @Field("page_number") int page_number,
                              @Field("is_feature") int is_feature,
                              @Field("categories_id") String categories_id);

    @FormUrlEncoded
    @POST("allnewscategories")
    Call<NewsCategoryData> allNewsCategories(@Field("language_id") int language_id,
                                             @Field("page_number") int page_number);

    //******************** Order Data ********************//

    @POST("addtoorder")
    Call<OrderData> addToOrder(@Body PostOrder postOrder);

    @FormUrlEncoded
    @POST("getorders")
    Call<OrderData> getOrders(@Field("customers_id") String customers_id,
                              @Field("language_id") int language_id,
                              @Field("currency_code") String currency_code);

    @FormUrlEncoded
    @POST("updatestatus")
    Call<OrderData> updatestatus(@Field("customers_id") String customers_id,
                                 @Field("orders_id") int orders_id);

    @FormUrlEncoded
    @POST("getcoupon")
    Call<CouponsData> getCouponInfo(@Field("code") String code);

    @FormUrlEncoded
    @POST("getpaymentmethods")
    Call<PaymentMethodsData> getPaymentMethods(@Field("language_id") int language_id);

    @GET("generatebraintreetoken")
    Call<GetBrainTreeToken> generateBraintreeToken();

    //******************** Banner Data ********************//

    @GET("getbanners")
    Call<BannerData> getBanners();

    //******************** Tax & Shipping Data ********************//

    @POST("getrate")
    Call<ShippingRateData> getShippingMethodsAndTax(@Body PostTaxAndShippingData postTaxAndShippingData);

    //******************** Contact Us Data ********************//

    @FormUrlEncoded
    @POST("contactus")
    Call<ContactUsData> contactUs(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("message") String message);

    //******************** Languages Data ********************//

    @GET("getlanguages")
    Call<LanguageData> getLanguages();

    //******************** App Settings Data ********************//

    @GET("sitesetting")
    Call<AppSettingsData> getAppSetting();


    //******************** Static Pages Data ********************//

    @FormUrlEncoded
    @POST("getallpages")
    Call<PagesData> getStaticPages(@Field("language_id") int language_id);

    //******************** Notifications Data ********************//

    @FormUrlEncoded
    @POST("registerdevices")
    Call<UserData> registerDeviceToFCM(@Field("device_id") String device_id,
                                       @Field("device_type") String device_type,
                                       @Field("ram") String ram,
                                       @Field("processor") String processor,
                                       @Field("device_os") String device_os,
                                       @Field("location") String location,
                                       @Field("device_model") String device_model,
                                       @Field("manufacturer") String manufacturer,
                                       @Field("customers_id") String customers_id);

    @FormUrlEncoded
    @POST("notify_me")
    Call<ContactUsData> notify_me(@Field("is_notify") String is_notify,
                                  @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("givereview")
    Call<GiveRating> giveRating(@FieldMap Map<String, String> stringMap);

    @GET("getreviews")
    Call<GetRatings> getProductReviews(@Query("products_id") String product_id,
                                       @Query("languages_id") String languages_id);

    // This Api will give us City bounds
    @GET("getlocation")
    Call<GoogleAPIResponse> getCityBounds(@Query(value = "address", encoded = true) String address);

    // Upload Image
    @Multipart
    @POST("uploadimage")
    Call<UploadImageModel> uploadImage(@Part MultipartBody.Part filePart);

    // Update Password
    @GET("updatepassword")
    Call<UserData> updatePassword(@Query("oldpassword") String oldPassword,
                                  @Query("newpassword") String newPassword,
                                  @Query("customers_id") String customers_id);

    //Change Currency
    @GET("getcurrencies")
    Call<CurrencyModel> getCurrency();

    @GET("generatpaytmhashes")
    Call<Checksum> getPayTMChecksum(
            @Query("customers_id") String customerID,
            @Query("amount") String totalAmount
    );

    @POST("SP/sp_app_get_medical_type")
    Call<ResponseData<List<MedicalTypeModel>>> getMedicalType();

    @POST("SP/sp_app_get_banner")
    Call<ResponseData<List<BannerDetails>>> getBanner();

    @POST("SP/sp_app_get_doctor")
    Call<ResponseData<List<DoctorModel>>> getDoctor(@Query("problem_id") Integer problem_id,
                                                    @Query("location_id") Integer location_id);
    @POST("SP/sp_app_get_service")
    Call<ResponseData<List<ServiceModel>>> getService();

    @POST("Upload/upload")
    @Multipart
    Call<JsonObject> uploadMultiFile( @Part List<MultipartBody.Part> files,@Part("uid") RequestBody uid, @Part("Full_Address") RequestBody address, @Part("d_charge") RequestBody d_charge, @Part("size") RequestBody size);

    @POST("Token")
    Call<ResponseData<ResponseLogin>> login(@Body PostLogin data);

    @POST("SP/sp_app_get_prescription_order")
    Call<ResponseData<List<PrescriptionHistory>>> getPrescriptionOrder(@Query("id") Integer user_id);

    @POST("SP/sp_app_get_doctor_time_slot")
    Call<ResponseData<List<DateSlot>>> getDateTimeSlot(@Query("doctor_id") Integer doctor_id,
                                                       @Query("type") String type);


    @POST("SP/sp_app_create_address")
    Call<ResponseData<Integer>> createAddress(@Query("id") Integer id,@Query("userid") Integer userid, @Query("address") String address,
                                              @Query("postcode") String postcode, @Query("Landmark") String Landmark,@Query("address_type") String address_type,
                                              @Query("latitude") Double latitude, @Query("longitude") Double longitude);

    @POST("SP/sp_app_get_address")
    Call<ResponseData<List<AddressDetails>>> getAddress(@Query("userid") Integer userid);

    @POST("SP/request_appointment")
    Call<ResponseData<Integer>> RequestAppointment(@Body RequestDoctorAppointment data);

    @POST("SP/sp_app_get_all_doctor")
    Call<ResponseData<List<DoctorModel>>> getAllDoctor();

    @GET("Chamber_type")
    Call<ResponseData<Document<ChamberModel>>> getAllChamber(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );

    @GET("Clinic")
    Call<ResponseData<Document<ClinicModel>>> getAllClinic(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );

    @DELETE("Clinic/id")
    Call<ResponseData<String>> DeleteClinic( @Query("id") Integer id);


    @POST("Doctor")
    Call<ResponseData<Integer>> RequestDoctor(@Body RequestDoctor data);
    @PUT("Doctor/id")
    Call<ResponseData<Integer>> UpdateDoctor( @Query("id") Integer id,@Body RequestDoctor data);

    @DELETE("Doctor/id")
    Call<ResponseData<String>> DeleteDoctor( @Query("id") Integer id);

    @GET("Medical_problem")
    Call<ResponseData<Document<MedicalProblemModel>>> getAllMedicalproblem(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );



    @GET("Time_slot")
    Call<ResponseData<Document<DoctorSlot>>> getAllDoctorSlot(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );
    @POST("Time_slot")
    Call<ResponseData<Integer>> RequestDoctorSlot(@Body DoctorSlot data);

    @DELETE("Time_slot/id")
    Call<ResponseData<Object>> DeleteDoctorSlot( @Query("id") Integer id);

    @POST("Time_slot/filter")
    Call<ResponseData<Document<DoctorSlot>>> getFilterDoctorSlot(
            @Query("andOr") String andOr,
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage,
            @Body List<FilterModel> data
    );

    @GET("Subscription")
    Call<ResponseData<Document<SubscriptionModel>>> getAllSubscription(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );


    @POST("Clinic")
    Call<ResponseData<Integer>> RequestClinic(@Body ClinicModel data);

    @POST("Subscription")
    Call<ResponseData<Integer>> RequestSubscription(@Body SubscriptionModel data);

    @POST("Ambulance")
    Call<ResponseData<Integer>> RequestAmbulance(@Body AmbulanceModel data);

    @POST("Hospital")
    Call<ResponseData<Integer>> RequestHospital(@Body HospitalModel data);
    @DELETE("Hospital/id")
    Call<ResponseData<String>> DeleteHospital( @Query("id") Integer id);
    @GET("Hospital")
    Call<ResponseData<Document<HospitalModel>>> getAllHospital(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );
    @GET("Ambulance")
    Call<ResponseData<Document<AmbulanceModel>>> getAllAmbulance(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );

    @DELETE("Ambulance/id")
    Call<ResponseData<String>> DeleteAmbulance( @Query("id") Integer id);

    @GET("Pathology")
    Call<ResponseData<Document<PathologyModel>>> getAllPathology(
            @Query("page") Integer page,
            @Query("itemsPerPage") Integer itemsPerPage
    );

    @POST("Pathology")
    Call<ResponseData<Integer>> RequestPathology(@Body PathologyModel data);
}
