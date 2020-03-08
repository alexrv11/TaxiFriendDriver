package com.taxi.friend.drivers.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.taxi.friend.drivers.TaxiGlobalInfo;
import com.taxi.friend.drivers.register.ConfirmCode;
import com.taxi.friend.drivers.register.ConfirmCodeViewModel;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class MySMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            SmsMessage[] smgs = null;
            String msgFrom = "";

            if (extras != null) {
                try {
                    Object[] pdus = (Object[]) extras.get("pdus");
                    smgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        smgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        //msgFrom = smgs[i].getOriginatingAddress();
                        String msgBody = smgs[i].getMessageBody();
                        String code = "";
                        if (msgBody.contains("Taxifriend tu codigo es ")){
                            code = msgBody.replace("Taxifriend tu codigo es ", "").trim();
                            code = code.replace(".", "").trim();
                            if (!code.isEmpty()) {
                                TaxiGlobalInfo.codeViewModel.setCode(new ConfirmCode(code));
                            }

                        }
                        Toast.makeText(context, ""+ code, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
}