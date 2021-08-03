package com.example.farmsales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetPrice extends AppCompatActivity {

    private String productName,loc;
    private String productPrice,productQuantity;
    private TextView name,quan;
    private EditText price;
    private ImageButton plus,minus;
    private Button post;
    private int maxPrice = 0,minPrice = 0;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_price);

        Intent in = getIntent();
        productName = in.getStringExtra("name");
        productPrice = in.getStringExtra("price");
        productQuantity = in.getStringExtra("quan");
        loc = in.getStringExtra("loc");

        maxPrice = (int) Math.round(Double.valueOf(productPrice.replace("₹","")))+10;
        minPrice = (int) Math.round(Double.valueOf(productPrice.replace("₹","")))-10;

        //hooks
        name = findViewById(R.id.productName);
        price = findViewById(R.id.productPrice);
        quan = findViewById(R.id.productQuantity);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        post = findViewById(R.id.post);
        imgView = findViewById(R.id.productImage);


        name.setText(productName);
        price.setText("₹"+productPrice);
        quan.setText(productQuantity);
        new CustomAdapter().getData(imgView,productName);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(price.getText().toString());
                int updatePrice = (int) Math.round(Double.valueOf(price.getText().toString().replace("₹","")));
                if(updatePrice <= maxPrice && updatePrice >= minPrice){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    Map<String, String> products = new HashMap<>();
                    products.put("productName",productName);
                    products.put("productPrice",price.getText().toString());
                    products.put("productQuantity",quan.getText().toString());
                    ref.child("Products").child(loc).child(productName).setValue(products).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }else {
                    Toast.makeText(SetPrice.this,"your price exceeding maximum price!",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}