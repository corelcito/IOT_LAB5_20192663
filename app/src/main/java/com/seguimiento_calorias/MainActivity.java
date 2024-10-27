package com.seguimiento_calorias;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.graphics.RectKt;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String weight = "";
    String height = "";
    String age = "";
    String gender = "";
    String activityLevel = "";
    String object = "";
    String kcalGoal = "";

    private TextView  textkcalconsume, textkcalgoal, textkcalburn, textkcaldiary;
    private ImageView btnsettings;
    private LinearLayout btnWalk,btnRun,btnGym,btnaddfood, btnreceipts, btnnotis;
    private RecyclerView rvConsumes;

    private ArrayList<ItemConsume> listConsumeActivity = new ArrayList<>();
    private ItemConsumeAdapter adapterConsumeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        openActivityProfile();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        textkcalconsume = findViewById(R.id.text_kcal_consume);
        textkcalgoal = findViewById(R.id.text_kcal_goal);
        textkcalburn = findViewById(R.id.text_kcal_burn);
        textkcaldiary = findViewById(R.id.text_kcal_diary);
        btnsettings = findViewById(R.id.btn_settings);
        btnaddfood = findViewById(R.id.btn_add_food);
        btnreceipts = findViewById(R.id.btn_receipts);
        btnnotis = findViewById(R.id.btn_notis);
        btnWalk = findViewById(R.id.btn_walk);
        btnRun = findViewById(R.id.btn_run);
        btnGym = findViewById(R.id.btn_gym);
        rvConsumes = findViewById(R.id.rv_consumes);

        GlobalLists.addFood(new Food("Arroz blanco cocido", 130, 1, R.drawable.baseline_food_bank_24, false));
        GlobalLists.addFood(new Food("Pechuga de pollo a la plancha", 165, 1, R.drawable.baseline_food_bank_24, false));
        GlobalLists.addFood(new Food("Manzana", 52, 1, R.drawable.baseline_food_bank_24, false));
        GlobalLists.addFood(new Food("Brócoli cocido", 55, 1, R.drawable.baseline_food_bank_24, false));
        GlobalLists.addFood(new Food("Pan integral ", 250, 1, R.drawable.baseline_food_bank_24, false));
        adapterConsumeActivity = new ItemConsumeAdapter(MainActivity.this,listConsumeActivity);
        rvConsumes.setAdapter(adapterConsumeActivity);

        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityProfile();
            }
        });
        btnWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog("Caminar",R.drawable.baseline_directions_walk_24);
            }
        });

        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog("Correr",R.drawable.baseline_directions_run_24);
            }
        });

        btnGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog("Gym",R.drawable.baseline_fitness_center_24);
            }
        });

        btnreceipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
            }
        });

        btnaddfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodLauncher.launch(new Intent(MainActivity.this, FoodLogActivity.class));
            }
        });

        btnnotis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotisActivity.class));
            }
        });
    }

    private ActivityResultLauncher<Intent> profileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    weight = String.valueOf(data.getDoubleExtra("peso", 0));
                    height = String.valueOf(data.getDoubleExtra("altura", 0));
                    age = String.valueOf(data.getIntExtra("edad", 0));
                    gender = data.getStringExtra("genero");
                    activityLevel = data.getStringExtra("nivel_actividad");
                    object = data.getStringExtra("objetivo");
                    kcalGoal = data.getStringExtra("calorias_recomendadas");
                    textkcalgoal.setText(kcalGoal);
                } else {
                    if(kcalGoal.isEmpty()) {
                        openActivityProfile();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> addFoodLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    ItemConsume item  = (ItemConsume) data.getSerializableExtra("itemConsume");
                    addActivityConsume(item);
                } else {
                    if(kcalGoal.isEmpty()) {
                        openActivityProfile();
                    }
                }
            }
    );
    private void openActivityProfile() {
        Intent intent = new Intent(this, ActivityProfile.class);
        profileLauncher.launch(intent);
    }

    private void addActivityConsume(ItemConsume item) {
        listConsumeActivity.add(item);
        adapterConsumeActivity.update(listConsumeActivity);
        int totalBurn = 0;
        int totalConsume = 0;
        int totalDiary = 0;
        for(ItemConsume itemConsume : listConsumeActivity) {
            if(itemConsume.kcal < 0) {
                totalBurn += (-1)*itemConsume.kcal*itemConsume.qty;
            } else {
                totalConsume += itemConsume.kcal*itemConsume.qty;
            }
            totalDiary+=itemConsume.kcal*itemConsume.qty;
        }

        textkcalburn.setText(""+totalBurn);
        textkcalconsume.setText(""+totalConsume);
        textkcaldiary.setText(""+totalDiary);

        if(totalDiary > Double.valueOf(kcalGoal)) {
            showCalorieWarningDialog();
        }
    }

    public void showCustomDialog( String name, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Detalles");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextView nameTextView = new TextView(MainActivity.this);
        nameTextView.setText("Actividad: " + name);
        nameTextView.setPadding(0, 0, 0, 20);
        layout.addView(nameTextView);

        EditText amountEditText = new EditText(MainActivity.this);
        amountEditText.setHint("Gasto calórico");
        amountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(amountEditText);
        builder.setView(layout);
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String amount = amountEditText.getText().toString();
            if (!amount.isEmpty()) {
                ItemConsume item = new ItemConsume(name,(-1)*Integer.valueOf(amount),icon,1);
                addActivityConsume(item);
            } else {
                Toast.makeText(MainActivity.this, "Por favor ingrese una cantidad", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void showCalorieWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this
        );
        builder.setMessage("Estás consumiendo más calorías de las que debes, intenta hacer alguna actividad sugerida.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showRemenberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this
        );
        builder.setMessage("No has agregado alguna comida o acividad durante el dia.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}