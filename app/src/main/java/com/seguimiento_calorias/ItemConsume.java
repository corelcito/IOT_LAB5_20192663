package com.seguimiento_calorias;

import java.io.Serializable;

public class ItemConsume implements Serializable {

    String activity;
    int kcal;
    int icon;
    int qty;

    public ItemConsume(String activity, int kcal, int icon, int qty) {
        this.activity = activity;
        this.kcal = kcal;
        this.icon = icon;
        this.qty = qty;
    }
}
