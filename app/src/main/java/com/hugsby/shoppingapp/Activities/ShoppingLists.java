package com.hugsby.shoppingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hugsby.shoppingapp.OnSwipeTouchListener;
import com.hugsby.shoppingapp.ProductsAdapter;
import com.hugsby.shoppingapp.R;
import com.hugsby.shoppingapp.RealmObjects.ShoppingBag;
import com.hugsby.shoppingapp.RealmObjects.ShoppingProduct;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ShoppingLists extends AppCompatActivity {

    private ActionBarDrawerToggle mToogle;
    NavigationView slideMenu;
    DrawerLayout mDrawerLayout;
    Realm realm;

    List<ShoppingProduct> shoppingProducts;
    ProductsAdapter productsAdapter;
    ListView list;

    RealmResults<ShoppingBag> bags;

    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_shopping_lists);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);

        final TextView proba = (TextView)findViewById(R.id.message);

        final TextView total = (TextView)findViewById(R.id.total);
        total.setText("");

        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        slideMenu = (NavigationView) findViewById(R.id.navigationSlide);

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        shoppingProducts=new ArrayList<>();
        productsAdapter=new ProductsAdapter(this, R.layout.product_element, shoppingProducts);
        list = (ListView)findViewById(R.id.product_list);
        list.setAdapter(productsAdapter);

        realm.beginTransaction();

        bags = realm.where(ShoppingBag.class).findAll();

        for (ShoppingBag bag:bags
             ) {
            slideMenu.getMenu().getItem(1).getSubMenu().add(bag.getName());
        //    slideMenu.getMenu().getItem(1).getSubMenu().removeItem();
        }

        if (bags.size()==0)
        {
            proba.setText(R.string.no_bags);
        }
        else
        {
            shoppingProducts.clear();
            shoppingProducts.addAll(bags.last().getShoppingList());
            ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();
            proba.setVisibility(View.GONE);
            name=bags.last().getName();

            float total_price=0;

            for (ShoppingProduct p : bags.last().getShoppingList())
            {
                if (!p.getQuantity().equals("unknown"))
                {
                    total_price+=Float.valueOf(p.getQuantity())*Float.valueOf(p.getPrice());
                }
            }

            total.setText("Total: "+total_price);

        }

        realm.commitTransaction();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ProductDetails.class);
                intent.putExtra("name", name);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });


        list.setOnTouchListener( new OnSwipeTouchListener()
        {
            public boolean onSwipeTop() {

               int i= list.getSelectedItemPosition();
                Toast.makeText(getApplicationContext(), "top"+i, Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        slideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.new_list:
                        startActivity(new Intent(getApplicationContext(), AddNewList.class));
                        break;
                    case R.id.maps:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        break;
                    case R.id.ex_list:
                        break;
                    case R.id.g2:
                        break;
                    default:
                        proba.setText(item.getTitle());

                        realm.beginTransaction();

                        name=item.getTitle().toString();

                        ShoppingBag bag = realm.where(ShoppingBag.class).equalTo("name", item.getTitle().toString()).findFirst();

                        shoppingProducts.clear();

                        shoppingProducts.addAll(bag.getShoppingList());

                        proba.setVisibility(View.GONE);

                        ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

                        proba.setText(item.getTitle()+" "+shoppingProducts.size());

                        float total_price=0;

                        for (ShoppingProduct p : bag.getShoppingList())
                        {
                            if (!p.getQuantity().equals("unknown"))
                            {
                                total_price+=Float.valueOf(p.getQuantity())*Float.valueOf(p.getPrice());
                            }
                        }

                        total.setText("Total: "+total_price);

                        mDrawerLayout.closeDrawer(Gravity.LEFT);

                        realm.commitTransaction();
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.new_product:
                Intent intent = new Intent(getApplicationContext(), AddNewProduct.class);
                intent.putExtra("name", name);
                startActivity(intent);
                break;
            case R.id.delete:
                realm.beginTransaction();
                ShoppingBag bag = realm.where(ShoppingBag.class).equalTo("name", name).findFirst();
                bag.deleteFromRealm();
                realm.commitTransaction();
                startActivity(new Intent(getApplicationContext(), ShoppingLists.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void hardCodeData()
    {
        realm.beginTransaction();

        ShoppingProduct s=new ShoppingProduct();
        s.setName("Nyx Soft Matte lip cream");
        s.setMeasuringUnit("pc");
        s.setQuantity("2");
        s.setPrice(15);

        ShoppingProduct n =s;
        n.setName("Browpomade in 02 blonde");

        ShoppingProduct k=s;
        k.setName("Eyeshadow insurance primer");

        ShoppingBag shoppingLists = new ShoppingBag("makeup");
        RealmList<ShoppingProduct> list= new RealmList<>();
        list.add(s);
        list.add(n);
        list.add(k);
        shoppingLists.setShoppingList(list);

        realm.copyToRealm(shoppingLists);

        realm.commitTransaction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();


        inflater.inflate(R.menu.optinos_menu, menu);
        if (bags.size()==0)
        {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
        else
        {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        }
        return true;
    }
}
