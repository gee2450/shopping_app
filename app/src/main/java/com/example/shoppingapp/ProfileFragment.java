package com.example.shoppingapp;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {
    Intent intent = null;

    ProfileFragment(Intent intent) {
        this.intent = intent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = (TextView) rootView.findViewById(R.id.Name);
        TextView id = (TextView) rootView.findViewById(R.id.ID);
        TextView phoneNum = (TextView) rootView.findViewById(R.id.PhoneNum);
        TextView address = (TextView) rootView.findViewById(R.id.Address);

        if (intent != null) {
            name.setText(intent.getStringExtra("userName"));
            id.setText(intent.getStringExtra("id"));
            phoneNum.setText(intent.getStringExtra("phoneNum"));
            address.setText(intent.getStringExtra("address"));
        }

        return rootView;
    }
}