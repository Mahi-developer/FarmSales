package com.example.farmsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText name,phone,email,password,confirmPassword;
    Button login;
    RadioGroup userType;
    Button submit;
    FirebaseUser user;
    FirebaseAuth mAuth;
    boolean isFromPhone;
    String UserType,Name,Phone,Email,Password,ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent in = getIntent();
        Phone = in.getStringExtra("phone");
        Email = in.getStringExtra("mail");
        Name = in.getStringExtra("name");

        //hooks
        name = findViewById(R.id.name);
        phone = findViewById(R.id.mobile);
        email = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        userType = findViewById(R.id.type);
        login = findViewById(R.id.login_button);
        submit = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(Phone != null){
            phone.setText(Phone);
            isFromPhone = true;
        }
        if(Email != null){
            email.setText(Email);
            name.setText(Name);
        }

        login.setOnClickListener(v->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finishAndRemoveTask();
        });

        submit.setOnClickListener(v->{
            if(email.getText() != null || phone.getText() != null){
                checkValid();
                verifyDetails();
            }
        });
    }

    private void checkValid(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(isFromPhone){
            db.collection("users").whereEqualTo("Phone",phone.getText().toString())
                    .get()
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           Toast.makeText(this,"Mobile number already in use, provide different number or try log in !",Toast.LENGTH_SHORT).show();
                           phone.setText("");
                       }
                    });
        }else{
            mAuth = FirebaseAuth.getInstance();
            mAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(this,"Your E-mail already in use, provide different mail or try log in !",Toast.LENGTH_SHORT).show();
                    email.setText("");
                }
            });
        }
    }

    private void verifyDetails() {

        Name = name.getText().toString();
        Phone = phone.getText().toString();
        Email = email.getText().toString();
        Password = password.getText().toString();
        ConfirmPassword = confirmPassword.getText().toString();
        RadioButton type = findViewById(userType.getCheckedRadioButtonId());
        UserType = type.getText().toString();

        if(Name.length() > 3){
            if(Phone.length() > 10){
                if(!Email.isEmpty()){
                    if(!UserType.isEmpty()){
                        if(Password.length() >= 8){
                            if(ConfirmPassword.equals(Password)){
                                if(!isFromPhone) {
                                    createUserData();
                                }else{
                                    updateDB();
                                }
                            }else {
                                Toast.makeText(this,"Password and confirm password are not the same !",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this,"Password should contain at-least 8 characters !",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this,"Select at-least any one on \"Who are You ?\" !",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this,"Please enter a valid Email !",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Please enter a valid mobile number !",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Please enter your full name !",Toast.LENGTH_SHORT).show();
        }

    }

    private void createUserData() {
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(task2 -> {
                    if(task2.isSuccessful()){
                        updateDB();
                    }
                });
            }
        });
    }

    private void updateDB(){
        user = mAuth.getCurrentUser();
        String uid = user.getUid();
        Map <String, Object> userDetails = new HashMap<>();
        userDetails.put("Uid",uid);
        userDetails.put("Name",Name);
        userDetails.put("Phone",Phone);
        userDetails.put("Email",Email);
        userDetails.put("Type",UserType);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users/"+UserType)
                .document(uid)
                .set(userDetails)
                .addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Name)
                                .build();
                        user.updateProfile(request).addOnCompleteListener(task3 -> {
                            if(task3.isSuccessful()){
                                Intent intent = new Intent(this,Home_page.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(this,"Some error occurred, retry login!",Toast.LENGTH_SHORT).show();
                                finishAndRemoveTask();
                            }
                        });
                    }else {
                        Toast.makeText(this,"Unable to store your data retry login!",Toast.LENGTH_SHORT).show();
                        finishAndRemoveTask();
                    }
                });
    }
}