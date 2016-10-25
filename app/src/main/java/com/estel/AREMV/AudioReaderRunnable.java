package com.estel.AREMV;

import android.app.ProgressDialog;

import com.datecs.audioreader.AudioReader;

import java.io.IOException;

/**
 * Created by Nacho on 23/10/2016.
 */

public interface AudioReaderRunnable {
    void run(ProgressDialog progressDialog, AudioReader audioReader) throws IOException;
}
