package com.taxi.friend.drivers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.taxi.friend.drivers.register.RegisterWelcomeActivity;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rootLayout;

    TextView txtTitle;
    TextView txtTitle2;
    TextView txtBanner;

    Button btnSignIn;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.rootLayout);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtTitle =  findViewById(R.id.title_app_primary);
        txtTitle2 = findViewById(R.id.title_app);
        txtBanner = findViewById(R.id.txt_rider_app);

        //events
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Inicia Sesion");
        dialog.setMessage("");

        LayoutInflater inflater = LayoutInflater.from(this);
        View loginLayout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText editEMail = loginLayout.findViewById(R.id.editMail);

        final MaterialEditText editPassword = loginLayout.findViewById(R.id.editPassword);


        dialog.setView(loginLayout);

        dialog.setPositiveButton("Iniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

                btnSignIn.setEnabled(false);

                if (TextUtils.isEmpty(editEMail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter your password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String userName = editEMail.getText().toString();
                String password = editPassword.getText().toString();
                /*TaxiGlobalInfo.driverInfo = new TaxiDriver();
                TaxiGlobalInfo.driverInfo.setName("Test Name");
                TaxiGlobalInfo.driverInfo.setCredit(0);
                TaxiGlobalInfo.driverInfo.setId("6");
                */


                startActivity(new Intent(MainActivity.this, MainDriverActivity.class));

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }

    private void showRegisterDialog() {

        Intent intent = new Intent(this, RegisterWelcomeActivity.class);

        startActivity(intent);
    }

}
