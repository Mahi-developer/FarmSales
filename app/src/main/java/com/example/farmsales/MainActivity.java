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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private EditText edtPhone;

    public static EditText otp_et;
    public AlertDialog alertDialog;
    private CountryCodePicker country_code;
    private android.app.AlertDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtPhone = findViewById(R.id.phone_num_view);
        Button sendOTP = findViewById(R.id.otp_btn);
        country_code = findViewById(R.id.countryCodePicker);



        sendOTP.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtPhone.getText().toString())){
                Toast.makeText(MainActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            }

            else{
                String Phone_number = "+" + country_code.getSelectedCountryCode() + edtPhone.getText().toString();
                buildDialog(Phone_number);
            }
        });
    }

    private void buildDialog(String phone){
        progressBar = new SpotsDialog.Builder().setContext(MainActivity.this)
                .setCancelable(false)
                .setMessage("Auto-Retrieving OTP")
                .setTheme(R.style.Custom)
                .build();
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_verify_otp,null);
        otp_et = mView.findViewById(R.id.enter_otp);
        Button verify_bt = mView.findViewById(R.id.verify_otp);
        Button resend_bt = mView.findViewById(R.id.resend_otp);
        builder.setView(mView);
        alertDialog = builder.create();
        Context context = this;
        Verify_otp otp_verification = new Verify_otp(otp_et,context,progressBar);
        otp_verification.sendVerificationCode(phone);
        alertDialog.setCanceledOnTouchOutside(false);

        verify_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otp_et.getText().toString();
                otp_verification.verifyCode(otp);
            }
        });

        resend_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp_verification.resendVerificationCode(phone, otp_verification.token);
            }
        });
        alertDialog.show();
        progressBar.show();
    }

    @Override
    protected void onDestroy() {
        progressBar.dismiss();
        alertDialog.dismiss();
        super.onDestroy();
    }
}
