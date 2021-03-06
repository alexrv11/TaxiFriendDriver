package com.taxi.friend.drivers.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.taxi.friend.drivers.R;

public class RegisterWelcomeActivity extends AppCompatActivity {

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_welcome);

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextStep();
            }
        });
    }

    public void showNextStep() {
        Intent intent = new Intent(this, DriverLicenceFrontActivity.class);

        startActivity(intent);
    }
}
