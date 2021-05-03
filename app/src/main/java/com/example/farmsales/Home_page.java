package com.example.farmsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Home_page extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TextView tv = findViewById(R.id.textView2);
        ImageView iv = findViewById(R.id.imageView);
        auth= FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        assert user != null;
        String photoUrl = user.getPhotoUrl().toString();
        Picasso.get().load(photoUrl).into(iv);

        tv.setText(user.getDisplayName());
        tv.setOnClickListener(v->{
            sign_out();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void sign_out(){
        auth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this,"Signed Out Successfully",Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            }
        });
    }
}