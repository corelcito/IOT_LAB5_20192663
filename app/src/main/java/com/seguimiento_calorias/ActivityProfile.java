package com.seguimiento_calorias;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ActivityProfile extends AppCompatActivity {
    private EditText inputWeight, inputHeight, inputAge;
    private TextInputLayout tilWeight, tilHeight, tilAge;
    private RadioGroup genderGroup;
    private Spinner spinnerActivityLevel, spinnerGoal;
    private TextView textViewResult;
    private MaterialButton btnCalculate;
    private MaterialButton btnSave;
    private ArrayList<String> listActivity;
    private ArrayList<String> listGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inputWeight = findViewById(R.id.edt_weight);
        inputHeight = findViewById(R.id.edt_height);
        inputAge = findViewById(R.id.edt_age);
        genderGroup = findViewById(R.id.group_gender);
        spinnerActivityLevel = findViewById(R.id.spinner_activity);
        spinnerGoal = findViewById(R.id.spinner_goal);
        textViewResult = findViewById(R.id.text_result);
        btnCalculate = findViewById(R.id.btn_calculate);
        btnSave = findViewById(R.id.btn_save);
        tilHeight = findViewById(R.id.til_height);
        tilWeight = findViewById(R.id.til_weight);
        tilAge = findViewById(R.id.til_age);
        ArrayAdapter<String> adapterGoal = new ArrayAdapter<String>(ActivityProfile.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.goal));
        ArrayAdapter<String> adapterActivities = new ArrayAdapter<String>(ActivityProfile.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.activities));
        spinnerGoal.setAdapter(adapterGoal);
        spinnerActivityLevel.setAdapter(adapterActivities);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveResults();
            }
        });
        inputWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilWeight.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilHeight.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilAge.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    calculateCalories();
                }
            }
        });
    }

    private void saveResults() {
        double weight = Double.parseDouble(inputWeight.getText().toString());
        double height = Double.parseDouble(inputHeight.getText().toString());
        int age = Integer.parseInt(inputAge.getText().toString());
        boolean isMale = genderGroup.getCheckedRadioButtonId() == R.id.radio_male;
        String gender = isMale ? "Masculino" : "Femenino";
        String activityLevel = spinnerActivityLevel.getSelectedItem().toString();
        String goal = spinnerGoal.getSelectedItem().toString();
        String calories = textViewResult.getText().toString();

        if(calories.isEmpty()) {
            Toast.makeText(this, "Calcule el total de calorias antes de continuar.", Toast.LENGTH_SHORT).show();
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("peso", weight);
        resultIntent.putExtra("altura", height);
        resultIntent.putExtra("edad", age);
        resultIntent.putExtra("genero", gender);
        resultIntent.putExtra("nivel_actividad", activityLevel);
        resultIntent.putExtra("objetivo", goal);
        resultIntent.putExtra("calorias_recomendadas", calories);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (inputWeight.getText().toString().isEmpty()) {
            tilWeight.setError("El campo no puede estar vacío");
            isValid = false;
        }

        if (inputHeight.getText().toString().isEmpty()) {
            tilHeight.setError("El campo no puede estar vacío");
            isValid = false;
        }

        if (inputAge.getText().toString().isEmpty()) {
            tilAge.setError("El campo no puede estar vacío");
            isValid = false;
        }

        return isValid;
    }

    private void calculateCalories() {
        double weight = Double.parseDouble(inputWeight.getText().toString());
        double height = Double.parseDouble(inputHeight.getText().toString());
        int age = Integer.parseInt(inputAge.getText().toString());
        boolean isMale = genderGroup.getCheckedRadioButtonId() == R.id.radio_male;

        double tmb = isMale
                ? (10 * weight) + (6.25 * height) - (5 * age) + 5
                : (10 * weight) + (6.25 * height) - (5 * age) - 161;

        double activityMultiplier = getActivityMultiplier(spinnerActivityLevel.getSelectedItemPosition());
        double dailyCalories = tmb * activityMultiplier;

        String goal = spinnerGoal.getSelectedItem().toString();
        if (goal.equals("Subir de peso")) {
            dailyCalories += 500;
        } else if (goal.equals("Bajar de peso")) {
            dailyCalories -= 300;
        }

        textViewResult.setText(""+dailyCalories);
    }

    private double getActivityMultiplier(int position) {
        switch (position) {
            case 1: return 1.375;
            case 2: return 1.55;
            case 3: return 1.725;
            case 4: return 1.9;
            default: return 1.2;
        }
    }
}