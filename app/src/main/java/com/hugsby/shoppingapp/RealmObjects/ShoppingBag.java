package com.hugsby.shoppingapp.RealmObjects;


import io.realm.RealmList;
import io.realm.RealmObject;


public class ShoppingBag extends RealmObject {


    private String name;
    public RealmList<ShoppingProduct> shoppingList;

    public ShoppingBag(String name)
    {
        this.name=name;
        shoppingList=new RealmList<>();
    }

    public ShoppingBag(){};

    public RealmList<ShoppingProduct> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(RealmList<ShoppingProduct> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
