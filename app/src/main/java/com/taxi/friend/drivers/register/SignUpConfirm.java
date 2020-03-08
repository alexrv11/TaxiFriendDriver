/*
 * Copyright 2013-2017 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

package com.taxi.friend.drivers.register;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.taxi.friend.drivers.MainDriverActivity;
import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.auth.AppHelper;
import com.taxi.friend.drivers.models.TaxiDriver;
import com.taxi.friend.drivers.services.AuthService;


public class SignUpConfirm extends AppCompatActivity {

    private Button confirm;
    private EditText reqCode;
    private String userName;
    private String name;
    private AlertDialog userDialog;
    private String phone;
    private String carIdentity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sign_up_confirm);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        reqCode = findViewById(R.id.txtCode);
        reqCode.setText("");
        confirm = findViewById(R.id.btnConfirm);
        confirm.setEnabled(false);
        userName  = getIntent().getStringExtra("userName");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        carIdentity = getIntent().getStringExtra("car_identity");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)
        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }

        TaxiGlobalInfo.codeViewModel = ViewModelProviders.of(this).get(ConfirmCodeViewModel.class);
        TaxiGlobalInfo.codeViewModel.getCode().observe(this, code -> reqCode.setText(code.getCode()));

        addRequestCodeTextEvents();
        confirm.setOnClickListener(t -> {
            String confirmCode = reqCode.getText().toString();
            AppHelper.getPool().getUser(userName).confirmSignUpInBackground(confirmCode, true, confHandler);
        });
    }

    public void addRequestCodeTextEvents() {
        reqCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 6) {
                    confirm.setEnabled(true);
                    confirm.setBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                    AppHelper.getPool().getUser(userName).confirmSignUpInBackground(s.toString(), true, confHandler);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Ya estamos terminando..", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Necesitamos permisos para terminar el registro", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    GenericHandler confHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            startApp();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(SignUpConfirm.this, "Codigo incorrecto", Toast.LENGTH_LONG).show();
        }
    };

    public void startApp() {
        Intent intent = new Intent(SignUpConfirm.this, MainDriverActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("USERNAME", userName);
        startActivity(intent);
        AuthService authService = new AuthService();
        authService.registerUserInformation(name, phone, carIdentity);
        //finishAffinity();
    }
}
