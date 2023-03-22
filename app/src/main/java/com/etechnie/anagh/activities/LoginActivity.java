package com.etechnie.anagh.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etechnie.anagh.R;
import com.etechnie.anagh.models.ResponseData;
import com.etechnie.anagh.models.country_model.CountryCode;
import com.etechnie.anagh.models.login_model.PostLogin;
import com.etechnie.anagh.models.login_model.ResponseLogin;
import com.etechnie.anagh.network.APIClient;
import com.etechnie.anagh.utils.CustPrograssbar;
import com.etechnie.anagh.utils.JWTUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.txt_olduser)
    TextView txtSingUp;
    @BindView(R.id.btn_proceed)
    TextView btnProceed;
    @BindView(R.id.lvl_singup)
    LinearLayout lvlSingup;
    @BindView(R.id.txt_newuser)
    TextView txtLogin;
    @BindView(R.id.lvl_login)
    LinearLayout lvlLogin;
    List<CountryCode> cCodes = new ArrayList<>();
    String codeSelect="";
    boolean isNewuser = true;

    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        custPrograssbar=new CustPrograssbar();

        String sourceString = "Have a <b>" + "Email/Password" + "</b> " + "Account?";
        String sourceCreate = "<b>Sign Up?</b>";
        txtSingUp.setText(Html.fromHtml(sourceString));
        txtLogin.setText(Html.fromHtml(sourceCreate));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //codeSelect = cCodes.get(position).getCcode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getContryCode();
    }


    private void getContryCode() {
        List<String> Arealist = new ArrayList<>();

                Arealist.add("+91");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, Arealist);
        dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
        spinner.setAdapter(dataAdapter);

    }


    @OnClick({R.id.txt_olduser, R.id.btn_proceed, R.id.txt_newuser, R.id.txt_forgotclick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_olduser:
                txtSingUp.setVisibility(View.GONE);
                lvlSingup.setVisibility(View.GONE);
                lvlLogin.setVisibility(View.VISIBLE);
                isNewuser = false;
                break;
            case R.id.btn_proceed:
                if (isNewuser) {
                    if (!TextUtils.isEmpty(edMobile.getText().toString())) {
                        loginUser();

                    } else {
                        edMobile.setError("Enter Mobile Number");
                    }
                }
                else {
                    if (TextUtils.isEmpty(edEmail.getText().toString())) {
                        edEmail.setError("Enter Mobile / Email");
                    } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                        edPassword.setError("Enter Password");
                    } else {
                        //loginUser();
                        startActivity(new Intent(LoginActivity.this, VerifyPhoneActivity.class).putExtra("code", codeSelect).putExtra("phone", edMobile.getText().toString()));

                    }
                }
                break;
            case R.id.txt_newuser:
                isNewuser = true;
                txtSingUp.setVisibility(View.VISIBLE);
                lvlSingup.setVisibility(View.VISIBLE);
                lvlLogin.setVisibility(View.GONE);
                break;
            case R.id.txt_forgotclick:
                //startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            default:
                break;
        }
    }


    private void loginUser() {

        final String uname = this.edMobile.getText().toString();

        PostLogin p=new PostLogin();
        //p.setMobileno(uname);
        p.setUsername(uname);
        p.setPassword(uname);
        RequestAllProducts(p);
    }

    public void RequestAllProducts(PostLogin p) {

        custPrograssbar.prograssCreate(LoginActivity.this);
        Call<ResponseData<ResponseLogin>> call = APIClient.getInstance()
                .login
                        (
                                p
                        );

        call.enqueue(new Callback<ResponseData<ResponseLogin>>() {
            @Override
            public void onResponse(Call<ResponseData<ResponseLogin>> call, retrofit2.Response<ResponseData<ResponseLogin>> response) {

                custPrograssbar.closePrograssBar();

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (String.valueOf(response.body().getCode()).equalsIgnoreCase("1")) {

                        ResponseLogin data =   response.body().getData();
                        try {
                            String body= JWTUtils.decoded(data.getAccessToken());

                            Intent i = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                            i.putExtra("data",body);
                            i.putExtra("type","login");
                            i.putExtra("token",data.getAccessToken());
                            i.putExtra("phone", edMobile.getText().toString());
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        // Unable to get Success status
                        Toast.makeText(LoginActivity.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Show the Error Message
                    String Str = response.message();
                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseData<ResponseLogin>> call, Throwable t) {
                custPrograssbar.closePrograssBar();
                String Str = "" + t;
                Toast.makeText(LoginActivity.this, "NetworkCallFailure : " + t, Toast.LENGTH_LONG).show();

            }
        });

    }
}