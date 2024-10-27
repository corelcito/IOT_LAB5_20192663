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

public class ItemConsumeAdapter extends RecyclerView.Adapter<ItemConsumeAdapter.ItemConsumeViewHolder> {

    private List<ItemConsume> itemList;
    private Context context;

    public ItemConsumeAdapter(Context context, List<ItemConsume> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemConsumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consume_activity, parent, false);
        return new ItemConsumeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemConsumeViewHolder holder, int position) {
        ItemConsume item = itemList.get(position);
        holder.textActivity.setText(item.activity);
        if(item.qty == 1 ) {
            holder.textKcal.setText(item.kcal + " kcal" );
        } else {
            holder.textKcal.setText(item.kcal + " kcal" + " x "+item.qty);
        }

        holder.iconConsume.setImageResource(item.icon);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void update(ArrayList<ItemConsume> listConsumeActivity) {
        itemList = listConsumeActivity;
        notifyDataSetChanged();
    }

    static class ItemConsumeViewHolder extends RecyclerView.ViewHolder {
        TextView textActivity, textKcal;
        ImageView iconConsume;

        ItemConsumeViewHolder(View itemView) {
            super(itemView);
            textActivity = itemView.findViewById(R.id.text_activity);
            textKcal = itemView.findViewById(R.id.text_kcal);
            iconConsume = itemView.findViewById(R.id.icon_consume);
        }
    }
}
