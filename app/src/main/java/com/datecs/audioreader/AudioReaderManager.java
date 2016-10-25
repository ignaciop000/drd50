package com.datecs.audioreader;

import android.content.Context;
import android.media.AudioManager;

import java.io.IOException;

public class AudioReaderManager {
    public static final AudioReader getReader(Context context) throws IOException {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(3, manager.getStreamMaxVolume(3), 0);
        manager.setMicrophoneMute(false);
        manager.setMode(0);
        return new AudioReader(context);
    }
}
