package com.estel.AREMV;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.estel.AREMVMAKIN.R;

public class SwipeCardActivity extends Activity implements OnClickListener {
    static ModelData md;
    Button buttonNext;

    static {
        md = new ModelData();
    }

    public static ModelData getMd() {
        return md;
    }

    public static void setMd(ModelData md) {
        md = md;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);
        System.out.println("Finish SwipeCardActivity Activity");
        setTitle("Menu");
        startActivity(new Intent(this, MobileReaderMenu.class));
        System.out.println("111 Finish MobileReaderMenu Activity");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.swipe_card, menu);
        return true;
    }

    public void onClick(View v) {
        startActivity(new Intent(this, GetDataActivity.class));
    }
}
