package com.taxi.friend.drivers.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.taxi.friend.drivers.R;

public class CarFrontPictureActivity extends AppCompatActivity {

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_front_picture);

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextStep();
            }
        });
    }

    public void showNextStep() {
        Intent intent = new Intent(this, CarBackPictureActivity.class);

        startActivity(intent);
    }
}
