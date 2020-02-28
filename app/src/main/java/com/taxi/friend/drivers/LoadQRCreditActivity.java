package com.taxi.friend.drivers;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.taxi.friend.drivers.constants.Constants;
import com.taxi.friend.drivers.models.Credit;
import com.taxi.friend.drivers.models.Qr;

import com.taxi.friend.drivers.models.User;
import com.taxi.friend.drivers.services.CreditService;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadQRCreditActivity extends AppCompatActivity {

    private String qrCredit;
    private ProgressBar progressBar;
    private LinearLayout loadContainer;
    private LinearLayout progressContainer;
    TextView creditValue;
    TextView currencyType;
    Button btnLoadCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_qrcredit_activiy);
        creditValue = findViewById(R.id.creditValue);
        currencyType = findViewById(R.id.currencyType);
        btnLoadCredit = findViewById(R.id.btnLoadCredit);
        initProgress();
        Intent intent = getIntent();

        qrCredit = intent.getStringExtra(Constants.QR_CREDIT_VALUE).replaceAll(Constants.FRIEND_QR, "");


        btnLoadCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCredit();
            }
        });
    }

    private void initProgress() {

        loadContainer = findViewById(R.id.loadContainer);
        loadContainer.setVisibility(View.INVISIBLE);
        progressContainer = findViewById(R.id.progressContainer);
        progressContainer.setVisibility(View.VISIBLE);
        this.progressBar = findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCreditValue();
            }
        }, 5000);
    }

    public void getCreditValue() {

        CreditService creditService = new CreditService();


        Call<Qr> callQr = creditService.getQrDetails(this.qrCredit);
        callQr.enqueue(new Callback<Qr>() {
            @Override
            public void onResponse(Call<Qr> call, Response<Qr> response) {
                if(response.code() == HttpsURLConnection.HTTP_OK){
                    Qr qr = response.body();

                    if(!qr.getDriverId().equals(Constants.DRIVER_NONE)) {
                        Toast.makeText(LoadQRCreditActivity.this,
                                "Este Qr ya fue usado prueba con otro", Toast.LENGTH_LONG).show();
                        btnLoadCredit.setVisibility(View.INVISIBLE);
                        finalizeActivity();

                    } else {
                        currencyType.setText("Bs");
                        creditValue.setText(qr.getCredit() + "");
                    }
                    progressContainer.setVisibility(View.GONE);
                    loadContainer.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(LoadQRCreditActivity.this,
                            "Hubo problemas al leer el codigo qr, intente con otro qr", Toast.LENGTH_LONG).show();

                    finalizeActivity();
                }

            }

            @Override
            public void onFailure(Call<Qr> call, Throwable t) {
                Log.e("ErrorQr", t.getMessage());
                Toast.makeText(LoadQRCreditActivity.this,
                        "Hubo problemas al cargar, vuelva a intentarlo mas tarde", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadCredit() {
        CreditService creditService = new CreditService();

        Call<Credit> callLoad = creditService.loadCreditByQR(TaxiGlobalInfo.DriverId, qrCredit);
        callLoad.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {

                if(response.code() == HttpURLConnection.HTTP_OK){
                    Credit credit = response.body();
                    User user = TaxiGlobalInfo.mainViewModel.getUser().getValue();
                    double total = user.getCredit() + credit.getCredit();
                    user.setCredit(total);
                    TaxiGlobalInfo.mainViewModel.getUser().setValue(user);

                    Toast.makeText(LoadQRCreditActivity.this, "La carga fue exitosa", Toast.LENGTH_LONG).show();
                } else if( response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    Toast.makeText(LoadQRCreditActivity.this, "El codigo no es valido", Toast.LENGTH_LONG).show();
                }

                finalizeActivity();
            }

            @Override
            public void onFailure(Call<Credit> call, Throwable t) {
                Log.e("ErrorQr", t.getMessage());
                Toast.makeText(LoadQRCreditActivity.this,
                        "Hubo problemas al cargar, vuelva a intentarlo mas tarde", Toast.LENGTH_LONG).show();

                finalizeActivity();
            }
        });

    }

    private void finalizeActivity(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadQRCreditActivity.this.finish();
            }
        }, 3000);
    }
}
