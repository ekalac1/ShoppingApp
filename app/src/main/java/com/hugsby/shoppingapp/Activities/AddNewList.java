package com.hugsby.shoppingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hugsby.shoppingapp.R;
import com.hugsby.shoppingapp.RealmObjects.ShoppingBag;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddNewList extends AppCompatActivity {

    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);

        Button addBag=(Button)findViewById(R.id.add);
       final EditText bagName=(EditText)findViewById(R.id.edit_name);
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        addBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=bagName.getText().toString();
                if (name.equals(""))
                {
                    bagName.setError("This field is mandatory");
                }
                else if (name.length()>=20)
                {
                    bagName.setError("Max number of characters is 20");
                }
                else
                {
                    realm.beginTransaction();
                    ShoppingBag temp = new ShoppingBag(name);
                    realm.copyToRealm(temp);
                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(), R.string.added_shopping_bag, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), AddNewProduct.class);
                    intent.putExtra("name", name);

                    startActivity(intent);
                }

            }
        });
    }
}
