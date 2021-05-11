package com.example.farmsales;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

public class Verify_otp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    public String phone = "";
    private Button resend_bt;
    private TextView timer;
    public in.aabhasjindal.otptextview.OtpTextView otp_et;
    public PhoneAuthProvider.ForceResendingToken token = null;
    private int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        Intent in = getIntent();
        phone = in.getStringExtra("phone");
        sendVerificationCode(phone);
        otp_et = findViewById(R.id.enter_otp);
        TextView tv_number = findViewById(R.id.tv_no);
        timer = findViewById(R.id.timer);
        tv_number.setText(phone);
        resend_bt = findViewById(R.id.resend_otp);
        resend_bt.setEnabled(false);
        ImageButton back = findViewById(R.id.back);

        otp_et.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                verifyCode(otp);
            }
        });

        resend_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phone, token);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        phone = number;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
        System.out.println(number);
    }

    public void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            token = forceResendingToken;

            new CountDownTimer(60000,1000){

                @Override
                public void onTick(long l) {
                    int time = 60 - counter;
                    String str_time = "";
                    if(time > 9){
                        str_time = "0:"+time;
                    }else{
                        str_time = "0:0"+time;
                    }
                    counter++;
                    timer.setText(str_time);
                }

                @Override
                public void onFinish() {
                    resend_bt.setEnabled(true);
                }
            }.start();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            Toast.makeText(Verify_otp.this,s,Toast.LENGTH_SHORT).show();
            super.onCodeAutoRetrievalTimeOut(s);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verifyCode(code);
                otp_et.setOTP(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify_otp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };



    public void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }



    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    if(task1.getResult().exists()){
                                        Intent intent = new Intent(Verify_otp.this,Home_page.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Intent intent = new Intent(Verify_otp.this,SignUp.class);
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }

                        else {
                            Toast.makeText(Verify_otp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }


}

