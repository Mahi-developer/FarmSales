package com.example.farmsales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{

    Context context;
    ArrayList<String> product_name;
    ArrayList<String> product_quantity;
    ArrayList<String> product_price;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<String> product_name, ArrayList<String> product_quantity, ArrayList<String> product_price) {
        this.context = context;
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
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView name = (TextView) view.findViewById(R.id.product_name);
        TextView quantity = (TextView) view.findViewById(R.id.product_quantity);
        TextView price = (TextView) view.findViewById(R.id.product_price);
        ImageView image_view = (ImageView)view.findViewById(R.id.product_image);
        getData(image_view,product_name.get(i));
        name.setText(product_name.get(i));
        quantity.setText(product_quantity.get(i));
        price.setText("₹"+product_price.get(i));
        return view;
    }

    public void getData(ImageView image_view,String product){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference getImage = databaseReference.child(product);

        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(image_view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
