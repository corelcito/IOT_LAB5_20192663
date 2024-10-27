package com.seguimiento_calorias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdapterSelectFood  extends RecyclerView.Adapter<AdapterSelectFood.SelectFoodViewHolder> {

    private List<Food> itemList;
    private Context context;
    private OnAddClickListener onAddClickListener;
    private OnMinusClickListener onMinusClickListener;
    private OnSelectFood onSelectFood;
    public AdapterSelectFood(Context context, List<Food> itemList, OnAddClickListener onAddClickListener, OnMinusClickListener onMinusClickListener, OnSelectFood onSelectFood) {
        this.context = context;
        this.itemList = itemList;
        this.onAddClickListener = onAddClickListener;
        this.onMinusClickListener = onMinusClickListener;
        this.onSelectFood = onSelectFood;
    }

    @NonNull
    @Override
    public SelectFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_add, parent, false);
        return new AdapterSelectFood.SelectFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectFood.SelectFoodViewHolder holder, int position) {
        Food item = itemList.get(position);
        holder.textActivity.setText(item.name);
        holder.textKcal.setText(item.kcal + " kcal");
        holder.textQty.setText(""+item.qty);

        if(item.selected) {
            holder.containerFood.setStrokeColor(context.getResources().getColor(R.color.green_selected));
            holder.containerControls.setVisibility(View.VISIBLE);
        } else {
            holder.containerFood.setStrokeColor(context.getResources().getColor(R.color.strokeColor));
            holder.containerControls.setVisibility(View.GONE);
        }


        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddClickListener != null) {
                    onAddClickListener.onAddClick(position);
                }
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMinusClickListener != null) {
                    onMinusClickListener.onMinusClick(position);
                }
            }
        });

        holder.containerFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectFood.onSelect(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void update(ArrayList<Food> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    public interface OnAddClickListener {
        void onAddClick(int position);
    }

    public interface OnMinusClickListener {
        void onMinusClick(int position);
    }

    public interface OnSelectFood {
        void onSelect(int position);
    }

    static class SelectFoodViewHolder extends RecyclerView.ViewHolder {
        TextView textActivity, textKcal, textQty;
        ImageView iconConsume, btnAdd, btnMinus;
        MaterialCardView containerFood;
        LinearLayout containerControls;
        SelectFoodViewHolder(View itemView) {
            super(itemView);
            textActivity = itemView.findViewById(R.id.text_activity);
            textKcal = itemView.findViewById(R.id.text_kcal);
            iconConsume = itemView.findViewById(R.id.icon_consume);
            textQty = itemView.findViewById(R.id.text_qty);
            btnAdd = itemView.findViewById(R.id.btn_add);
            btnMinus = itemView.findViewById(R.id.btn_remove);
            containerFood = itemView.findViewById(R.id.container_food);
            containerControls = itemView.findViewById(R.id.container_controls);
        }
    }
}