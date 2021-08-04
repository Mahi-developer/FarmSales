package com.example.farmsales;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.icu.util.ULocale;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter extends BaseAdapter{

    Context context;
    ArrayList<String> product_name;
    ArrayList<String> product_quantity;
    ArrayList<String> product_price;
    LayoutInflater inflter;
    String type,location;


    public CustomAdapter(){

    }

    public CustomAdapter(Context applicationContext,String type,String location,ArrayList<String> product_name, ArrayList<String> product_quantity, ArrayList<String> product_price) {
        this.context = applicationContext;
        this.type = type;
        this.location = location;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return product_name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(type.equals("Farmer")){
            view = inflter.inflate(R.layout.activity_listview_farmer, null);
        }else{
            view = inflter.inflate(R.layout.activity_listview, null);
        }

        ImageView image_view = (ImageView)view.findViewById(R.id.product_image);
        getData(image_view,product_name.get(i));
        TextView name = (TextView) view.findViewById(R.id.product_name);
        TextView quantity = (TextView) view.findViewById(R.id.product_quantity);
        TextView price = (TextView) view.findViewById(R.id.product_price);
        ImageButton img = (ImageButton)view.findViewById(R.id.buttoncl);
        name.setText(product_name.get(i));
        quantity.setText(product_quantity.get(i));
        price.setText("₹"+product_price.get(i));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("Farmer")){
                    Intent intent = new Intent(context,SetPrice.class);
                    intent.putExtra("name",product_name.get(i));
                    intent.putExtra("price",product_price.get(i));
                    intent.putExtra("quan",product_quantity.get(i));
                    intent.putExtra("loc",location);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    img.setImageResource(R.drawable.ic_baseline_check_24);
                    setData(product_name.get(i),product_price.get(i),product_quantity.get(i),location);

//                    Intent intent = new Intent(context,Cart.class);
//                    intent.putExtra("name",product_name.get(i));
//                    intent.putExtra("price",product_price.get(i));
//                    intent.putExtra("quan",product_quantity.get(i));
//                    intent.putExtra("loc",location);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }

            }
        });
        return view;
    }

    public void setData(String productName,String productPrice,String productQuantity,String location){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Map<String, String> products = new HashMap<>();
        products.put("productName",productName);
        products.put("productPrice",productPrice);
        products.put("productQuantity",productQuantity);

        databaseReference.child("Customer").child(user.getUid()).child("Cart").child(productName)
                .setValue(products)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context , productName+" "+"Added to cart",Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void getData(ImageView image_view,String product){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference getImage = databaseReference.child("Images");

        getImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals(product)){
                        String link = dataSnapshot1.getValue(String.class);
                        Picasso.get().load(link).into(image_view);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
