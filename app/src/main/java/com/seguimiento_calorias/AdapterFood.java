package com.seguimiento_calorias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterFood  extends RecyclerView.Adapter<AdapterFood.FoodsViewHolder> {

    private List<Food> itemList;
    private Context context;

    public AdapterFood(Context context, List<Food> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AdapterFood.FoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new AdapterFood.FoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFood.FoodsViewHolder holder, int position) {
        Food item = itemList.get(position);
        holder.textActivity.setText(item.name);
        holder.textKcal.setText(item.kcal + " kcal");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void update(ArrayList<Food> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    static class FoodsViewHolder extends RecyclerView.ViewHolder {
        TextView textActivity, textKcal;
        ImageView iconConsume;

        FoodsViewHolder(View itemView) {
            super(itemView);
            textActivity = itemView.findViewById(R.id.text_activity);
            textKcal = itemView.findViewById(R.id.text_kcal);
            iconConsume = itemView.findViewById(R.id.icon_consume);
        }
    }
}