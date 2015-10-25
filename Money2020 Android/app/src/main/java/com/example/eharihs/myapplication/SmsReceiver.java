package com.example.eharihs.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            Object[] pdus = (Object[])bundle.get("pdus");
            SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);
            Log.d("asd",sms.getMessageBody());
            Intent i = new Intent();
            i.setClassName("com.example.eharihs.myapplication", "com.example.eharihs.myapplication.SpeakActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setType(sms.getDisplayMessageBody());
            context.startActivity(i);
        }
    }
}