package com.example.mywardrobe.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mywardrobe.R;

public class SettingsFragment extends Fragment {

    public static String userLocation = "Los Angeles";
    EditText etLocation;
    Button btnSaveLocation;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etLocation = view.findViewById(R.id.etLocation);

        btnSaveLocation = view.findViewById(R.id.btnSaveLocation);
        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocation = etLocation.getText().toString();
                etLocation.setText(userLocation);
                Toast.makeText(getContext(), "New Location Saved Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}