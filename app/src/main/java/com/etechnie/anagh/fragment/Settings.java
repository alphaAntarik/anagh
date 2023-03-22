package com.etechnie.anagh.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.Login;
import com.etechnie.anagh.activities.LoginActivity;
import com.etechnie.anagh.activities.MainActivity;
import com.etechnie.anagh.adapters.CategoryListAdapter_4;
import com.etechnie.anagh.app.App;
import com.etechnie.anagh.app.MyAppPrefsManager;
import com.etechnie.anagh.constant.ConstantValues;
import com.etechnie.anagh.models.category_model.CategoryDetails;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Settings extends Fragment {



    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_setting, container, false);
        mContext=getContext();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.lvl_order, R.id.lvl_myprescription, R.id.lvl_address, R.id.lvl_about, R.id.lvl_contect, R.id.lvl_privacy, R.id.lvl_teams, R.id.lvl_logot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_order:

                break;
            case R.id.lvl_myprescription:
               Fragment fragment = new PrescriptionOrder();
                Bundle categoryInfo = new Bundle();
                fragment.setArguments(categoryInfo);
                FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.main_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
                break;
            case R.id.lvl_address:

                break;
            case R.id.lvl_about:

                break;
            case R.id.lvl_contect:

                break;
            case R.id.lvl_privacy:

                break;
            case R.id.lvl_teams:

                break;
            case R.id.lvl_logot:

                    // Navigate to Login Activity
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    ((MainActivity) getContext()).finish();
                    ((MainActivity) getContext()).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

                break;

            default:
                break;
        }
    }


}

