package com.example.farmsales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import java.util.ArrayList;

public class Home_page extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<String> product_data = new ArrayList<String>();
    private ArrayList<String> product_name = new ArrayList<String>();
    private final ArrayList<String> product_quantity = new ArrayList<String>();
    private ArrayList<String> product_price = new ArrayList<String>();
    private ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_home_page);


        new GetRates().execute();
        auth= FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        if(user.getPhotoUrl()!=null){
            String photoUrl = user.getPhotoUrl().toString();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void setDataRates(){
        simpleList = (ListView) findViewById(R.id.list);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), product_name, product_quantity, product_price);
        simpleList.setAdapter(customAdapter);
    }

    public void sign_out(){
        auth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this,"Signed Out Successfully",Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            }
        });
    }

    private class GetRates extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("https://rates.goldenchennai.com/vegetable-price/vellore-vegetable-price-today/").get();
                org.jsoup.select.Elements rows = doc.select("tr");
                for (org.jsoup.nodes.Element row : rows) {
                    org.jsoup.select.Elements columns = row.select("td");
                    for (org.jsoup.nodes.Element column : columns) {
                        product_data.add(column.text());
                    }
                }
                for(int i=0;i<product_data.size();i=i+3){
                    product_name.add(product_data.get(i));
                }
                for(int i=1;i<product_data.size();i=i+3){
                    product_quantity.add(product_data.get(i));
                }
                for(int i=2;i<product_data.size();i=i+3){
                    product_price.add(product_data.get(i));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setDataRates();
        }


//        image_view = findViewById(R.id.image_view);
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference();
//        DatabaseReference getImage = databaseReference.child("Bangalore Tomato (Bangalore Thakkali)");
//
//        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String link = dataSnapshot.getValue(String.class);
//                Picasso.get().load(link).into(image_view);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(Home_page.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
//            }
//        });


     }

}