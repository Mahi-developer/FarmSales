package com.example.farmsales;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

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



        sendOTP.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtPhone.getText().toString())){
                Toast.makeText(MainActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            }

            else{
                String Phone_number = "+" + country_code.getSelectedCountryCode() + edtPhone.getText().toString();
                Verify_otp otp_verification = new Verify_otp();
                otp_verification.sendVerificationCode(Phone_number);
                buildDialog(otp_verification.otp);
            }
        });
    }

    private void buildDialog(String otp){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_verify_otp,null);
        final EditText otp_et = mView.findViewById(R.id.enter_otp);
        Button verify_bt = mView.findViewById(R.id.verify_otp);
        Button resend_bt = mView.findViewById(R.id.resend_otp);
        builder.setView(mView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        if(!otp.isEmpty()){
            otp_et.setText(otp);
        }
        verify_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otp_et.getText().toString();
            }
        });
        resend_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        alertDialog.show();
    }
}
