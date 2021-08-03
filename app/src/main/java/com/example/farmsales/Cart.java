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

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    private ListView simpleList;

    private String productName,loc;
    private String productPrice,productQuantity;
    private TextView name,quan;
    private EditText price;
    private ImageButton plus,minus;
    private ImageView imgView;
    ArrayList<String> product_name = new ArrayList<String>();
    ArrayList<String> product_quantity = new ArrayList<String>();
    ArrayList<String> product_price= new ArrayList<String>();
    private int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent in = getIntent();
        productName = in.getStringExtra("name");
        productPrice = in.getStringExtra("price");
        productQuantity = in.getStringExtra("quan");
        loc = in.getStringExtra("loc");
        product_name.add(productName);
        product_price.add(productPrice);
        product_quantity.add(productQuantity);

        //hooks
        simpleList = findViewById(R.id.list_cart);
        TextView tot = findViewById(R.id.total_price);
        Button placeOrder = findViewById(R.id.place_order);
        ImageButton back = findViewById(R.id.back_button);
        setDataRates();

        sum = 0;
        for(String i : product_price){
            sum+= Math.round(Double.valueOf(i.replace("₹","")));
        }
        tot.setText("₹"+sum);

        placeOrder.setOnClickListener(v->{
            Intent intent = new Intent(this,Payment.class);
            intent.putExtra("total","₹"+sum);
            startActivity(intent);
        });
        back.setOnClickListener(v->{
            onBackPressed();
        });
    }

    public void setDataRates(){
        CartAdapter cartAdapter = new CartAdapter(getApplicationContext(),product_name, product_quantity, product_price);
        simpleList.setAdapter(cartAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}