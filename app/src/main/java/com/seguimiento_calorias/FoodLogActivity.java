package com.seguimiento_calorias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FoodLogActivity extends AppCompatActivity {
    ImageView btnBack;
    MaterialButton btnSave;
    RecyclerView rvFood;
    AdapterSelectFood adapterSelectFood;
    ArrayList<Food> listFood = new ArrayList<>();
    Food foodSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_log);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.btn_back);
        rvFood = findViewById(R.id.rv_foods);
        btnSave = findViewById(R.id.btn_save);

        listFood.clear();
        listFood = GlobalLists.listFoods;

        adapterSelectFood = new AdapterSelectFood(FoodLogActivity.this, listFood,
                new AdapterSelectFood.OnAddClickListener() {
                    @Override
                    public void onAddClick(int position) {
                        listFood.get(position).qty = listFood.get(position).qty + 1;
                        adapterSelectFood.update(listFood);
                    }
                },
                new AdapterSelectFood.OnMinusClickListener() {
                    @Override
                    public void onMinusClick(int position) {
                        if (listFood.get(position).qty - 1 > 0) {
                            listFood.get(position).qty = listFood.get(position).qty - 1;
                            adapterSelectFood.update(listFood);
                        }
                    }
                },
                new AdapterSelectFood.OnSelectFood() {
                    @Override
                    public void onSelect(int position) {
                        for(Food item : listFood) {
                            item.selected = false;
                        }
                        listFood.get(position).selected = true;
                        adapterSelectFood.update(listFood);
                        foodSelected = listFood.get(position);
                    }
                });
        rvFood.setAdapter(adapterSelectFood);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foodSelected != null) {
                    ItemConsume item = new ItemConsume(foodSelected.name, foodSelected.kcal,foodSelected.icon,foodSelected.qty);
                    Intent intent = new Intent();
                    intent.putExtra("itemConsume",item);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(FoodLogActivity.this, "Seleccione algun alimento.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}