package com.example.farmsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    private ListView simpleList;

    private TextView name,quan;
    private EditText price;
    private ImageButton plus,minus;
    private ImageView imgView;
    private String location;
    ArrayList<String> product_name = new ArrayList<String>();
    ArrayList<String> product_quantity = new ArrayList<String>();
    ArrayList<String> product_price= new ArrayList<String>();
    private int sum = 0;

    String pName;

    TextView tot;
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent in = getIntent();
        product_name = in.getStringArrayListExtra("productName");
        product_price = in.getStringArrayListExtra("productPrice");
        product_quantity = in.getStringArrayListExtra("productQuantity");
        location = in.getStringExtra("location");


        //hooks
        simpleList = findViewById(R.id.list_cart);
        tot = findViewById(R.id.total_price);
        placeOrder = findViewById(R.id.place_order);
        ImageButton back = findViewById(R.id.back_button);
        setDataRates();


        sum = 0;
        for(String i : product_price){
            sum+= Math.round(Double.valueOf(i.replace("₹","")));
        }
//        tot.setText("₹"+sum);

        back.setOnClickListener(v->{
            onBackPressed();
        });

    }



    public void setDataRates(){
        CartAdapter cartAdapter = new CartAdapter(getApplicationContext(),product_name, product_quantity, product_price, sum, tot , placeOrder);
        cartAdapter.setmOnCartListener(new CartAdapter.OnCartListener() {
            @Override
            public void onMinusClick(int position) {
                deleteDb(product_name.get(position));
                product_name.remove(position);
                product_price.remove(position);
                product_quantity.remove(position);
                sum=0;
                cartAdapter.notifyDataSetChanged();

//                pName = product_name.get(position);
//                deleteData(pName);
            }



//            private void deleteData(String pName) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                FirebaseUser user = auth.getCurrentUser();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference databaseReference  = database.getReference().child("Customer").child(user.getUid()).child("Cart").child(pName);
//                databaseReference.removeValue();
//            }

        });

        cartAdapter.setonPlaceClick(new CartAdapter.PlaceOrder() {
            @Override
            public void onPlaceClick() {
//                product_name.clear();
//                product_price.clear();
//                product_quantity.clear();
//                cartAdapter.notifyDataSetChanged();

                finish();
            }
        });

        simpleList.setAdapter(cartAdapter);
    }

    private void deleteDb(String name) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference  = database.getReference().child("Customer").child(user.getUid()).child("Cart").child(name);
        databaseReference.removeValue();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}