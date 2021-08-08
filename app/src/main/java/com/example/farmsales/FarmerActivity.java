package com.example.farmsales;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.ArrayList;

public class FarmerActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<String> product_data = new ArrayList<String>();
    private ArrayList<String> product_name = new ArrayList<String>();
    private final ArrayList<String> product_quantity = new ArrayList<String>();
    private ArrayList<String> product_price = new ArrayList<String>();
    private ListView simpleList;
    private final String[] location_list_item = {"Ambur","Arcot","Ariyalur","Chennai","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Hosur","Jayankondam","Kallakurichi","Kanchipuram","Kanyakumari","Karaikudi","Karur","Kodaikanal","Kovilpatti","Krishnagiri","Kumbakonam","Madurai","Nagapattinam","Nagercoil","Namakkal","Ooty","Palani","Paramakudi","Perambalur","Pollachi","Pudukkottai","Ramanathapuram","Rameswaram","Salem","Sivagangai","Thanjavur","Theni","Tirunelveli","Tiruppur","Tiruvannamalai","Tiruvarur","Trichy","Tuticorin","Vellore","Villupuram","Virudhunagar"};
    private String location = "Vellore";
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        simpleList = (ListView) findViewById(R.id.list_farmer);
        new GetRates(location).execute();
        ImageButton userProfile = findViewById(R.id.user_profile_farmer);
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

        ImageButton Locate = findViewById(R.id.location_farmer);
        Locate.setOnClickListener(l->
        {
            createLocationDialog();
        });

        ImageButton dash = findViewById(R.id.cart_farmer);
        dash.setOnClickListener(l->
        {
            Intent intent = new Intent(this,Dashboard.class);
            intent.putExtra("loc", location);
            startActivity(intent);
        });
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("pos"+i);
            }
        });


    }

    private void createLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(FarmerActivity.this);
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
                AlertDialog.Builder confirmationBuilder = new AlertDialog.Builder(FarmerActivity.this);
                confirmationBuilder.setMessage(location);
                confirmationBuilder.setTitle("Your District is");
                confirmationBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                        new GetRates(location).execute();
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
        customAdapter = new CustomAdapter(getApplicationContext(),"Farmer",location,product_name, product_quantity, product_price);
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

        private String locate;
        GetRates(String location){
            this.locate = location.toLowerCase();
        }

        @Override
        protected Void doInBackground(Void...n) {
            try {
                System.out.println("Location - "+locate);
                String url = "https://rates.goldenchennai.com/vegetable-price/"+locate+"-vegetable-price-today/";
                System.out.println("URL = "+url);
                org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                org.jsoup.select.Elements rows = doc.select("tr");
                for (org.jsoup.nodes.Element row : rows) {
                    org.jsoup.select.Elements columns = row.select("td");
                    for (org.jsoup.nodes.Element column : columns) {
                        product_data.add(column.text());
                    }
                }
                for (int i = 0; i < product_data.size(); i = i + 3) {
                    product_name.add(product_data.get(i));
                }
                for (int i = 1; i < product_data.size(); i = i + 3) {
                    product_quantity.add(product_data.get(i));
                }
                for (int i = 2; i < product_data.size(); i = i + 3) {
                    product_price.add("₹"+product_data.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            setDataRates();
        }
    }
}