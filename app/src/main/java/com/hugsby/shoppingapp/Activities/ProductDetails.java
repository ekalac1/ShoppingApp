package com.hugsby.shoppingapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hugsby.shoppingapp.R;
import com.hugsby.shoppingapp.RealmObjects.ShoppingBag;
import com.hugsby.shoppingapp.RealmObjects.ShoppingProduct;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ProductDetails extends AppCompatActivity {

    String name;
    int position;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        name=getIntent().getStringExtra("name");
        position=getIntent().getIntExtra("position", 0);

        realm.beginTransaction();

        ShoppingBag bag = realm.where(ShoppingBag.class).equalTo("name",name).findFirst();

        ShoppingProduct shoppingProduct=bag.getShoppingList().get(position);

        final TextView product_name=(TextView)findViewById(R.id.product_name);
        final TextView product_quantity=(TextView)findViewById(R.id.quantity);
        final TextView product_unit=(TextView) findViewById(R.id.unit);
        final TextView expected_price=(TextView) findViewById(R.id.expected_price);

        product_name.setText(shoppingProduct.getName());
        product_quantity.setText(shoppingProduct.getQuantity());
        product_unit.setText(shoppingProduct.getMeasuringUnit());
        expected_price.setText(String.valueOf(shoppingProduct.getPrice()));




       // Toast.makeText(getApplicationContext(), name+position, Toast.LENGTH_SHORT).show();
    }
}
