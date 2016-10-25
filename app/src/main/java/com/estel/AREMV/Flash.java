package com.estel.AREMV;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Flash extends Activity {
    public static final String PREFFS_NAME = "PrefsStatus";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash);
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences settings = Flash.this.getSharedPreferences(Flash.PREFFS_NAME, 0);
                    boolean Agree = settings.getBoolean("AgreementDone", false);
                    boolean Activated = settings.getBoolean("AccSetupDone", false);
                    boolean ChnMpin = settings.getBoolean("ChnMpinDone", false);
                    if (!Agree) {
                        System.out.println("Loading LicenseActivity");
                        Flash.this.startActivity(new Intent(Flash.this, LicenseActivity.class));
                        Flash.this.finish();
                    } else if (!Activated) {
                        System.out.println("Loading ActivationActivity");
                        Flash.this.startActivity(new Intent(Flash.this, ActivationActivity.class));
                        Flash.this.finish();
                    } else if (ChnMpin) {
//                        System.out.println("Loading ActivityLogin");
//                        Intent in = new Intent(Flash.this, ActivityLogin.class);
//                        in.putExtra("TxnType", "LOGIN");
//                        Flash.this.startActivity(in);
//                        Flash.this.finish();
                    } else {
//                        System.out.println("Loading ChangeMPinsActivity");
//                        Intent in = new Intent(Flash.this, ChangeMPinsActivity.class);
//                        in.putExtra("TxnType", "CHPIN");
//                        Flash.this.startActivity(in);
//                        Flash.this.finish();
                    }
                    Flash.this.finish();
                }
            }, 3500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
