package com.taxi.friend.drivers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taxi.friend.drivers.constants.Constants;
import com.taxi.friend.drivers.models.Credit;
import com.taxi.friend.drivers.models.Qr;
import com.taxi.friend.drivers.models.ResponseWrapper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_qrcredit_activiy);
        creditValue = (TextView)findViewById(R.id.creditValue);
        Button btnLoadCredit = (Button)findViewById(R.id.btnLoadCredit);
        initProgress();
        Intent intent = getIntent();

        qrCredit = intent.getStringExtra(Constants.INSTANCE.getQR_CREDIT_VALUE()).replaceAll(Constants.INSTANCE.getFRIEND_QR(), "");


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


        Call<ResponseWrapper<Qr>> callQr = creditService.getQrDetails(this.qrCredit);
        callQr.enqueue(new Callback<ResponseWrapper<Qr>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Qr>> call, Response<ResponseWrapper<Qr>> response) {
                if(response.code() == HttpsURLConnection.HTTP_OK){
                    Qr qr = response.body().getResult();
                    creditValue.setText(qr.getCredit() + "");

                    progressContainer.setVisibility(View.GONE);
                    loadContainer.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(LoadQRCreditActivity.this,
                            "Hubo problemas al leer el codigo qr, intente con otro qr", Toast.LENGTH_LONG).show();

                    finalizeActivity();
                }

            }

            @Override
            public void onFailure(Call<ResponseWrapper<Qr>> call, Throwable t) {
                Log.e("ErrorQr", t.getMessage());
                Toast.makeText(LoadQRCreditActivity.this,
                        "Hubo problemas al cargar, vuelva a intentarlo mas tarde", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadCredit() {
        CreditService creditService = new CreditService();

        Call<ResponseWrapper<Credit>> callLoad = creditService.loadCreditByQR(TaxiGlobalInfo.DriverId, qrCredit);
        callLoad.enqueue(new Callback<ResponseWrapper<Credit>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Credit>> call, Response<ResponseWrapper<Credit>> response) {

                if(response.code() == HttpURLConnection.HTTP_OK){
                    Credit credit = response.body().getResult();
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
            public void onFailure(Call<ResponseWrapper<Credit>> call, Throwable t) {
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
