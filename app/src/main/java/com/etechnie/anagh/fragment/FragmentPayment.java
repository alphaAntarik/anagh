package com.etechnie.anagh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etechnie.anagh.R;
import com.etechnie.anagh.activities.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentPayment extends Fragment {



    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_payment_option, container, false);
        mContext=getContext();
        ButterKnife.bind(this, rootView);
        return rootView;
    }




}

