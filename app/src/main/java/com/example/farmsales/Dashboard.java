package com.example.farmsales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Dashboard extends AppCompatActivity {


    private ArrayList<String> product_data = new ArrayList<String>();
    private ArrayList<String> product_name = new ArrayList<String>();
    private final ArrayList<String> product_quantity = new ArrayList<String>();
    private ArrayList<String> product_price = new ArrayList<String>();
    private String location;

    ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent in = getIntent();
        location = in.getStringExtra("loc");

        simpleList = findViewById(R.id.list_dash);
        ImageButton back = findViewById(R.id.back_dash);


        back.setOnClickListener(v->{
            onBackPressed();
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Products").child(location).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.exists()){
                        GenericTypeIndicator<Map<String,String>> to = new GenericTypeIndicator<Map<String,String>>() {};
                        Map<String,String> products = snapshot1.getValue(to);
                        for(String i : products.values()){
                            if(i.contains("₹")){
                                product_price.add(i);
                            }else if(i.contains("Kg")||i.contains("Piece")||i.contains("Bunch")){
                                product_quantity.add(i);
                            }else{
                                product_name.add(i);
                            }
                        }
                        setDataRates();
                    }else{
                        Toast.makeText(Dashboard.this,"No Products Found in your Location",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setDataRates(){
        DashboardAdapter customAdapter = new DashboardAdapter(getApplicationContext(),product_name, product_quantity, product_price);
        simpleList.setAdapter(customAdapter);

    }
}