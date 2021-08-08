package com.example.farmsales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.logging.Logger;

public class CartAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> product_name;
    ArrayList<String> product_quantity;
    ArrayList<String> product_price;
    LayoutInflater inflter;
    int sum=0;
    TextView tot;
    Button placeOrder;

    private OnCartListener mOnCartListener;
    private PlaceOrder place;

    CartAdapter(){

    }

    CartAdapter(Context applicationContext,ArrayList<String> product_name, ArrayList<String> product_quantity, ArrayList<String> product_price, int sum, TextView tot, Button placeOrder) {
        this.context = applicationContext;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.sum=sum;
        this.tot = tot;
        this.placeOrder=placeOrder;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void setmOnCartListener(OnCartListener mOnCartListener) {
        this.mOnCartListener = mOnCartListener;
    }

    public void setonPlaceClick( PlaceOrder order){
        this.place = order;
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
        view = inflter.inflate(R.layout.cart, null);

        sum+= Math.round(Double.valueOf(product_price.get(i).replace("₹","")));

        ImageView image_view = (ImageView)view.findViewById(R.id.product_image_cart);
        new CustomAdapter().getData(image_view,product_name.get(i));
        TextView name = (TextView) view.findViewById(R.id.product_name_cart);
        TextView quantity = (TextView) view.findViewById(R.id.product_quantity_cart);
        TextView price = (TextView) view.findViewById(R.id.product_price_cart);
        name.setText(product_name.get(i));
        quantity.setText(product_quantity.get(i));
        price.setText(product_price.get(i));

        System.out.println("sum ---"+ sum);

        ImageButton plus = view.findViewById(R.id.buttonclp_cart);
        ImageButton minus = view.findViewById(R.id.buttoncl_cart);

        plus.setOnClickListener(v->{
            int qu = 1;
            String p = price.getText().toString();
            int pr = (int) Math.round(Double.valueOf(p.replace("₹", "")));
            String q = quantity.getText().toString();

            if(q.contains("Kg")) {
                qu = (int) Math.round(Double.valueOf(q.replace(" Kg", "")));
                if(qu<10) {
                    int temp = pr/qu;
                    qu++;
                    price.setText("₹"+temp*qu);
                    sum+=temp;
                    quantity.setText(qu + " Kg");
                }
            }
            else if(q.contains("Piece")){
                qu = (int) Math.round(Double.valueOf(q.replace(" Piece", "")));
                if(qu<10) {
                    int temp = pr/qu;
                    qu++;
                    price.setText("₹"+temp*qu);
                    sum+=temp;
                    quantity.setText(qu + " Piece");
                }
            }
            else{
                qu = (int) Math.round(Double.valueOf(q.replace(" Bunch", "")));
                if(qu<10) {
                    int temp = pr/qu;
                    qu++;
                    price.setText("₹"+temp*qu);
                    sum+=temp;
                    quantity.setText(qu + " Bunch");
                }
            }
            if(qu>1){
                minus.setImageResource(R.drawable.ic_baseline_remove_24);
            }

            tot.setText("₹"+sum);
        });

        minus.setOnClickListener(v->{
            int qu = 1;
            String p = price.getText().toString();
            String q = quantity.getText().toString();
            int pr = (int) Math.round(Double.valueOf(p.replace("₹", "")));

            if(q.contains("Kg")) {
                qu = (int) Math.round(Double.valueOf(q.replace(" Kg", "")));
                if(qu>1){
                    int temp = pr/qu;
                    qu--;
                    price.setText("₹"+temp*qu);
                    sum-=temp;
                    quantity.setText(qu+" Kg");
                }
                else{
                    sum=0;
                   // product_name.remove(i);
                    if(mOnCartListener!=null){
                        mOnCartListener.onMinusClick(i);
                    }
                }
            }
            else if(q.contains("Piece")){
                qu = (int) Math.round(Double.valueOf(q.replace(" Piece", "")));
                if(qu>1) {
                    int temp = pr/qu;
                    qu--;
                    price.setText("₹"+temp*qu);
                    sum-=temp;
                    quantity.setText(qu + " Piece");
                }
                else{
                    sum=0;
                    if(mOnCartListener!=null){
                        mOnCartListener.onMinusClick(i);
                    }
                }
            }
            else{
                qu = (int) Math.round(Double.valueOf(q.replace(" Bunch", "")));
                if(qu>1) {
                    int temp = pr/qu;
                    qu--;
                    price.setText("₹"+temp*qu);
                    sum-=temp;
                    quantity.setText(qu + " Bunch");
                }
                else{
                    sum=0;
                    if(mOnCartListener!=null){
                        mOnCartListener.onMinusClick(i);
                    }
                }
            }

            if(qu==1){
                minus.setImageResource(R.drawable.ic_baseline_delete_24);
            }

            tot.setText("₹"+sum);
        });

        tot.setText("₹"+sum);

        placeOrder.setOnClickListener(v->{
            Intent intent = new Intent(context,Payment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("total",sum);
            context.startActivity(intent);
            if(place!=null){
                place.onPlaceClick();
            }
        });
        return view;

    }


    public int getSum(){
        return sum;
    }




    public interface OnCartListener{
        void onMinusClick(int position);
    }

    public interface PlaceOrder{
        void onPlaceClick();
    }



//    private void deletedb(String name) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference  = database.getReference().child("Customer").child(user.getUid()).child("Cart").child(name);
//        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    product_name.clear();
//                    product_quantity.clear();
//                    product_price.clear();
//                    getData(new IsAvailableCallBack() {
//                        @Override
//                        public void onAvailableCallBack(boolean isAvailable) {
//                            if(isAvailable){
//                                Intent refresh = new Intent(context,Cart.class);
//                                refresh.putStringArrayListExtra("productName", product_name);
//                                refresh.putStringArrayListExtra("productPrice", product_price);
//                                refresh.putStringArrayListExtra("productQuantity", product_quantity);
//                                refresh.putExtra("location",loction);
//                                refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(refresh);
//                                ((Activity)context).finish();
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void getData(IsAvailableCallBack callBack) {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference();
//
//
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
//        databaseReference.child("Customer").child(user.getUid()).child("Cart").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int count = 0;
//                System.out.println("total count"+snapshot.getChildrenCount());
//                for(DataSnapshot snapshot1:snapshot.getChildren()){
//                    if(snapshot1.exists()){
//                        GenericTypeIndicator<Map<String,String>> to = new GenericTypeIndicator<Map<String,String>>() {};
//                        Map<String,String> products = snapshot1.getValue(to);
//                        for(String i : products.values()){
//                            if(i.contains("₹")){
//                                product_price.add(i);
//                            }else if(i.contains("Kg")){
//                                product_quantity.add(i);
//                            }else{
//                                product_name.add(i);
//                            }
//                        }
//                    }
//                    count++;
//                    System.out.println("Count"+count);
//                    if(count==snapshot.getChildrenCount()){
//                        callBack.onAvailableCallBack(true);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
