package com.etechnie.anagh.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etechnie.anagh.R;
import com.etechnie.anagh.app.MyAppPrefsManager;
import com.etechnie.anagh.locationpick.LocationGetActivity;
import com.etechnie.anagh.locationpick.MapUtility;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.address_model.AddressDetails;
import com.etechnie.anagh.models.medical_type_model.MedicalTypeModel;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.CustPrograssbar;
import com.etechnie.anagh.utils.Utilities;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class AddressActivity extends AppCompatActivity {
    @BindView(R.id.lvl_myaddress)
    LinearLayout lvlMyAddress;

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_actiontitle)
    TextView txtActionTitle;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;

    LinearLayoutManager layoutManager;
    public static boolean changeAddress = false;
    MyAppPrefsManager myAppPrefsManager;

    CustPrograssbar custPrograssbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        this.myAppPrefsManager = new MyAppPrefsManager(this);
        layoutManager = new LinearLayoutManager(AddressActivity.this, LinearLayoutManager.VERTICAL, false);
        myRecyclerView.setLayoutManager(layoutManager);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utilities.hasGPSDevice(AddressActivity.this)) {
            Toast.makeText(AddressActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            Utilities.enableLoc(AddressActivity.this);
        }

        getAddress();
    }

    @OnClick({R.id.lvl_clocation, R.id.lvl_myaddress, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_clocation:

                startActivity(new Intent(AddressActivity.this, LocationGetActivity.class)
                        .putExtra(MapUtility.latitude, 0.0)
                        .putExtra(MapUtility.longitude, 0.0)
//                        .putExtra("landmark","")
//                        .putExtra("hno", "")
                        .putExtra("atype", "Home")
                        .putExtra("newuser", "curruntlat")
                        .putExtra("userid", 1)
                        .putExtra("aid", "0"));
                break;
            case R.id.lvl_myaddress:
                break;
            case R.id.img_back:

                    finish();

                break;
            default:
                break;
        }
    }


    public void getAddress() {



        Call<ResponseData<List<AddressDetails>>> call = APIClient.getInstance()
                .getAddress
                        (
1
                        );

        call.enqueue(new Callback<ResponseData<List<AddressDetails>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<AddressDetails>>> call, retrofit2.Response<ResponseData<List<AddressDetails>>> response) {

                //  String str = new Gson().toJson(response.body());

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        lvlMyAddress.setVisibility(View.VISIBLE);
                        AdepterAddress adepterAddress = new AdepterAddress(AddressActivity.this, response.body().getData());
                        myRecyclerView.setAdapter(adepterAddress);
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
            public void onFailure(Call<ResponseData<List<AddressDetails>>> call, Throwable t) {
                //Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                // dialogLoader.hideProgressDialog();
            }
        });
    }


    public class AdepterAddress extends RecyclerView.Adapter<AdepterAddress.BannerHolder> {
        private Context context;
        private List<AddressDetails> addressLists;

        public AdepterAddress(Context context, List<AddressDetails> mBanner) {
            this.context = context;
            this.addressLists = mBanner;
        }

        @NonNull
        @Override
        public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_addresss_item, parent, false);
            return new BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {

            holder.txtType.setText("" + addressLists.get(position).getDelivery_time());
            holder.txtHomeaddress.setText(addressLists.get(position).getHouseno()+"," + addressLists.get(position).getLandmark() + "," + addressLists.get(position).getAddress());
            Glide.with(context).load( addressLists.get(position).getAddress()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).into(holder.imgBanner);
            holder.lvlHome.setOnClickListener(v -> {
                changeAddress = true;
                myAppPrefsManager.setAddress(addressLists.get(position));
                myAppPrefsManager.setAddressLoggedIn(true);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","result");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAddress = true;
                    myAppPrefsManager.setAddress(addressLists.get(position));
                    myAppPrefsManager.setAddressLoggedIn(true);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","result");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            });
            holder.imgMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, holder.imgMenu);
                popup.inflate(R.menu.address_menu);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_select:
                            changeAddress = true;
                            myAppPrefsManager.setAddress(addressLists.get(position));
                            myAppPrefsManager.setAddressLoggedIn(true);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result","result");
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                            break;
                        case R.id.menu_edit:
//                            startActivity(new Intent(AddressActivity.this, LocationGetActivity.class)
//                                    .putExtra(MapUtility.latitude, addressLists.get(position).getLatMap())
//                                    .putExtra(MapUtility.longitude, addressLists.get(position).getLongMap())
//                                    .putExtra("atype", addressLists.get(position).getType())
//                                    .putExtra("landmark", addressLists.get(position).getLandmark())
//                                    .putExtra("hno", addressLists.get(position).getHno())
//                                    .putExtra("newuser", "update")
//                                    .putExtra("userid", user.getId())
//                                    .putExtra("aid", "0"));

                            break;
                        default:
                            break;
                    }
                    return false;
                });
                popup.show();
            });

        }

        @Override
        public int getItemCount() {
            return addressLists.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_banner)
            ImageView imgBanner;
            @BindView(R.id.img_menu)
            ImageView imgMenu;
            @BindView(R.id.txt_homeaddress)
            TextView txtHomeaddress;
            @BindView(R.id.txt_tital)
            TextView txtType;
            @BindView(R.id.lvl_home)
            LinearLayout lvlHome;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}