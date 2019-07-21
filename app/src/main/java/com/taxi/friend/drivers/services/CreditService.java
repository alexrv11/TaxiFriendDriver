package com.taxi.friend.drivers.services;


import com.taxi.friend.drivers.models.Credit;
import com.taxi.friend.drivers.models.LoadCredit;
import com.taxi.friend.drivers.models.Qr;
import com.taxi.friend.drivers.models.ResponseWrapper;
import com.taxi.friend.drivers.repository.QrRepository;
import com.taxi.friend.drivers.repository.RestClient;

import retrofit2.Call;

public class CreditService {

    public Call<ResponseWrapper<Credit>> loadCreditByQR(String driverId, String qrValue) {

        QrRepository service = RestClient.createRestClient().create(QrRepository.class);

        return service.loadCreditQr(new LoadCredit(driverId, qrValue));

    }

    public Call<ResponseWrapper<Qr>> getQrDetails(String qrCode) {
        QrRepository service = RestClient.createRestClient().create(QrRepository.class);

        return service.getQrDetails(qrCode);
    }
}
