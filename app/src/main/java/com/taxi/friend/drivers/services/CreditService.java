package com.taxi.friend.drivers.services;


import com.taxi.friend.drivers.models.Credit;
import com.taxi.friend.drivers.models.LoadCredit;
import com.taxi.friend.drivers.models.Qr;
import com.taxi.friend.drivers.repository.QrRepository;
import com.taxi.friend.drivers.repository.RestClient;

import retrofit2.Call;

public class CreditService {

    public Call<Credit> loadCreditByQR(String driverId, String qrValue) {

        QrRepository service = RestClient.createRestClient().create(QrRepository.class);

        return service.loadCreditQr(qrValue, new LoadCredit(driverId));

    }

    public Call<Qr> getQrDetails(String qrCode) {
        QrRepository service = RestClient.createRestClient().create(QrRepository.class);

        return service.getQrDetails(qrCode);
    }
}
