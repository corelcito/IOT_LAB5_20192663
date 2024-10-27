package com.seguimiento_calorias;

import java.util.ArrayList;
import java.util.List;

public class GlobalLists {

    public static  ArrayList<Food> listFoods = new ArrayList<>();


    public static  void addFood(Food food) {
        listFoods.add(food);
    }
    public static void addFoods(ArrayList<Food> list) {
        listFoods.addAll(list);
    }
}
