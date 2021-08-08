package com.example.farmsales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> product_name;
    ArrayList<String> product_quantity;
    ArrayList<String> product_price;
    LayoutInflater inflter;
    int sum;
    TextView tot;


    DashboardAdapter(){

    }

    DashboardAdapter(Context applicationContext,ArrayList<String> product_name, ArrayList<String> product_quantity, ArrayList<String> product_price) {
        this.context = applicationContext;
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
        view = inflter.inflate(R.layout.dashboard_listview, null);

        ImageView image_view = (ImageView) view.findViewById(R.id.product_image_dash);
        new CustomAdapter().getData(image_view, product_name.get(i));
        TextView name = (TextView) view.findViewById(R.id.product_name_dash);
        TextView quantity = (TextView) view.findViewById(R.id.product_quantity_dash);
        TextView price = (TextView) view.findViewById(R.id.product_price_dash);
        name.setText(product_name.get(i));
        quantity.setText(product_quantity.get(i));
        price.setText(product_price.get(i));


        return view;

    }
}
