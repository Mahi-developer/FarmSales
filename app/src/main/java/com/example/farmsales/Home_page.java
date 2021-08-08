package com.example.farmsales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home_page extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<String> product_data = new ArrayList<String>();
    private ArrayList<String> product_name = new ArrayList<String>();
    private ArrayList<String> product_quantity = new ArrayList<String>();
    private ArrayList<String> product_price = new ArrayList<String>();
    private ArrayList<String> cart_product_name = new ArrayList<String>();
    private ArrayList<String> cart_product_quantity = new ArrayList<String>();
    private ArrayList<String> cart_product_price = new ArrayList<String>();
    private ListView simpleList;
    private final String[] location_list_item = {"Ambur","Arcot","Ariyalur","Chennai","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Hosur","Jayankondam","Kallakurichi","Kanchipuram","Kanyakumari","Karaikudi","Karur","Kodaikanal","Kovilpatti","Krishnagiri","Kumbakonam","Madurai","Nagapattinam","Nagercoil","Namakkal","Ooty","Palani","Paramakudi","Perambalur","Pollachi","Pudukkottai","Ramanathapuram","Rameswaram","Salem","Sivagangai","Thanjavur","Theni","Tirunelveli","Tiruppur","Tiruvannamalai","Tiruvarur","Trichy","Tuticorin","Vellore","Villupuram","Virudhunagar"};
    private String location = "Vellore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_home_page);
        getProducts(location);
        ImageButton userProfile = findViewById(R.id.user_profile);
        auth= FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userProfile.setOnClickListener(l ->
        {
            sign_out();
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        if(user.getPhotoUrl()!=null){
            String photoUrl = user.getPhotoUrl().toString();
            Picasso.get().load(photoUrl).into(userProfile);
        }

        ImageButton locationbt = findViewById(R.id.location);
        locationbt.setOnClickListener(l->
        {
            createLocationDialog();
        });

        ImageButton cart = findViewById(R.id.cart);
        cart.setOnClickListener(l->
        {
            getData(new IsAvailableCallBack() {
                @Override
                public void onAvailableCallBack(boolean isAvailable) {
                    if(isAvailable){
                        Intent intent = new Intent(Home_page.this,Cart.class);
                        intent.putStringArrayListExtra("productName",cart_product_name);
                        intent.putStringArrayListExtra("productPrice",cart_product_price);
                        intent.putStringArrayListExtra("productQuantity",cart_product_quantity);
                        intent.putExtra("location",location);
                        startActivity(intent);
                        cart_product_name.clear();
                        cart_product_price.clear();
                        cart_product_quantity.clear();
                    }

                }
            });

        });

    }

    private void getData(IsAvailableCallBack callBack) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        databaseReference.child("Customer").child(user.getUid()).child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                System.out.println("total count"+snapshot.getChildrenCount());
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.exists()){
                        GenericTypeIndicator<Map<String,String>> to = new GenericTypeIndicator<Map<String,String>>() {};
                        Map<String,String> products = snapshot1.getValue(to);
                        for(String i : products.values()){
                            if(i.contains("₹")){
                                cart_product_price.add(i);
                            }else if(i.contains("Kg")||i.contains("Piece")||i.contains("Bunch")){
                                cart_product_quantity.add(i);
                            }else{
                                cart_product_name.add(i);
                            }
                        }
                    }
                    count++;
                    System.out.println("Count"+count);
                    if(count==snapshot.getChildrenCount()){
                        callBack.onAvailableCallBack(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProducts(String location) {
        product_name.clear();
        product_price.clear();
        product_quantity.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("Products").child(location).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot2:snapshot.getChildren()){
                    if(snapshot2.exists()){
                        DatabaseReference reference1 = database.getReference();
                        reference1.child("Products").child(location).child(snapshot2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        Toast.makeText(Home_page.this,"No Products Found in your Location",Toast.LENGTH_LONG).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Home_page.this);
        builder.setTitle("Select your District : ");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,location_list_item);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                location = adapter.getItem(i);
                AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(Home_page.this);
                confirmationBuilder.setMessage(location);
                confirmationBuilder.setTitle("Your District is");
                confirmationBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                        getProducts(location);
                    }
                });
                confirmationBuilder.show();
            }
        });
        builder.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void setDataRates(){
        System.out.println(product_name.get(0));
        simpleList = (ListView) findViewById(R.id.list);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),"Customer",location,product_name, product_quantity, product_price);
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



//    private class GetRates extends AsyncTask<Void,Void,Void> {
//
//        private String locate;
//        GetRates(String location){
//            this.locate = location.toLowerCase();
//        }
//
//        @Override
//        protected Void doInBackground(Void...n) {
//            try {
//                System.out.println("Location - "+locate);
//                String url = "https://rates.goldenchennai.com/vegetable-price/"+locate+"-vegetable-price-today/";
//                System.out.println("URL = "+url);
//                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
//                org.jsoup.select.Elements rows = doc.select("tr");
//                for (org.jsoup.nodes.Element row : rows) {
//                    org.jsoup.select.Elements columns = row.select("td");
//                    for (org.jsoup.nodes.Element column : columns) {
//                        product_data.add(column.text());
//                    }
//                }
//                for (int i = 0; i < product_data.size(); i = i + 3) {
//                    product_name.add(product_data.get(i));
//                }
//                for (int i = 1; i < product_data.size(); i = i + 3) {
//                    product_quantity.add(product_data.get(i));
//                }
//                for (int i = 2; i < product_data.size(); i = i + 3) {
//                    product_price.add(product_data.get(i));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//            setDataRates();
//        }
//    }
}