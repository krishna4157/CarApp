package com.vasilkoff.easyvpnfree;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
//            Intent serviceIntent = new Intent(context, BootService.class);
//            Intent serviceIntent2 = new Intent(context, FloatingWindowService.class);
//
////            context.startService(serviceIntent);
//            context.startService(serviceIntent2);
            new Handler().postDelayed(() -> {
                Intent launchIntent = new Intent(context, SpeechToTextActivityForVPN.class);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(launchIntent);
            }, 2000);

        }
    }
}