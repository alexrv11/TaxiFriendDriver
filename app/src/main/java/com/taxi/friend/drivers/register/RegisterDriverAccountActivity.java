package com.taxi.friend.drivers.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.taxi.friend.drivers.MainDriverActivity;
import com.taxi.friend.drivers.R;
import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.models.DriverInfo;
import com.taxi.friend.drivers.models.DriverLocation;
import com.taxi.friend.drivers.models.ResponseWrapper;
import com.taxi.friend.drivers.models.TaxiDriver;
import com.taxi.friend.drivers.services.DriverService;
import com.taxi.friend.drivers.utils.ImageHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDriverAccountActivity extends AppCompatActivity {

    private Button btnNext;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver_account);

        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextStep();
            }
        });
    }

    public void showNextStep() {

        MaterialEditText editNames = findViewById(R.id.editName);
        MaterialEditText editPhone = findViewById(R.id.editPhone);
        MaterialEditText editCarIdentity = findViewById(R.id.editCarIdentity);
        MaterialEditText editPassword = findViewById(R.id.editCarIdentity);

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
            Toast.makeText(this, "Ingrese la placa de su automivil valido", Toast.LENGTH_LONG).show();
            validParameters = false;
        }

        if(editPassword.length() < 6) {
            Toast.makeText(this, "Ingrese una contraseña mayor a 6 caracteres", Toast.LENGTH_LONG).show();
            validParameters = false;
        }

        if(validParameters){
            String names = editNames.getText().toString();
            String phone = editPhone.getText().toString();
            String carIdentity = editCarIdentity.getText().toString();
            String password = editPassword.getText().toString();
            String carFrontImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.CAR_FRONT_PHOTO));
            String carBackImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.CAR_BACK_PHOTO));
            String carSideImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.CAR_SIDE_PHOTO));
            String licenseFrontImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.LICENSE_FRONT_PHOTO));
            String licenseBackImage = ImageHelper.base64(TaxiGlobalInfo.photoBitmap.get(TaxiGlobalInfo.LICENSE_BACK_PHOTO));

            TaxiDriver driver = new TaxiDriver(names, carFrontImage, carBackImage, carSideImage, licenseFrontImage, licenseBackImage,
                    phone, carIdentity, password);

            DriverService service = new DriverService();
            Call<DriverInfo> callRequest = service.createDriver(driver);

            try {

                callRequest.enqueue(new Callback<DriverInfo>() {
                    @Override
                    public void onResponse(Call<DriverInfo> call, Response<DriverInfo> response) {
                        int code = response.code();
                        if (code != 200) {
                            String message = "intenta de nuevo crear tu cuenta";
                            Toast.makeText(RegisterDriverAccountActivity.this, message, Toast.LENGTH_LONG).show();
                            return;
                        }

                        TaxiGlobalInfo.taxiDriver = response.body();
                        redirectDriverMainActivity();
                    }

                    @Override
                    public void onFailure(Call<DriverInfo> call, Throwable t) {
                        Log.e("ErrorServer", t.getMessage());
                    }
                });
            }catch (Exception e ){
                Log.e("ErrorCreateDriver", e.getMessage());
                e.printStackTrace();
            }
        }
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
