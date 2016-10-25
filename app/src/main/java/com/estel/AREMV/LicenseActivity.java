package com.estel.AREMV;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LicenseActivity extends Activity implements OnClickListener {
    public static final String PREFFS_NAME = "PrefsStatus";
    Button Agree;
    Button Cancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.Agree = (Button) findViewById(R.id.BtnAgree);
        this.Cancel = (Button) findViewById(R.id.BtnCancel);
        this.Agree.setOnClickListener(this);
        this.Cancel.setOnClickListener(this);
    }

    public void onClick(View v) {
        Editor editor = getSharedPreferences(PREFFS_NAME, 0).edit();
        switch (v.getId()) {
            case R.id.BtnCancel:
                editor.putBoolean("AgreementDone", false);
                editor.commit();
                finish();
            case R.id.BtnAgree:
                editor.putBoolean("AgreementDone", true);
                editor.commit();
                startActivity(new Intent(this, ActivationActivity.class));
                finish();
            default:
        }
    }
}
