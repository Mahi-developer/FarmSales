package com.example.farmsales;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import dmax.dialog.SpotsDialog;
import in.aabhasjindal.otptextview.OTPListener;

public class MainActivity extends AppCompatActivity {

    private EditText edtPhone;
    private CountryCodePicker country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPhone = findViewById(R.id.phone_num_view);
        Button sendOTP = findViewById(R.id.otp_btn);
        country_code = findViewById(R.id.countryCodePicker);
        Button google_bt = findViewById(R.id.google_btn);

        sendOTP.setOnClickListener(v -> {

            if (edtPhone.getText().toString().length() != 10) {
                Toast.makeText(MainActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {
                String Phone_number = "+" + country_code.getSelectedCountryCode() + edtPhone.getText().toString();
                Intent in = new Intent(MainActivity.this,Verify_otp.class);
                in.putExtra("phone",Phone_number);
                startActivity(in);
            }
        });

        google_bt.setOnClickListener(v -> {

        });

    }
}
