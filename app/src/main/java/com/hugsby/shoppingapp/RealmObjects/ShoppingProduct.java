package com.hugsby.shoppingapp.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by Hugsby on 19-Mar-17.
 */

public class ShoppingProduct extends RealmObject {


    public String name;
    public String quantity;
    public float price;
    public String measuringUnit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }
}
