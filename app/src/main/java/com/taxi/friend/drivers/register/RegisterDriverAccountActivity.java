package com.taxi.friend.drivers.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.CodeDeliveryDetailsType;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.taxi.friend.drivers.MainDriverActivity;
import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.auth.AppHelper;
import com.taxi.friend.drivers.utils.PhoneCode;


public class RegisterDriverAccountActivity extends AppCompatActivity {

    private Button btnNext;
    private ProgressBar progressBar;
    private String userName;
    private String names;
    private boolean registerDriverAccount = false;
    private String phone;
    private String carIdentity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver_account);

        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);

        btnNext.setOnClickListener(v -> showNextStep());
    }

    public void showNextStep() {

        MaterialEditText editNames = findViewById(R.id.editName);
        MaterialEditText editPhone = findViewById(R.id.editPhone);
        MaterialEditText editCarIdentity = findViewById(R.id.editCarIdentity);
        MaterialEditText editPassword = findViewById(R.id.editPassword);

        boolean validParameters = true;
        if(editNames.length() < 1){
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_LONG).show();
            validParameters = false;
        }

        if(editPhone.length() < 6 ){
            Toast.makeText(this, "Ingrese un numero de telefono valido", Toast.LENGTH_LONG).show();
            validParameters = false;
        }

        if(editCarIdentity.length() < 4){
            Toast.makeText(this, "Ingrese la placa de su auto", Toast.LENGTH_LONG).show();
            validParameters = false;
        }

        if(editPassword.length() < 6) {
            Toast.makeText(this, "Ingrese una contraseña mayor a 6 caracteres", Toast.LENGTH_LONG).show();
            validParameters = false;
        }

        if(validParameters){
            names = editNames.getText().toString();
            phone = editPhone.getText().toString();
            carIdentity = editCarIdentity.getText().toString();
            String password = editPassword.getText().toString();

            userName = PhoneCode.getNumber(phone);
            CognitoUserAttributes userAttributes = new CognitoUserAttributes();
            userAttributes.addAttribute("name", names);
            userAttributes.addAttribute("phone_number",  phone);
            userAttributes.addAttribute("custom:identify_car", carIdentity);

            if (!registerDriverAccount) {
                AppHelper.getPool().signUpInBackground(phone, password, userAttributes, null, signUpHandler);
            }


        }
    }

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            // Check signUpConfirmationState to see if the user is already confirmed
            //closeWaitDialog();
            Boolean regState = signUpResult.getUserConfirmed();
            if (regState) {
                // User is already confirmed
                //showDialogMessage("Sign up successful!",usernameInput+" has been Confirmed", true);
            }
            else {
                // User is not confirmed
                confirmSignUp(signUpResult.getCodeDeliveryDetails());
            }
            registerDriverAccount = true;
        }

        @Override
        public void onFailure(Exception exception) {
            //closeWaitDialog();
            //TextView label = (TextView) findViewById(R.id.textViewRegUserIdMessage);
            //label.setText("Sign up failed");
            //username.setBackground(getDrawable(R.drawable.text_border_error));
            showDialogMessage("Sign up failed", AppHelper.formatException(exception),false);
        }
    };

    private void confirmSignUp(CodeDeliveryDetailsType cognitoUserCodeDeliveryDetails) {
        Intent intent = new Intent(this, SignUpConfirm.class);
        intent.putExtra("source","signup");

        intent.putExtra("name", names);
        intent.putExtra("userName", userName);
        intent.putExtra("phone", phone);
        intent.putExtra("car_identity", carIdentity);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, 10);
    }

    private AlertDialog userDialog;
    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", (dialog, which) -> userDialog.dismiss());
        userDialog = builder.create();
        userDialog.show();
    }


    public void redirectDriverMainActivity(){
        Intent intent = new Intent(this, MainDriverActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
