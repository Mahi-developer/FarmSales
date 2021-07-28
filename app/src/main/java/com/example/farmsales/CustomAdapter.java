package com.example.farmsales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        name.setText(product_name.get(i));
        quantity.setText(product_quantity.get(i));
        price.setText(product_price.get(i));
        return view;
    }
}
