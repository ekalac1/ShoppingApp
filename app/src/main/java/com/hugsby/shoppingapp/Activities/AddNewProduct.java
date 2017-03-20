package com.hugsby.shoppingapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hugsby.shoppingapp.R;
import com.hugsby.shoppingapp.RealmObjects.ShoppingBag;
import com.hugsby.shoppingapp.RealmObjects.ShoppingProduct;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddNewProduct extends AppCompatActivity {

    String name;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        getSupportActionBar().setTitle(R.string.add_new_title);

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        name = getIntent().getStringExtra("name");

        Button add = (Button) findViewById(R.id.add);
        final EditText nameEdit = (EditText) findViewById(R.id.input_name);
        final EditText quantityEdit = (EditText) findViewById(R.id.quantity);
        final EditText priceEdit = (EditText) findViewById(R.id.expected_price);
        final EditText unitEdit = (EditText)findViewById(R.id.unit);
        final Button addPhoto = (Button)findViewById(R.id.take_photo);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 1);

                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = nameEdit.getText().toString();
                String productQuantity = quantityEdit.getText().toString();
                String productExpectedPrice = (priceEdit.getText().toString());
                String productUnit = unitEdit.getText().toString();

                if (productName.equals(""))
                {
                    nameEdit.setError("This field is mandatory");
                }
                else if (productQuantity.equals(""))
                {
                    quantityEdit.setError("This field is mandatory");
                }
                else if (productUnit.equals(""))
                {
                    unitEdit.setError("This field is mandatory");
                }
                else
                {
                   if (!realm.isInTransaction()) realm.beginTransaction();
                    ShoppingBag bag = realm.where(ShoppingBag.class).equalTo("name", name).findFirst();
                    ShoppingProduct p = new ShoppingProduct();
                    p.setName(productName);
                    if (productExpectedPrice.equals(""))
                    p.setPrice(0);
                    else
                        p.setPrice(Float.parseFloat(productExpectedPrice));
                    p.setMeasuringUnit(productUnit);
                    if (productQuantity.equals("")) productQuantity="unknown";
                    p.setQuantity(productQuantity);
                    bag.getShoppingList().add(p);
                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(), "Product added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ShoppingLists.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            final ImageView mImageView = (ImageView)findViewById(R.id.photo);
            mImageView.setImageBitmap(imageBitmap);
        }
    }
}
