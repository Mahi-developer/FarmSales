package com.example.farmsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EmailLogin extends AppCompatActivity {

    private EditText mail,password;
    private String Mail,Pass;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

//        hooks
        mail = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        TextView name = findViewById(R.id.tvLogin);
        Button login = findViewById(R.id.mail_login_button);
        Button forgotPass = findViewById(R.id.forgot_pass);
        Button create_acc = findViewById(R.id.new_acc);
        ImageView iv = findViewById(R.id.ivLogin);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v->{
            Mail = mail.getText().toString();
            Pass = password.getText().toString();
            if(Mail.endsWith(".com")){
                if(Pass.length() >= 8){
                    signInWithMailPassword(Mail,Pass);
                }else {
                    Toast.makeText(this,"Password should at-least contain 8 characters",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Provide a valid mail id",Toast.LENGTH_SHORT).show();
            }
        });

        forgotPass.setOnClickListener(v->{
            Mail = mail.getText().toString();
            if(Mail.endsWith(".com")){
                resetPassword(Mail);
            }else{
                Toast.makeText(this,"Provide a valid mail id",Toast.LENGTH_SHORT).show();
            }
        });

        create_acc.setOnClickListener(v -> {
            Intent intent = new Intent(this,SignUp.class);
            startActivity(intent);
            finishAndRemoveTask();
        });

    }

    private void resetPassword(String mail){

        mAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               if(task.getResult().getSignInMethods().isEmpty()){
                   Toast.makeText(this,"Your mail is not associated with us... Please create a new account !",Toast.LENGTH_SHORT).show();
               }else {
                   mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task1 -> {
                       if(task1.isSuccessful()){
                           Toast.makeText(this,"Password reset mail has been sent to your mail id",Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(this,"Cannot reset password now, try later",Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }
        });
    }

    private void signInWithMailPassword(String mail,String pass) {

        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(this,Home_page.class);
                Toast.makeText(this,"Signed in as"+mAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finishAndRemoveTask();
            }else {
                Toast.makeText(this,"Invalid e-mail and password",Toast.LENGTH_SHORT).show();
            }
        });

    }
}