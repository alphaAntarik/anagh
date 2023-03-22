package com.etechnie.anagh.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.adapters.CategoryListAdapter_1;
import com.etechnie.anagh.app.App;
import com.etechnie.anagh.app.MyAppPrefsManager;
import com.etechnie.anagh.imagepicker.ImageCompressionListener;
import com.etechnie.anagh.imagepicker.ImagePicker;
import com.etechnie.anagh.models.RestResponse;
import com.etechnie.anagh.models.category_model.CategoryDetails;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.CustPrograssbar;
import com.etechnie.anagh.utils.FileUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.etechnie.anagh.utils.Utilities.isLocal;

public class PrescriptionFragment extends Fragment implements View.OnClickListener {




    TextView txtPrescriptionValid;

    TextView btnUpload;

    LinearLayout lvlEmpty;

    LinearLayout lvlPic;

    RecyclerView recyclerView;

    TextView txtAtype;

    TextView txtAddress;

    TextView txtChangeadress;

    TextView btnAther;

    TextView btnSubmit;
    private ImagePicker imagePicker;
    ArrayList<String> arrayListImage = new ArrayList<>();
    CustPrograssbar custPrograssbar;
    Context mcontext;
    MyAppPrefsManager myAppPrefsManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_upload_prescription, container, false);
        mcontext=getActivity();
       // ButterKnife.bind(getActivity());
        txtPrescriptionValid= (TextView) rootView.findViewById(R.id.txt_prescription_valid);
        custPrograssbar = new CustPrograssbar();
        btnUpload= (TextView) rootView.findViewById(R.id.btn_upload);
        lvlEmpty= (LinearLayout) rootView.findViewById(R.id.lvl_empty);
        lvlPic= (LinearLayout) rootView.findViewById(R.id.lvl_pic);
         myAppPrefsManager = new MyAppPrefsManager(mcontext);
        txtAtype= (TextView) rootView.findViewById(R.id.txt_atype);
        txtAddress= (TextView) rootView.findViewById(R.id.txt_address);
        txtChangeadress= (TextView) rootView.findViewById(R.id.txt_changeadress);
        btnAther= (TextView) rootView.findViewById(R.id.btn_ather);
        btnSubmit= (TextView) rootView.findViewById(R.id.btn_submit);

        btnUpload.setOnClickListener(this);
        btnAther.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        recyclerView= (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mcontext, 2));
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(mcontext);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imagePicker = new ImagePicker();
        if (checkPermission()) {
            //start image picker

        } else {
            //ask permission
            requestStoragePermission();
        }
        // Enable Drawer Indicator with static variable actionBarDrawerToggle of MainActivity
       //MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        ((MainActivity)getActivity()).toggleNavigaiton(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("CategoryName", getString(R.string.actionCategory)));




        return rootView;
    }




    public void bottonChoseoption() {
       final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mcontext);

        View sheetView = getLayoutInflater().inflate(R.layout.activity_image_select, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView textViewCamera = sheetView.findViewById(R.id.textViewCamera);
        TextView textViewGallery = sheetView.findViewById(R.id.textViewGallery);
        TextView textViewCancel = sheetView.findViewById(R.id.textViewCancel);


        mBottomSheetDialog.show();
        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                imagePicker.withFragment(PrescriptionFragment.this).chooseFromGallery(false).chooseFromCamera(true).withCompression(true).start();

            }
        });
        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                imagePicker.withFragment(PrescriptionFragment.this).chooseFromGallery(true).chooseFromCamera(false).withCompression(true).start();

            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel()  ;
            }
        });

    }

    public void bottonVelidation() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(mcontext);

        View sheetView = getLayoutInflater().inflate(R.layout.custome_vallid_layout, null);
        mBottomSheetDialog.setContentView(sheetView);


        mBottomSheetDialog.show();


    }

    private void uploadMultiFile(ArrayList<String> filePaths) {
        custPrograssbar.prograssCreate(mcontext);
        List<MultipartBody.Part> files = new ArrayList<>();

        if (filePaths != null) {
            // create part for file (photo, video, ...)
            for (int i = 0; i < filePaths.size(); i++) {
                files.add(prepareFilePart("image" + i, filePaths.get(i)));
            }
        }
       RequestBody uid = createPartFromString(myAppPrefsManager.getUserId());
       // RequestBody addres = createPartFromString(selectaddress.getHno()+","+selectaddress.getLandmark()+ ","+selectaddress.getAddress());
        RequestBody addres = createPartFromString("address");
       RequestBody dCharge = createPartFromString("100");
        RequestBody size = createPartFromString("" + files.size());

// finally, execute the request
        Call<JsonObject> call = APIClient.getInstance().uploadMultiFile(files,uid,addres,dCharge,size);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                custPrograssbar.closePrograssBar();
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(response.body(), RestResponse.class);
                Toast.makeText(mcontext, restResponse.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    lvlEmpty.setVisibility(VISIBLE);
                    lvlPic.setVisibility(GONE);
                    arrayListImage.clear();

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                custPrograssbar.closePrograssBar();

            }
        });
    }


    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = getFile(mcontext, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static File getFile(Context context, String path) {
        if (path == null) {
            return null;
        }

        if (isLocal(path)) {
            return new File(path);
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(mcontext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1234);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_ather:
                bottonChoseoption();
                break;
            case R.id.btn_submit:
                if (arrayListImage.size() != 0) {
                    uploadMultiFile(arrayListImage);
                }
                break;
            case R.id.txt_changeadress:
                //startActivity(new Intent(UploadPrescriptionActivity.this, AddressActivity.class));

                break;
            case R.id.txt_prescription_valid:
                bottonVelidation();

                break;
            case R.id.btn_upload:
                bottonChoseoption();
                break;
            default:
                break;
        }
    }



     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == RESULT_OK) {

            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onCompressed(String filePath) {
                    if (filePath != null) {
                        if(arrayListImage.size()<4) {
                            //return filepath
                            arrayListImage.add(filePath);
                            postImage(arrayListImage);
                        }
                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {
                if(arrayListImage.size()<4) {
                    //return filepath
                    arrayListImage.add(filePath);
                    postImage(arrayListImage);
                }
            }
        }
    }

    public void postImage(ArrayList<String> urilist) {
        if (urilist.size() != 0) {
            lvlEmpty.setVisibility(GONE);
        }
        ImageAdp imageAdp = new ImageAdp(mcontext, urilist);
        recyclerView.setAdapter(imageAdp);

    }

    public class ImageAdp extends RecyclerView.Adapter<ImageAdp.MyViewHolder> {
        private ArrayList<String> arrayList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView remove;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);

                thumbnail = view.findViewById(R.id.image_pic);
                remove = view.findViewById(R.id.image_remove);
            }
        }

        public ImageAdp(Context mContext, ArrayList<String> arrayList) {
            this.arrayList = arrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {

            Glide.with(mcontext)
                    .load(arrayList.get(holder.getAdapterPosition()))
                    .into(holder.thumbnail);
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.remove(holder.getAdapterPosition());
                    if (arrayList.size() != 0) {
                        notifyDataSetChanged();
                    } else {
                        lvlEmpty.setVisibility(VISIBLE);
                        lvlPic.setVisibility(GONE);
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}

