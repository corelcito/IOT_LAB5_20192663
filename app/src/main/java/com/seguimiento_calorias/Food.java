package com.seguimiento_calorias;

public class Food {
    String name;
    int kcal;
    int qty;
    int icon;
    boolean selected;

    public Food(String name, int kcal, int qty, int icon,boolean selected) {
        this.name = name;
        this.kcal = kcal;
        this.qty = qty;
        this.icon = icon;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
