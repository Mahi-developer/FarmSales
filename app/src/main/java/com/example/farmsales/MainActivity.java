package com.example.farmsales;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText edtPhone;

    private Button sendOTP;

    private CountryCodePicker country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtPhone = findViewById(R.id.phone_num_view);
        sendOTP = findViewById(R.id.otp_btn);
        country_code = (CountryCodePicker) findViewById(R.id.countryCodePicker);



        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtPhone.getText().toString())){

                    Toast.makeText(MainActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();

                }

                else{
                    String Phone_number = "+" + country_code.getSelectedCountryCode().toString() + edtPhone.getText().toString();
                    Intent i = new Intent(getApplicationContext(), Verify_otp.class);
                    i.putExtra("Phone_number", Phone_number);
                    startActivity(i);

                }

            }
        });

    }


}
