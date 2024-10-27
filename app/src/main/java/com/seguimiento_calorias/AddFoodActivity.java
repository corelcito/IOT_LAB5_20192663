package com.seguimiento_calorias;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddFoodActivity extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView rvFood;
    FloatingActionButton btnAdd;
    AdapterFood adapterFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back);
        rvFood = findViewById(R.id.rv_foods);
        btnAdd = findViewById(R.id.btn_add);

        adapterFood = new AdapterFood(AddFoodActivity.this, GlobalLists.listFoods);
        rvFood.setAdapter(adapterFood);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(R.drawable.baseline_food_bank_24);
            }
        });
    }

    private void addFood(String name, int amount, int icon) {
        Food food = new Food(name, amount, 1, icon, false);
        GlobalLists.addFood(food);
        adapterFood.update(GlobalLists.listFoods);
    }

    public void showCustomDialog( int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodActivity.this);
        builder.setTitle("Detalles");

        LinearLayout layout = new LinearLayout(AddFoodActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextView nameTextView = new TextView(AddFoodActivity.this);
        nameTextView.setText("Alimento: ");
        nameTextView.setPadding(0, 0, 0, 20);
        layout.addView(nameTextView);

        EditText additionalNameEditText = new EditText(AddFoodActivity.this);
        additionalNameEditText.setHint("Comida");
        layout.addView(additionalNameEditText);

        EditText amountEditText = new EditText(AddFoodActivity.this);
        amountEditText.setHint("Gasto calórico");
        amountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(amountEditText);

        builder.setView(layout);
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = additionalNameEditText.getText().toString();
            String amount = amountEditText.getText().toString();

            if (!amount.isEmpty() && !name.isEmpty()) {
                addFood(name, Integer.valueOf(amount), icon);
            } else {
                Toast.makeText(AddFoodActivity.this, "Por favor ingrese todos los datos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }


}