package com.estel.AREMV;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivationActivity extends Activity {
    ImageButton Activate;
    ImageButton Contact;
    ImageButton Exit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.Activate = (ImageButton) findViewById(R.id.ImgActivateAcc);
        this.Contact = (ImageButton) findViewById(R.id.imgForgotMPIN);
        this.Exit = (ImageButton) findViewById(R.id.imgRefundBtn);
    }

    public void OnClick(View v) {
        System.out.println("From ActivationActivity");
        switch (v.getId()) {
            case R.id.ImgActivateAcc:
                System.out.println("From ActivationActivity");
                Intent in = new Intent(this, MainActivity.class);
                System.out.println("1.From ActivationActivity");
                startActivity(in);
                System.out.println("2.From ActivationActivity");
                finish();
            case R.id.imgRefundBtn:
                finish();
            default:
        }
    }

    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
    }
}
