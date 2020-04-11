package com.taxi.friend.drivers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.taxi.friend.drivers.auth.AppHelper;
import com.taxi.friend.drivers.register.RegisterWelcomeActivity;
import com.taxi.friend.drivers.utils.PhoneCode;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rootLayout;

    TextView txtTitle;
    TextView txtTitle2;
    TextView txtBanner;

    Button btnSignIn;
    Button btnSignUp;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        rootLayout = findViewById(R.id.rootLayout);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtTitle =  findViewById(R.id.title_app_primary);

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

        AppHelper.init(getApplicationContext());
    }

    private void showLoginDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View loginLayout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText editEMail = loginLayout.findViewById(R.id.editMail);

        final MaterialEditText editPassword = loginLayout.findViewById(R.id.editPassword);


        if (TextUtils.isEmpty(editEMail.getText().toString())) {
            Snackbar.make(rootLayout, "Por favor ingresa tu numero de telefono", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(editPassword.getText().toString())) {
            Snackbar.make(rootLayout, "Por favor ingresa tu contraseña", Snackbar.LENGTH_SHORT).show();
            return;
        }

        username = editEMail.getText().toString();
        password = editPassword.getText().toString();
        /*TaxiGlobalInfo.driverInfo = new TaxiDriver();
        TaxiGlobalInfo.driverInfo.setName("Test Name");
        TaxiGlobalInfo.driverInfo.setCredit(0);
        TaxiGlobalInfo.driverInfo.setId("6");
        */

        username =  PhoneCode.getNumber(username);

        signInUser();
    }

    private void signInUser() {

        AppHelper.setUser(username);
        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
    }

    private void showRegisterDialog() {

        Intent intent = new Intent(this, RegisterWelcomeActivity.class);

        startActivity(intent);
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.d("AuthSession", " -- Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.newDevice(device);
            Intent intent = new Intent(LoginActivity.this, MainDriverActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

            LoginActivity.this.finish();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {

            getUserAuthentication(authenticationContinuation);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {

        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(LoginActivity.this, "Algo salio mal, intenta nuevamente", Toast.LENGTH_LONG).show();
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }
    };

    private void getUserAuthentication(AuthenticationContinuation continuation) {

        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

}
