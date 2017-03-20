package com.hugsby.shoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugsby.shoppingapp.RealmObjects.ShoppingProduct;

import java.util.List;


public class ProductsAdapter extends ArrayAdapter<ShoppingProduct> {

    int resource;
    Context context;


    public ProductsAdapter(Context _context, int _resource, List<ShoppingProduct> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LinearLayout newView;
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().
                    getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }


        final ShoppingProduct product = getItem(position);

        TextView name = (TextView)newView.findViewById(R.id.product_name);

        name.setText(product.getName());



        return newView;
    }
}
