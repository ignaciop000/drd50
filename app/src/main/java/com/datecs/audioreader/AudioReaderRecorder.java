package com.datecs.audioreader;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
//import com.sun.mail.imap.IMAPStore;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.x509.ReasonFlags;

public class AudioReaderRecorder {
    private static final int AUDIO_FORMAT = 2;
    private static final int AUTO_THRES = 25;
    private static final int BUF_SIZE = 65536;
    private static final int CHANNEL_CONFIG = 16;
    private static boolean D = true;
    private static final int END_LENGTH = 441;
    private static final int FREQ_THRES = 60;
    private static String LOG_FILE = null;
    private static final String LOG_TAG = "AudioReaderRecorder";
    private static final int ON_RECEIVEING_LENGTH = 4410;
    private static final int SAMPLE_RATE = 44100;
    private static final int SILENCE_THRES = 500;
    private short[] mBuffer;
    private int mBufferLength;
    private int mCalibrateLength;
    private int mEndLength;
    private RecorderListener mListener;
    private List<Short> mLog;
    private int mPauseLength;
    private int[] mPeaks;
    private int mPeaksLength;
    private MyRecorderThread mRecorder;
    private int mSampleRate;
    private int mSilenceThres;

    private class MyRecorderThread extends Thread {
        private volatile boolean mRecording;

        public MyRecorderThread() {
            this.mRecording = true;
        }

        public void run() {
            Exception e;
            Throwable th;
            short[] buffer = new short[1024];
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
            AudioRecord audioRecord = null;
            try {
                int minBufSize = AudioRecord.getMinBufferSize(AudioReaderRecorder.this.mSampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 4;
                System.out.println("Obtain buffer size: " + minBufSize);
                if (minBufSize < 0) {
                    throw new RuntimeException("Illegal sample rate " + AudioReaderRecorder.this.mSampleRate);
                }
                audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, AudioReaderRecorder.this.mSampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufSize);
                long upTo = System.currentTimeMillis() + 1000;
                while (audioRecord.getState() != 1 && upTo < System.currentTimeMillis()) {
                    SystemClock.sleep(100);
                }
                if (audioRecord.getState() != 1) {
                    throw new RuntimeException("AudioRecord is not initialized");
                }
                audioRecord.startRecording();
                audioRecord.setPositionNotificationPeriod(160);
                int skip = AudioReaderRecorder.this.mSampleRate / 20;
                while (this.mRecording) {
                    int length = audioRecord.read(buffer, 0, buffer.length);
                    if (length <= 0) {
                        throw new RuntimeException("Illegal read operation: " + length);
                    } else if (skip > 0) {
                        skip -= length;
                    } else {
                        AudioReaderRecorder.this.update(buffer, length);
                    }
                }
                audioRecord.stop();
                audioRecord.release();
                System.out.println("Stop recorder");
            } catch (Exception e3) {
                AudioReaderRecorder.logE("Error: ", e3);
                audioRecord.release();
                System.out.println("Stop recorder");
            }
        }

        public void stopRecorder() {
            this.mRecording = false;
        }

        public boolean isActive() {
            return this.mRecording;
        }
    }

    public interface RecorderListener {
        void OnPacketReceived(int[] iArr, int i);

        void OnReceiving(int i);
    }

    static {
        D = false;
        LOG_FILE = null;
    }

    public AudioReaderRecorder(RecorderListener listener) {
        this(SAMPLE_RATE, listener);
    }

    public AudioReaderRecorder(int sampleRate, RecorderListener listener) {
        System.out.println("Create audio reader recorder");
        this.mSampleRate = sampleRate;
        this.mListener = listener;
        this.mEndLength = END_LENGTH;
        this.mSilenceThres = SILENCE_THRES;
        this.mCalibrateLength = 0;
        this.mBuffer = new short[BUF_SIZE];
        this.mPeaks = new int[BUF_SIZE];
        reset();
    }

    public static void setDebug(boolean on) {
        D = on;
    }

    public static void setLogFile(String file) {
        LOG_FILE = file;
    }

    protected static void logD(String text) {
        if (D) {
            Log.d(LOG_TAG, text);
        }
    }

    protected static void logI(String text) {
        if (D) {
            Log.i(LOG_TAG, text);
        }
    }

    protected static void logW(String text) {
        if (D) {
            Log.w(LOG_TAG, text);
        }
    }

    protected static void logE(String text, Throwable e) {
        Log.e(LOG_TAG, text, e);
    }

    public void setSampleRate(int sampleRate) {
        this.mSampleRate = sampleRate;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public synchronized boolean isActive() {
        boolean z;
        z = this.mRecorder != null && this.mRecorder.isActive();
        return z;
    }

    public synchronized void start() {
        System.out.println("Start recorder");
        if (this.mRecorder == null) {
            this.mRecorder = new MyRecorderThread();
            this.mRecorder.start();
        }
        if (!(LOG_FILE == null || LOG_FILE.isEmpty())) {
            this.mLog = new ArrayList();
            System.gc();
        }
    }

    public synchronized void stop() {
        if (this.mRecorder != null) {
            this.mRecorder.stopRecorder();
            synchronized (this.mRecorder) {
                try {
                    this.mRecorder.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.mRecorder = null;
        }
        if (this.mLog != null) {
            try {
                logD("Writing log file...");
                OutputStream out = new BufferedOutputStream(new FileOutputStream(LOG_FILE, false));
                for (Short v : this.mLog) {
                    out.write(new StringBuilder(String.valueOf(v.toString())).append("\n").toString().getBytes());
                }
                out.flush();
                out.close();
                logD("Writing log file finished");
            } catch (Exception e2) {
            }
        }
    }

    public synchronized void restart() {
        stop();
        start();
    }

    public void setEndLength(int ms) {
        this.mEndLength = (this.mSampleRate * ms) / 1000;
    }

    private void reset() {
        this.mBufferLength = 0;
        this.mPeaksLength = 0;
        this.mPauseLength = 0;
    }

    public void calibrate(int samples) {
        this.mCalibrateLength = samples;
        this.mSilenceThres = 0;
    }

    private void appendSample(short v) {
        if (this.mBuffer.length == this.mBufferLength) {
            short[] tmp = new short[(this.mBuffer.length + BUF_SIZE)];
            System.arraycopy(this.mBuffer, 0, tmp, 0, this.mBuffer.length);
            this.mBuffer = tmp;
        }
        this.mBuffer[this.mBufferLength] = v;
        this.mBufferLength++;
        if (this.mBufferLength % ON_RECEIVEING_LENGTH == 0) {
            this.mListener.OnReceiving(this.mBufferLength);
        }
    }

    private void appendPeak(int v) {
        if (this.mPeaks.length == this.mPeaksLength) {
            int[] tmp = new int[(this.mPeaks.length + BUF_SIZE)];
            System.arraycopy(this.mPeaks, 0, tmp, 0, this.mPeaksLength);
            this.mPeaks = tmp;
        }
        this.mPeaks[this.mPeaksLength] = v;
        this.mPeaksLength++;
    }

    private void update(short[] samples, int length) {
        for (int i = 0; i < length; i++) {
            update(samples[i]);
        }
    }

    private void update(short v) {
        int absVal = Math.abs(v);
        if (this.mLog != null) {
            this.mLog.add(Short.valueOf(v));
        }
        if (this.mCalibrateLength > 0) {
            if (this.mSilenceThres <= absVal) {
                this.mSilenceThres = absVal + 1;
                logD("Calibrate to " + this.mSilenceThres);
            }
            this.mCalibrateLength--;
            return;
        }
        int silenceThres = (absVal * AUTO_THRES) / 100;
        if (this.mSilenceThres < silenceThres) {
            this.mSilenceThres = silenceThres;
        }
        if (this.mBufferLength != 0) {
            if (absVal > this.mSilenceThres) {
                this.mPauseLength = 0;
            } else {
                this.mPauseLength++;
            }
            if (this.mPauseLength < this.mEndLength) {
                appendSample(v);
                return;
            }
            decodeSamples(this.mBuffer, this.mBufferLength, this.mSilenceThres);
            if (this.mPeaksLength > 0) {
                this.mListener.OnPacketReceived(this.mPeaks, this.mPeaksLength);
            }
            reset();
        } else if (absVal > this.mSilenceThres) {
            logD("Receiving packet...");
            appendSample(v);
        }
    }

    private static final boolean sameSign(int x, int y) {
        int i = 1;
        int i2 = x >= 0 ? 1 : 0;
        if (y >= 0) {
            i = 0;
        }
        return i != i2;
    }

    private void decodeSamples(short[] buffer, int length, int silenceThres) {
        int ppeak = 0;
        int i = 0;
        logD("Decode " + length + " samples");
        while (i < length) {
            int peak = i;
            int maxValue = 0;
            while (i < length && (Math.abs(buffer[i]) <= silenceThres || sameSign(buffer[i], buffer[peak]))) {
                if (Math.abs(buffer[i]) > maxValue) {
                    maxValue = Math.abs(buffer[i]);
                }
                i++;
            }
            silenceThres = (maxValue * AUTO_THRES) / 100;
            i = peak;
            while (i < length && (Math.abs(buffer[i]) <= silenceThres || sameSign(buffer[i], buffer[peak]))) {
                i++;
            }
            if (peak - ppeak > 0) {
                appendPeak(peak - ppeak);
                ppeak = peak;
            }
        }
        this.mPeaksLength = decodeData(this.mPeaks, decodeBits(this.mPeaks, this.mPeaksLength));
    }

    private static final int decodeBits(int[] peaks, int length) {
        if (length < AUDIO_FORMAT) {
            return 0;
        }
        StringBuffer sb;
        int i;
        int bitCount;
        if (D) {
            sb = new StringBuffer(0);
            for (i = 0; i < length; i++) {
                sb.append(peaks[i] + ",");
            }
            logD("Peaks(" + length + "): " + sb.toString());
        }
        int zeroBlock = peaks[AUDIO_FORMAT];
        i = AUDIO_FORMAT;
        int bitCount2 = 0;
        while (i < length - 1) {
            if (peaks[i + 1] < (zeroBlock / AUDIO_FORMAT) - (((zeroBlock / AUDIO_FORMAT) * FREQ_THRES) / 100)) {
                while (i < length + AUDIO_FORMAT && peaks[i + 1] + peaks[i + AUDIO_FORMAT] < (zeroBlock / AUDIO_FORMAT) + (((zeroBlock / AUDIO_FORMAT) * FREQ_THRES) / 100)) {
                    int i2 = i + 1;
                    peaks[i2] = peaks[i2] + peaks[i + AUDIO_FORMAT];
                    for (int j = i + AUDIO_FORMAT; j < length - 1; j++) {
                        peaks[j] = peaks[j + 1];
                    }
                    length--;
                }
                logW("Correct peak[" + i + "]=" + peaks[i] + " and peak[" + (i + 1) + "]=" + peaks[i + 1] + " zeroblock=" + zeroBlock + " length=" + length);
            }
            if (peaks[i] >= (zeroBlock / AUDIO_FORMAT) + (((zeroBlock / AUDIO_FORMAT) * FREQ_THRES) / 100)) {
                zeroBlock = (peaks[i] + zeroBlock) / AUDIO_FORMAT;
                peaks[bitCount2] = 0;
                bitCount = bitCount2 + 1;
            } else if (peaks[i + 1] < (zeroBlock / AUDIO_FORMAT) + (((zeroBlock / AUDIO_FORMAT) * FREQ_THRES) / 100)) {
                zeroBlock = ((peaks[i] + peaks[i + 1]) + zeroBlock) / AUDIO_FORMAT;
                peaks[bitCount2] = 1;
                bitCount = bitCount2 + 1;
                i++;
            } else {
                logW("Skip peak[" + i + "]=" + peaks[i] + " and peak[" + (i + 1) + "]=" + peaks[i + 1] + " zeroblock=" + zeroBlock);
                bitCount = bitCount2;
            }
            i++;
            bitCount2 = bitCount;
        }
        bitCount = bitCount2 + 1;
        peaks[bitCount2] = 1;
        bitCount2 = bitCount + 1;
        peaks[bitCount] = 1;
        if (D) {
            sb = new StringBuffer(bitCount2);
            for (i = 0; i < bitCount2; i++) {
                sb.append(peaks[i]);
            }
            logD("Bits(" + bitCount2 + "): " + sb.toString());
        }
        bitCount = bitCount2;
        return bitCount2;
    }

    private static final int decodeData(int[] bits, int length) {
        int i = 0;
        while (i < length && bits[i] == 0) {
            i++;
        }
        int bytes = (length - i) / 8;
        for (int j = 0; j < bytes; j++) {
            int v = 0;
            for (int k = 0; k < 8; k++) {
                if (bits[i] > 0) {
                    v += ReasonFlags.unused >> k;
                }
                i++;
            }
            bits[j] = v;
        }
        return bytes;
    }
}
