package com.taxi.friend.drivers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.taxi.friend.drivers.constants.Constants;
import com.taxi.friend.drivers.services.CreditService;

public class LoadQRCreditActivity extends AppCompatActivity {

    private String qrCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_qrcredit_activiy);

        TextView creditValue = (TextView)findViewById(R.id.creditValue);
        Button btnLoadCredit = (Button)findViewById(R.id.btnLoadCredit);

        Intent intent = getIntent();

        qrCredit = intent.getStringExtra(Constants.QR_CREDIT_VALUE);
        creditValue.setText(qrCredit);

        btnLoadCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCredit();
            }
        });
    }

    public void loadCredit() {
        CreditService creditService = new CreditService();
        creditService.loadCreditByQR(qrCredit);
        this.finish();
    }
}
