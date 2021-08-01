package com.example.farmsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Location extends AppCompatActivity {

    ListView location_list;
    TextView text_view_location ;
    private String location;
    String[] location_list_item = {"Ambur","Arcot","Ariyalur","Chennai","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Hosur","Jayankondam","Kallakurichi","Kanchipuram","Kanyakumari","Karaikudi","Karur","Kodaikanal","Kovilpatti","Krishnagiri","Kumbakonam","Madurai","Nagapattinam","Nagercoil","Namakkal","Ooty","Palani","Paramakudi","Perambalur","Pollachi","Pudukkottai","Ramanathapuram","Rameswaram","Salem","Sivagangai","Thanjavur","Theni","Tirunelveli","Tiruppur","Tiruvannamalai","Tiruvarur","Trichy","Tuticorin","Vellore","Villupuram","Virudhunagar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        location_list =(ListView)findViewById(R.id.location_list);
        text_view_location =(TextView)findViewById(R.id.text_view_location);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,location_list_item);
        location_list.setAdapter(adapter);


        location_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                location = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), location + " " + "selected", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
