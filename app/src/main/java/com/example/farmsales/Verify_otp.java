package com.example.farmsales;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

public class Verify_otp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private String verificationId;
    public String phone = "";
    public EditText otp_et;
    private android.app.AlertDialog progressDialog;
    public Context context;
    public PhoneAuthProvider.ForceResendingToken token = null;

    public Verify_otp(EditText otp_et, Context context, android.app.AlertDialog progressDialog){
        this.otp_et = otp_et;
        this.context = context;
        this.progressDialog = progressDialog;
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
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            progressDialog.dismiss();
            Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            super.onCodeAutoRetrievalTimeOut(s);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                verifyCode(code);
                otp_et.setText(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressDialog.dismiss();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(context,Home_page.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }

                        else {
                            progressDialog.dismiss();
                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }


}

