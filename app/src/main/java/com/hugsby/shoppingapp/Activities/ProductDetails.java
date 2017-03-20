package com.hugsby.shoppingapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.hugsby.shoppingapp.R;
import com.hugsby.shoppingapp.RealmObjects.ShoppingBag;
import com.hugsby.shoppingapp.RealmObjects.ShoppingProduct;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ProductDetails extends AppCompatActivity {

    String name;
    int position;

    Realm realm;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    ShoppingProduct shoppingProduct;

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

        name = getIntent().getStringExtra("name");
        position = getIntent().getIntExtra("position", 0);

        realm.beginTransaction();

        ShoppingBag bag = realm.where(ShoppingBag.class).equalTo("name", name).findFirst();

        shoppingProduct = bag.getShoppingList().get(position);

        realm.commitTransaction();

        final TextView product_name = (TextView) findViewById(R.id.product_name);
        final TextView product_quantity = (TextView) findViewById(R.id.quantity);
        final TextView product_unit = (TextView) findViewById(R.id.unit);
        final TextView expected_price = (TextView) findViewById(R.id.expected_price);

        product_name.setText(shoppingProduct.getName());
        product_quantity.setText(shoppingProduct.getQuantity());
        product_unit.setText(shoppingProduct.getMeasuringUnit());
        expected_price.setText(String.valueOf(shoppingProduct.getPrice()));

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        // Toast.makeText(getApplicationContext(), name+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.social_media_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.facebook:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    final ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentTitle(shoppingProduct.getName())
                            .build();
                    shareDialog.show(content);
                }
                break;
            case R.id.share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(android.content.Intent.EXTRA_TEXT, shoppingProduct.getName());
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(i,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) startActivity(Intent.createChooser(i, "Share using"));

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
