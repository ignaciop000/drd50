package com.datecs.audioreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
//import com.sun.mail.imap.IMAPStore;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bouncycastle.asn1.x509.ReasonFlags;

public class AudioReaderPlayer {
    private static final int AUDIO_FORMAT = 3;
    private static final int CHANNEL_CONFIG = 12;
    private static boolean D = false;
    private static boolean KEEP_ALIVE = false;
    private static final String LOG_TAG = "AudioReaderPlayer";
    private static final int SAMPLE_RATE = 44100;
    public static final int WF_RECTANGLE = 0;
    public static final int WF_SINE = 2;
    public static final int WF_TBD = 3;
    public static final int WF_ТRAPEZIUM = 1;
    private volatile boolean mActive;
    private AudioPlayerThread mAudioPlayer;
    private byte[][] mBitPatterns;
    private IOException mLastError;
    private int mOutputBufferSize;
    private int mSampleRate;
    private List<byte[]> mTrackQueue;

    private class AudioPlayerThread extends Thread {
        private AudioPlayerThread() {
        }

        public void run() {
            System.out.println("Audio player thread is started");
            Process.setThreadPriority(-19);
            byte[] keepAliveData = new byte[AudioReaderPlayer.WF_SINE];
            keepAliveData[AudioReaderPlayer.WF_RECTANGLE] = AudioReaderPlayer.convertToUnsigned((byte) 0);
            keepAliveData[AudioReaderPlayer.WF_ТRAPEZIUM] = AudioReaderPlayer.convertToUnsigned((byte) 0);
            while (AudioReaderPlayer.this.mActive) {
                try {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    if (AudioReaderPlayer.this.mActive) {
                        if (!AudioReaderPlayer.this.mTrackQueue.isEmpty()) {
                            System.out.println("Play track data");
                            playData(AudioReaderPlayer.this.mTrackQueue.get(0));
                            AudioReaderPlayer.this.mTrackQueue.remove(0);
                        } else if (AudioReaderPlayer.KEEP_ALIVE) {

                            System.out.println("Keep alive");
                            playData(keepAliveData);
                        } else if (AudioReaderPlayer.D) {
                            System.out.println("Suspended");
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println("Play audio data failed:" + e2.getMessage());
                    AudioReaderPlayer.this.mLastError = new IOException(e2.getMessage());
                }
            }
            System.out.println("Audio player thread is stopped");
        }

        private void playData(byte[] data) {
            AudioTrack track = null;
            try {
                int totalSamples = data.length / 2;
                int bufferSize = AudioTrack.getMinBufferSize(AudioReaderPlayer.this.mSampleRate, AudioReaderPlayer.CHANNEL_CONFIG, AudioReaderPlayer.WF_TBD);
                if (bufferSize < data.length) {
                    bufferSize = data.length;
                }
                if (bufferSize % AudioReaderPlayer.this.mOutputBufferSize != 0) {
                    bufferSize += AudioReaderPlayer.this.mOutputBufferSize - (bufferSize % AudioReaderPlayer.this.mOutputBufferSize);
                }
                System.out.println("Create audio track: sampleRate=" + AudioReaderPlayer.this.mSampleRate + "Hz, bufferSize=" + bufferSize + " bytes");
                track = new AudioTrack(AudioManager.STREAM_MUSIC, AudioReaderPlayer.this.mSampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_8BIT, bufferSize, AudioTrack.MODE_STATIC);

                float volume = AudioTrack.getMaxVolume();
                track.setVolume(volume);
                int count = 0;
                while (count < data.length) {
                    count += track.write(data, count, data.length - count);
                }
                if (count > 0) {
                    System.out.println("Play " + totalSamples + " samples");
                    track.play();
                    while (track.getPlaybackHeadPosition() < totalSamples) {
                        SystemClock.sleep(10);
                    }
                }
                if (track != null) {
                    System.out.println("Release audio track");
                    track.release();
                }
                SystemClock.sleep(50);
            } catch (Throwable th3) {
                if (track != null) {
                    System.out.println("Release audio track");
                    track.release();
                }
                th3.printStackTrace();
            }
        }
    }

//    static {
//        D = false;
//        KEEP_ALIVE = true;
//    }

    @SuppressLint({"NewApi"})
    public AudioReaderPlayer(Context context, int baudRate, int waveform) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.mSampleRate = SAMPLE_RATE;
        if (VERSION.SDK_INT >= 17) {
            String nativeSampleRate = manager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE");
            if (nativeSampleRate != null) {
                try {
                    this.mSampleRate = Integer.valueOf(nativeSampleRate).intValue();
                } catch (Exception e) {
                }
            }
        }
        this.mOutputBufferSize = AudioTrack.getMinBufferSize(this.mSampleRate, CHANNEL_CONFIG, WF_TBD);
        if (VERSION.SDK_INT >= 17) {
            String nativeBufferSize = manager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER");
            if (nativeBufferSize != null) {
                try {
                    this.mOutputBufferSize = Integer.valueOf(nativeBufferSize).intValue();
                } catch (Exception e2) {
                }
            }
        }
        int bitSize = this.mSampleRate / baudRate;
        this.mBitPatterns = new byte[WF_SINE][];
        this.mBitPatterns[WF_RECTANGLE] = new byte[bitSize];
        getPulseData(waveform, this.mBitPatterns[WF_RECTANGLE]);
        this.mBitPatterns[WF_ТRAPEZIUM] = new byte[(bitSize / WF_SINE)];
        getPulseData(waveform, this.mBitPatterns[WF_ТRAPEZIUM]);
        this.mTrackQueue = Collections.synchronizedList(new LinkedList());
    }

    private static final void getPulseData(int waveform, byte[] pulse) {
        int samples = pulse.length;
        int i;
        switch (waveform) {
            case WF_RECTANGLE /*0*/:
                for (i = WF_RECTANGLE; i < samples; i += WF_ТRAPEZIUM) {
                    pulse[i] = Byte.MAX_VALUE;
                }
                return;
            case WF_ТRAPEZIUM /*1*/:
                for (i = WF_ТRAPEZIUM; i < samples; i += WF_ТRAPEZIUM) {
                    pulse[i] = Byte.MAX_VALUE;
                }
                pulse[WF_RECTANGLE] = (byte) 63;
                pulse[samples - 1] = (byte) 63;
                return;
            case WF_SINE /*2*/:
                for (i = WF_RECTANGLE; i < samples; i += WF_ТRAPEZIUM) {
                    pulse[i] = (byte) ((int) (Math.sin((3.141592653589793d * ((double) i)) / ((double) samples)) * 127.0d));
                }
                return;
            case WF_TBD /*3*/:
                for (i = WF_ТRAPEZIUM; i < samples; i += WF_ТRAPEZIUM) {
                    pulse[i] = Byte.MAX_VALUE;
                }
                pulse[WF_RECTANGLE] = (byte) 63;
                pulse[samples - 1] = (byte) 63;
                return;
            default:
                throw new IllegalArgumentException("Invalid waveform " + waveform);
        }
    }

    private static final boolean[] convertBytesToBits(byte[] bytes, int offset, int length) {
        boolean[] result = new boolean[(length * 8)];
        int index = WF_RECTANGLE;
        int i = WF_RECTANGLE;
        while (i < length) {
            int j = WF_RECTANGLE;
            int index2 = index;
            while (j < 8) {
                index = index2 + WF_ТRAPEZIUM;
                result[index2] = (bytes[offset + i] & (ReasonFlags.unused >> j)) != 0;
                j += WF_ТRAPEZIUM;
                index2 = index;
            }
            i += WF_ТRAPEZIUM;
            index = index2;
        }
        return result;
    }

    private static final byte convertToUnsigned(byte signedValue) {
        return (byte) (signedValue + ReasonFlags.unused);
    }

    private static final byte[] encodeManchester(byte[] bitPattern0, byte[] bitPattern1, boolean[] bits) {
        int n;
        int i;
        byte[] result = new byte[((bitPattern0.length * WF_SINE) * ((bits.length + 24) + 24))];
        int offset = WF_RECTANGLE;
        boolean invert = false;
        for (n = WF_RECTANGLE; n < 24; n += WF_ТRAPEZIUM) {
            for (i = WF_RECTANGLE; i < bitPattern0.length; i += WF_ТRAPEZIUM) {
                int i2 = offset + WF_ТRAPEZIUM;
                result[offset] = convertToUnsigned((byte) 0);
                offset = i2 + WF_ТRAPEZIUM;
                result[i2] = convertToUnsigned((byte) 0);
            }
            if (invert) {
                invert = false;
            } else {
                invert = true;
            }
        }
        int length = bits.length;
        for (int i3 = WF_RECTANGLE; i3 < length; i3 += WF_ТRAPEZIUM) {
            byte value;
            if (Boolean.valueOf(bits[i3]).booleanValue()) {
                for (n = WF_RECTANGLE; n < WF_SINE; n += WF_ТRAPEZIUM) {
                    i = WF_RECTANGLE;
                    int i2;
                    while (i < bitPattern1.length) {
                        value = convertToUnsigned(invert ? (byte) (-bitPattern1[i]) : bitPattern1[i]);
                        i2 = offset + WF_ТRAPEZIUM;
                        result[offset] = value;
                        offset = i2 + WF_ТRAPEZIUM;
                        result[i2] = value;
                        i += WF_ТRAPEZIUM;
                    }
                    if (invert) {
                        invert = false;
                    } else {
                        invert = true;
                    }
                }
            } else {
                int i2;
                i = WF_RECTANGLE;
                while (i < bitPattern0.length) {
                    value = convertToUnsigned(invert ? (byte) (-bitPattern0[i]) : bitPattern0[i]);
                    i2 = offset + WF_ТRAPEZIUM;
                    result[offset] = value;
                    offset = i2 + WF_ТRAPEZIUM;
                    result[i2] = value;
                    i += WF_ТRAPEZIUM;
                }
                invert = !invert;
            }
        }
        for (n = WF_RECTANGLE; n < 24; n += WF_ТRAPEZIUM) {
            int i2;
            for (i = WF_RECTANGLE; i < bitPattern0.length; i += WF_ТRAPEZIUM) {
                i2 = offset + WF_ТRAPEZIUM;
                result[offset] = convertToUnsigned((byte) 0);
                offset = i2 + WF_ТRAPEZIUM;
                result[i2] = convertToUnsigned((byte) 0);
            }
            if (invert) {
                invert = false;
            } else {
                invert = true;
            }
        }
        return result;
    }

    private static final String byteArrayToHexString(byte[] data, int offset, int length) {
        char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] buf = new char[(length * WF_SINE)];
        int offs = WF_RECTANGLE;
        for (int i = WF_RECTANGLE; i < length; i += WF_ТRAPEZIUM) {
            int i2 = offs + WF_ТRAPEZIUM;
            buf[offs] = hex[(data[offset + i] >> 4) & 15];
            offs = i2 + WF_ТRAPEZIUM;
            buf[i2] = hex[(data[offset + i] >> WF_RECTANGLE) & 15];
        }
        return new String(buf, WF_RECTANGLE, offs);
    }

    public static void setDebug(boolean on) {
        D = on;
    }

    public static void setKeepAlive(boolean on) {
        KEEP_ALIVE = on;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public synchronized void start() {
        this.mActive = true;
        this.mLastError = null;
        this.mAudioPlayer = new AudioPlayerThread();
        this.mAudioPlayer.start();
    }

    public synchronized void stop() {
        try {
            this.mActive = false;
            this.mTrackQueue.clear();
            this.mAudioPlayer.interrupt();
        } catch (Exception e) {
        }
        SystemClock.sleep(250);
    }

    private void checkReady() throws IOException {
        if (!this.mActive) {
            throw new IOException("The player is stopped");
        } else if (this.mLastError != null) {
            throw this.mLastError;
        }
    }

    private synchronized void playPCM(byte[] pcm) throws IOException {
        checkReady();
        this.mTrackQueue.add(pcm);
        this.mAudioPlayer.interrupt();
    }

    private byte[] encodeBytes(byte[] data, int offset, int length) {
        return encodeManchester(this.mBitPatterns[WF_RECTANGLE], this.mBitPatterns[WF_ТRAPEZIUM], convertBytesToBits(data, offset, length));
    }

    public synchronized void playBytes(byte[] bytes) throws IOException {
        if (D) {
            Log.d(LOG_TAG, "Play bytes (" + bytes.length + "): " + byteArrayToHexString(bytes, WF_RECTANGLE, bytes.length));
        }
        playPCM(encodeBytes(bytes, WF_RECTANGLE, bytes.length));
    }

    private byte[] encodeTone(int frequency, int duration, int volume) {
        int pulseLength = 0;
        if (frequency != 0) {
            pulseLength = getSampleRate() / frequency;
        }
        int totalLength = (getSampleRate() * duration) / 1000;
        int volumeLevel = (volume * 127) / 100;
        byte[] result = new byte[(totalLength * 2)];
        int offset = 0;
        for (int i = 0; i < totalLength; i++) {
            byte value = 0;
            if (pulseLength != 0) {
                value = convertToUnsigned((byte) ((int) (Math.sin((Math.PI * ((double) i)) / ((double) pulseLength)) * ((double) volumeLevel))));
            }
            int offset2 = offset + 1;
            result[offset] = value;
            offset = offset2 + 1;
            result[offset2] = value;
        }
        return result;
    }

//    public synchronized void playTone(byte[] input) throws IOException {
//        byte[] one = encodeTone(8000, 25, 100);
//        byte[] zero = encodeTone(8000, 25, 0);
//        byte[] buffer = new byte[one.length * input.length];
//        for (int i = 0;i< input.length;i++) {
//            if (input[i] == 0) {
//                System.arraycopy(zero, 0, buffer, i * zero.length, zero.length);
//            } else {
//                System.arraycopy(one, 0, buffer, i * one.length, one.length);
//            }
//        }
//        playPCM(buffer);
//    }

    public synchronized void playTone(int frequency, int duration, int volume) throws IOException {
        System.out.println("Play tone: " + frequency + "Hz, " + duration + "ms, " + volume + "%)");
        playPCM(encodeTone(frequency, duration, volume));
    }

    public synchronized void flush() throws java.io.IOException {
        while (!mTrackQueue.isEmpty()) {
            checkReady();
            SystemClock.sleep(10);
        }
    }
}
