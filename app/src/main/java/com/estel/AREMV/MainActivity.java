package com.estel.AREMV;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
//import com.datecs.audioreader.AudioReader;
//import com.datecs.audioreader.AudioReaderManager;
//import com.datecs.audioreader.emv.BerTlv;
//import com.datecs.audioreader.emv.EMVProcessor.TransactionResponse;
//import com.datecs.audioreader.emv.EMVProcessorCallback;
//import com.datecs.audioreader.emv.PrivateTags;
//import com.datecs.audioreader.emv.Tag;
//import com.estel.AREMVMAKIN.R;
import com.datecs.audioreader.AudioReader;
import com.datecs.audioreader.AudioReaderManager;
import com.datecs.audioreader.emv.EMVProcessor;
import com.datecs.model.CardInfo;
import com.datecs.model.FinancialCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.cookie.CookieSpec;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.bouncycastle.asn1.x509.ReasonFlags;

public class MainActivity extends Activity {
    private static final int DISPLAY_CHILD_LOG = 0;
    private static final int DISPLAY_CHILD_RECEIPT = 1;
    public static final String PREFFS_NAME = "PrefsStatus";
    private static final String PREF_TRANSACTION_SEQUENCE = "transaction_sequence";
    private static final int REQUEST_AMOUNT = 3;
    private static final int REQUEST_SIGN = 4;
    private static final int WAIT_CARD_TIME = 50000;
    static Context context;
    static String networkerror;
    private Dialog AltDialog;
    String CRD;
    String CardHolder;
    private TextView CardLevel;
    private TextView CardStatus;
    String CrdTyp;
    private TextView CurrDate;
    private TextView CurrTime;
    String DT;
    String DT1;
    ImageButton Exit;
    String ExpDate;
    ImageButton Menu;
    String SVC;
    ImageButton Setup;
    Button StButton;
    private TextView SwipeMessage;
    String TM;
    String Tmp;
    String TmpexpDate;
    String TmpexpDate1;
    String Tr2;
    String TxnDt;
    String expiryDate;
    String first6Pan;
    private Handler handler;
    String last4Pan;
    private EditText mAmountView;
    private ViewSwitcher mContentSwitcher;
    private HeadsetReceiver mHeadsetReceiver;
    private ScrollView mLogScrollView;
    private TextView mLogTitleView;
    private TextView mLogView;
    private SharedPreferences mPrefs;
    private ScrollView mRcpScrollView;
    private TextView mReceiptView;
    private TextView mStatusView;
    //    ModelData mdobj;
    ProgressDialog progress;
    String strDate;
    String strTime;
    private TextView textresponse;
    private TextView txtDate;
    private TextView txtTime;
    String xxx;


    //
//    class 2 implements Runnable {
//        private final /* synthetic */ ProgressDialog val$dialog;
//        private final /* synthetic */ int val$resId;
//
//        2(ProgressDialog progressDialog, int i) {
//            this.val$dialog = progressDialog;
//            this.val$resId = i;
//        }
//
//        public void run() {
//            this.val$dialog.setMessage(MainActivity.this.getString(this.val$resId));
//        }
//    }
//
//    class 3 implements Runnable {
//        private final /* synthetic */ int val$color;
//        private final /* synthetic */ int val$resId;
//
//        3(int i, int i2) {
//            this.val$resId = i;
//            this.val$color = i2;
//        }
//
//        public void run() {
//            MainActivity.this.mStatusView.setVisibility(MainActivity.DISPLAY_CHILD_LOG);
//            MainActivity.this.mStatusView.setText(this.val$resId);
//            MainActivity.this.mStatusView.setTextColor(this.val$color);
//        }
//    }
//
//    class 4 implements Runnable {
//        4() {
//        }
//
//        public void run() {
//            MainActivity.this.mContentSwitcher.setDisplayedChild(MainActivity.DISPLAY_CHILD_RECEIPT);
//            MainActivity.this.mLogTitleView.setText("Receipt Log");
//        }
//    }
//
//
//    class 6 implements Runnable {
//        private final /* synthetic */ ProgressDialog val$dialog;
//        private final /* synthetic */ AudioReaderRunnable val$r;
//
//        6(ProgressDialog progressDialog, AudioReaderRunnable audioReaderRunnable) {
//            this.val$dialog = progressDialog;
//            this.val$r = audioReaderRunnable;
//        }
//
//        public void run() {
//            String Amt;
//            Context context = MainActivity.this;
//            synchronized (context) {
//                AudioReader reader = null;
//                try {
//                    System.out.println("Starting....");
//                    reader = AudioReaderManager.getReader(context);
//                    long startTime = System.currentTimeMillis();
//                    this.val$r.run(this.val$dialog, reader);
//                    long endTime = System.currentTimeMillis() - startTime;
//                    System.out.println("# Finish for " + (endTime / 1000) + "." + ((endTime % 1000) / 100) + "s\n");
//                    if (reader != null) {
//                        reader.close();
//                    }
//                    this.val$dialog.dismiss();
//                    System.out.println("1.Transaction Status:" + MainActivity.this.mdobj.getEMVStatus());
//                    if (MainActivity.this.mdobj.getEMVStatus().equals("DONE")) {
//                        MainActivity.this.mdobj.setTotal(null);
//                        Amt = null;
//                        MainActivity.this.CallAmountEntry();
//                        while (Amt == null) {
//                            Amt = MainActivity.this.mdobj.getTotal();
//                        }
//                        System.out.println("Amount Entered:" + Amt);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    StringWriter sw = new StringWriter();
//                    e.printStackTrace(new PrintWriter(sw));
//                    System.out.println("CRITICAL ERROR: " + sw.toString() + "\n");
//                    if (reader != null) {
//                        reader.close();
//                    }
//                    this.val$dialog.dismiss();
//                    System.out.println("1.Transaction Status:" + MainActivity.this.mdobj.getEMVStatus());
//                    if (MainActivity.this.mdobj.getEMVStatus().equals("DONE")) {
//                        MainActivity.this.mdobj.setTotal(null);
//                        Amt = null;
//                        MainActivity.this.CallAmountEntry();
//                        while (Amt == null) {
//                            Amt = MainActivity.this.mdobj.getTotal();
//                        }
//                        System.out.println("Amount Entered:" + Amt);
//                    }
//                } catch (Throwable th) {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                    this.val$dialog.dismiss();
//                    System.out.println("1.Transaction Status:" + MainActivity.this.mdobj.getEMVStatus());
//                    if (MainActivity.this.mdobj.getEMVStatus().equals("DONE")) {
//                        MainActivity.this.mdobj.setTotal(null);
//                        Amt = null;
//                        MainActivity.this.CallAmountEntry();
//                        while (Amt == null) {
//                            Amt = MainActivity.this.mdobj.getTotal();
//                        }
//                        System.out.println("Amount Entered:" + Amt);
//                    }
//                }
//            }
//        }
//    }
//
//    class 7 implements OnClickListener {
//        7() {
//        }
//
//        public void onClick(View v) {
//            MainActivity.this.AltDialog.dismiss();
//            String RS = MainActivity.this.mdobj.getResCode();
//            System.out.println("Sale OnClick RS:" + RS);
//            if (RS == null) {
//                return;
//            }
//            if (RS.equals("0")) {
//                String LTSDATA = "SALE|Success|" + MainActivity.this.mdobj.getCardType() + "|" + MainActivity.this.mdobj.getPAN4() + "|" + MainActivity.this.mdobj.getTotal() + "|" + MainActivity.this.mdobj.getTxnDate() + "|" + MainActivity.this.mdobj.getInvoice() + "|" + MainActivity.this.mdobj.getInvoice() + "|" + MainActivity.this.mdobj.getTxnID() + "|" + MainActivity.this.mdobj.getCardHolderName();
//                System.out.println("Sale File Data:" + LTSDATA);
//                UtilClass uc = new UtilClass(MainActivity.this);
//                uc.WriteData(LTSDATA);
//                String Res = uc.ReadData();
//                SwipeCardActivity.setMd(MainActivity.this.mdobj);
//                System.out.println("Result for Sale ReadData:" + Res);
//                MainActivity.this.CallSignature();
//                return;
//            }
//            LTSDATA = "SALE|Failed|" + MainActivity.this.mdobj.getCardType() + "|" + MainActivity.this.mdobj.getPAN4() + "|" + MainActivity.this.mdobj.getTotal() + "|" + MainActivity.this.mdobj.getTxnDate() + "|null|null|null|" + MainActivity.this.mdobj.getCardHolderName();
//            System.out.println("Sale File Data:" + LTSDATA);
//            new UtilClass(MainActivity.this).WriteData(LTSDATA);
//        }
//    }

    private class HeadsetReceiver extends BroadcastReceiver {
        private final Handler mHandler;

        private HeadsetReceiver() {
            this.mHandler = new Handler();
        }

        public void onReceive(Context context, Intent intent) {
            boolean headsetAvailable;
            final boolean isConnected;
            int i = 0;
            if (intent.getIntExtra("state", MainActivity.DISPLAY_CHILD_LOG) == MainActivity.DISPLAY_CHILD_RECEIPT) {
                headsetAvailable = true;
            } else {
                headsetAvailable = false;
            }
            boolean microphoneAvailable;
            if (intent.getIntExtra("microphone", MainActivity.DISPLAY_CHILD_LOG) == MainActivity.DISPLAY_CHILD_RECEIPT) {
                microphoneAvailable = true;
            } else {
                microphoneAvailable = false;
            }
            if (headsetAvailable && microphoneAvailable) {
                isConnected = true;
            } else {
                isConnected = false;
            }
            this.mHandler.removeCallbacksAndMessages(null);
            Handler handler = this.mHandler;
            if (isConnected) {
                i = 500;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.updateReaderState(isConnected);
                }
            }, (long) i);
        }
    }

    //
//    private class SendToHost extends AsyncTask<String, Void, String> {
//        private Context context;
//        private ProgressDialog mProgressDialog;
//        String responsealert;
//
//        public SendToHost(Context context) {
//            this.responsealert = "";
//            this.context = context;
//            this.mProgressDialog = new ProgressDialog(context);
//            this.mProgressDialog.setMessage("Authorizing...");
//            this.mProgressDialog.setIndeterminate(false);
//            this.mProgressDialog.setProgressStyle(MainActivity.DISPLAY_CHILD_LOG);
//            this.mProgressDialog.setCancelable(false);
//        }
//
//        protected String doInBackground(String... arg0) {
//            String ReqData = MainActivity.this.GenData(MainActivity.this.mdobj.getTotal());
//            HttpClient client = HttpClientFactory.getThreadSafeClient();
//            System.out.println("Authorizing Test 1");
//            try {
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(client.execute(new HttpPost(ReqData)).getEntity().getContent()));
//                String str = "";
//                while (true) {
//                    str = buffer.readLine();
//                    if (str == null) {
//                        break;
//                    }
//                    this.responsealert += str;
//                }
//                if (this.responsealert.equalsIgnoreCase("Error") && this.responsealert.equals("<html>")) {
//                    this.responsealert = null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                this.responsealert = null;
//            }
//            return null;
//        }
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this.mProgressDialog.show();
//        }
//
//        protected void onPostExecute(String result) {
//            this.mProgressDialog.dismiss();
//            if (this.responsealert == null || this.responsealert.contains("Runtime Error")) {
//                System.out.println("1.Authorizing Test 4:" + this.responsealert);
//                MainActivity.this.alertbox(MainActivity.networkerror, "Transaction Result", this.context);
//                return;
//            }
//            System.out.println("2.Authorizing Test 5:" + this.responsealert);
//            MainActivity.this.alertbox(this.responsealert, "Transaction Result", this.context);
//        }
//    }
//
//    class 8 implements AudioReaderRunnable {
//
//        class 1 implements EMVProcessorCallback {
//            1() {
//            }
//
//            public byte[] onCardHolderSelectionRequest(byte[] data) {
//                System.out.println("    @Taggggggggg: " + BerTlv.createList(data));
//                int selectionIndex = -1;
//                List<BerTlv> listOfCandidates = null;
//                for (BerTlv tlv : BerTlv.createList(data)) {
//                    if (tlv.getTag().toIntValue() == 225) {
//                        listOfCandidates = BerTlv.createList(tlv.getValue());
//                    }
//                }
//                List<BerTlv> result = new ArrayList();
//                if (listOfCandidates != null) {
//                    List<String> listOfAppLabels = new ArrayList();
//                    List<Integer> listOfAppIndexes = new ArrayList();
//                    int applicationIndex = MainActivity.DISPLAY_CHILD_LOG;
//                    for (BerTlv tlv2 : listOfCandidates) {
//                        if (tlv2.getTag().toIntValue() == 228) {
//                            Map<Tag, byte[]> appMap = BerTlv.createMap(tlv2.getValue());
//                            String appLabel = null;
//                            if (appMap.containsKey(new Tag(EMVTags.TAG_9F12_APP_PREFERRED_NAME)) && appMap.containsKey(new Tag(EMVTags.TAG_9F11_ISSUER_CODE_TABLE_INDEX)) && EMVProcessorHelper.decodeInt((byte[]) appMap.get(new Tag(EMVTags.TAG_9F11_ISSUER_CODE_TABLE_INDEX))) == MainActivity.DISPLAY_CHILD_RECEIPT) {
//                                appLabel = EMVProcessorHelper.decodeAscii((byte[]) appMap.get(new Tag(EMVTags.TAG_9F12_APP_PREFERRED_NAME)));
//                            }
//                            if (appLabel == null && appMap.containsKey(new Tag(80))) {
//                                appLabel = EMVProcessorHelper.decodeAscii((byte[]) appMap.get(new Tag(80)));
//                            }
//                            if (appLabel == null && appMap.containsKey(new Tag(80))) {
//                                appLabel = EMVProcessorHelper.decodeAscii((byte[]) appMap.get(new Tag(80)));
//                            }
//                            if (appLabel == null && appMap.containsKey(new Tag(PrivateTags.TAG_C2_APPLICATION_LABEL_DEFAULT))) {
//                                appLabel = EMVProcessorHelper.decodeHex((byte[]) appMap.get(new Tag(PrivateTags.TAG_C2_APPLICATION_LABEL_DEFAULT)));
//                            }
//                            if (null == null && appMap.containsKey(new Tag(87))) {
//                                String Tr = EMVProcessorHelper.decodeAscii((byte[]) appMap.get(new Tag(87)));
//                                System.out.println("*****TRACK 2 DATA TEST: " + Tr);
//                            }
//                            if (appLabel != null) {
//                                listOfAppLabels.add(appLabel);
//                                listOfAppIndexes.add(Integer.valueOf(applicationIndex));
//                            }
//                            applicationIndex += MainActivity.DISPLAY_CHILD_RECEIPT;
//                        }
//                    }
//                    if (listOfAppLabels != null && listOfAppLabels.size() > 0) {
//                        String[] applications = (String[]) listOfAppLabels.toArray(new String[listOfAppLabels.size()]);
//                        selectionIndex = UiHelper.selectDialogItemOnThread(MainActivity.this, R.string.select_application, applications);
//                        if (selectionIndex >= 0) {
//                            int resultIndex = ((Integer) listOfAppIndexes.get(selectionIndex)).intValue();
//                            System.out.println("    Application: " + applications[selectionIndex] + "\n");
//                            byte[] bArr = new byte[MainActivity.DISPLAY_CHILD_RECEIPT];
//                            bArr[MainActivity.DISPLAY_CHILD_LOG] = (byte) resultIndex;
//                            result.add(new BerTlv((int) PrivateTags.TAG_C5_SELECTED_INDEX, bArr));
//                        }
//                    }
//                }
//                if (selectionIndex < 0) {
//                    bArr = new byte[MainActivity.DISPLAY_CHILD_RECEIPT];
//                    bArr[MainActivity.DISPLAY_CHILD_LOG] = (byte) -1;
//                    result.add(new BerTlv((int) PrivateTags.TAG_C5_SELECTED_INDEX, bArr));
//                }
//                System.out.println("BerTlv Result:" + result);
//                return BerTlv.listToByteArray(result);
//            }
//
//            public byte[] onPanCheckingRequest(byte[] data) {
//                List<BerTlv> result = new ArrayList();
//                result.add(EMVProcessorHelper.createTlv((int) HttpStatus.SC_NO_CONTENT, "01"));
//                System.out.println("PAN Application Lavel:" + result);
//                return BerTlv.listToByteArray(result);
//            }
//
//            public byte[] onOnlineProcessingRequest(byte[] data) {
//                System.out.println("Process online");
//                String EMV_OL_SUCCESS_TC = "0000";
//                List<BerTlv> result = new ArrayList();
//                result.add(EMVProcessorHelper.createTlv((int) PrivateTags.TAG_C7_ONLINE_AUTHORIZATION_PROCESSING_RESULT, "0000"));
//                result.add(EMVProcessorHelper.createTlv((int) EMVTags.TAG_8A_AUTH_RESP_CODE, "3030"));
//                System.out.println("Return processing result:" + result);
//                return BerTlv.listToByteArray(result);
//            }
//
//            public void onConfirmOrReverseOnlineRequest(byte[] data) {
//                int transactionResult = EMVProcessorHelper.decodeInt(BerTlv.find(data, (int) HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION).getValue());
//                boolean reverseOnline = true;
//                if (transactionResult == MainActivity.DISPLAY_CHILD_RECEIPT || transactionResult == EMVTags.TAG_81_AMOUNT_AUTHORISED_BINARY) {
//                    reverseOnline = false;
//                }
//                if (reverseOnline) {
//                    System.out.println("Reverse online\n");
//                } else {
//                    System.out.println("Confirm online\n");
//                }
//            }
//        }
//
//        8() {
//        }
//

    //    }
//
    public MainActivity() {
        this.mdobj = SwipeCardActivity.getMd();
//        this.TM = null;
//        this.DT = null;
//        this.DT1 = null;
//        this.Tmp = null;
//        this.first6Pan = new String();
//        this.last4Pan = new String();
//        this.expiryDate = new String();
//        this.TmpexpDate = new String();
//        this.TmpexpDate1 = new String();
//        this.CrdTyp = new String();
//        this.CardHolder = new String();
//        this.xxx = new String();
//        this.CRD = new String();
//        this.ExpDate = new String();
//        this.TxnDt = new String();
//        this.Tr2 = new String();
//        this.SVC = new String();
//        this.progress = null;
//        this.strDate = null;
//        this.strTime = null;
        this.mHeadsetReceiver = new HeadsetReceiver();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sreader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences settings = getSharedPreferences(PREFFS_NAME, DISPLAY_CHILD_LOG);
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.SwipeMessage = (TextView) findViewById(R.id.textViewSwipeCard);
        this.StButton = (Button) findViewById(R.id.bt_start_transaction);
        this.Exit = (ImageButton) findViewById(R.id.ImgBtnExitSwipe);
        this.Menu = (ImageButton) findViewById(R.id.ImgBtnMenuSwipe);
        this.Setup = (ImageButton) findViewById(R.id.ImgBtnSetupSwipe);
        this.txtDate = (TextView) findViewById(R.id.TxtDate);
        this.txtTime = (TextView) findViewById(R.id.TxtTime);
        Calendar cal = Calendar.getInstance();
//        Object[] objArr = new Object[DISPLAY_CHILD_RECEIPT];
//        objArr[DISPLAY_CHILD_LOG] = Integer.valueOf(cal.get(5));
//        StringBuilder append = new StringBuilder(String.valueOf(String.format("%02d", objArr))).append(CookieSpec.PATH_DELIM);
//        objArr = new Object[DISPLAY_CHILD_RECEIPT];
//        objArr[DISPLAY_CHILD_LOG] = Integer.valueOf(cal.get(2) + DISPLAY_CHILD_RECEIPT);
//        append = append.append(String.format("%02d", objArr)).append(CookieSpec.PATH_DELIM);
//        objArr = new Object[DISPLAY_CHILD_RECEIPT];
//        objArr[DISPLAY_CHILD_LOG] = Integer.valueOf(cal.get(DISPLAY_CHILD_RECEIPT));
//        this.strDate = append.append(String.format("%02d", objArr)).toString();
//        objArr = new Object[DISPLAY_CHILD_RECEIPT];
//        objArr[DISPLAY_CHILD_LOG] = Integer.valueOf(cal.get(10));
//        append = new StringBuilder(String.valueOf(String.format("%02d", objArr))).append(":");
//        objArr = new Object[DISPLAY_CHILD_RECEIPT];
//        objArr[DISPLAY_CHILD_LOG] = Integer.valueOf(cal.get(12));
//        append = append.append(String.format("%02d", objArr)).append(":");
//        objArr = new Object[DISPLAY_CHILD_RECEIPT];
//        objArr[DISPLAY_CHILD_LOG] = Integer.valueOf(cal.get(13));
//        this.strTime = append.append(String.format("%02d", objArr)).toString();
        this.txtDate.setText(this.strDate);
        this.txtTime.setText(this.strTime);
//        this.mdobj.setEMVStatus("");
        this.StButton.setVisibility(View.INVISIBLE);
        context = this;
        this.SwipeMessage.setText("");
        findViewById(R.id.bt_start_transaction).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startTransaction();
            }
        });
        setVolumeControlStream(REQUEST_AMOUNT);
    }

    public void onStart() {
        super.onStart();
        registerReceiver(this.mHeadsetReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
        updateReaderState(false);
    }

    public void onStop() {
        super.onStop();
        unregisterReceiver(this.mHeadsetReceiver);
    }

    private void updateReaderState(boolean connected) {
        if (connected) {
            this.SwipeMessage.setText("Swipe/Insert Card");
//            this.SwipeMessage.setTextColor(-16776961);
            this.StButton.setVisibility(View.VISIBLE);
            return;
        }
//        this.mdobj.setBTConn(false);
        this.SwipeMessage.setText("Plugin Your Reader");
//        this.SwipeMessage.setTextColor(-65536);
    }

//    public String getVersionName() {
//        try {
//            return getPackageManager().getPackageInfo(getPackageName(), DISPLAY_CHILD_LOG).versionName;
//        } catch (NameNotFoundException e) {
//            return null;
//        }
//    }

//    private void resetLogView() {
//        this.mContentSwitcher.setDisplayedChild(DISPLAY_CHILD_LOG);
//        this.mLogView.setText("");
//        this.mReceiptView.setText("");
//        this.mStatusView.setVisibility(8);
//    }

    //    private void hideKeyboard() {
//        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), DISPLAY_CHILD_LOG);
//    }
//
    private void setDialogText(final ProgressDialog dialog, final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage(MainActivity.this.getString(resId));
            }
        });
    }

//    public void setStatusText(int resId, int color) {
//        runOnUiThread(new 3(resId, color));
//    }
//
//    private int getTransactionSequence() {
//        int value = (this.mPrefs.getInt(PREF_TRANSACTION_SEQUENCE, DISPLAY_CHILD_LOG) + DISPLAY_CHILD_RECEIPT) % 1000000;
//        this.mPrefs.edit().putInt(PREF_TRANSACTION_SEQUENCE, value).commit();
//        return value;
//    }
//
//    private void showReceiptLog() {
//        runOnUiThread(new 4());
//    }
//
//    private void showStatus(TransactionResponse response) {
//        if (response.getProcessingResult() == 0) {
//            int transactionResult = response.getTransactonResult();
//            switch (transactionResult) {
//                case DISPLAY_CHILD_LOG /*0*/:
//                    System.out.println("Transaction Decliend");
//                    return;
//                case DISPLAY_CHILD_RECEIPT /*1*/:
//                case EMVTags.TAG_81_AMOUNT_AUTHORISED_BINARY /*129*/:
//                    System.out.println("Transaction Authorized");
//                    return;
//                case ReasonFlags.certificateHold /*2*/:
//                    System.out.println("Transaction Aborted");
//                    return;
//                case REQUEST_AMOUNT /*3*/:
//                    System.out.println("Transaction Not Accepted");
//                    return;
//                default:
//                    throw new RuntimeException("Invalid transaction result: " + transactionResult);
//            }
//        }
//        System.out.println("Transaction Aborted");
//    }

//    public void OnClick(View v) {
//        switch (v.getId()) {
//            case R.id.ImgBtnExitSwipe:
//                finish();
//            case R.id.ImgBtnMenuSwipe:
//                startActivity(new Intent(this, ActivityMainMenu.class));
//                finish();
//            default:
//        }
//    }

    public void onBackPressed() {
    }

    private void runAudioReaderTaskAync(final AudioReaderRunnable r) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Pls wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String Amt;
                Context context = MainActivity.this;
                synchronized (context) {
                    AudioReader reader = null;
                    try {
                        System.out.println("Starting....");
                        reader = AudioReaderManager.getReader(context);
                        long startTime = System.currentTimeMillis();
                        r.run(dialog, reader);
                        long endTime = System.currentTimeMillis() - startTime;
                        System.out.println("# Finish for " + (endTime / 1000) + "." + ((endTime % 1000) / 100) + "s\n");
                        if (reader != null) {
                            reader.close();
                        }
                        dialog.dismiss();
//                        System.out.println("1.Transaction Status:" + MainActivity.this.mdobj.getEMVStatus());
//                        if (MainActivity.this.mdobj.getEMVStatus().equals("DONE")) {
//                            MainActivity.this.mdobj.setTotal(null);
//                            Amt = null;
//                            MainActivity.this.CallAmountEntry();
//                            while (Amt == null) {
//                                Amt = MainActivity.this.mdobj.getTotal();
//                            }
//                            System.out.println("Amount Entered:" + Amt);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        System.out.println("CRITICAL ERROR: " + sw.toString() + "\n");
                        if (reader != null) {
                            reader.close();
                        }
                        dialog.dismiss();
//                        System.out.println("1.Transaction Status:" + MainActivity.this.mdobj.getEMVStatus());
//                        if (MainActivity.this.mdobj.getEMVStatus().equals("DONE")) {
//                            MainActivity.this.mdobj.setTotal(null);
//                            Amt = null;
//                            MainActivity.this.CallAmountEntry();
//                            while (Amt == null) {
//                                Amt = MainActivity.this.mdobj.getTotal();
//                            }
//                            System.out.println("Amount Entered:" + Amt);
//                        }
                    } catch (Throwable th) {
                        if (reader != null) {
                            reader.close();
                        }
                        dialog.dismiss();
//                        System.out.println("1.Transaction Status:" + MainActivity.this.mdobj.getEMVStatus());
//                        if (MainActivity.this.mdobj.getEMVStatus().equals("DONE")) {
//                            MainActivity.this.mdobj.setTotal(null);
//                            Amt = null;
//                            MainActivity.this.CallAmountEntry();
//                            while (Amt == null) {
//                                Amt = MainActivity.this.mdobj.getTotal();
//                            }
//                            System.out.println("Amount Entered:" + Amt);
//                        }
                    }
                }
            }
        }).start();
    }

    //
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_AMOUNT /*3*/:
//                System.out.println("Test For Amount Entry");
//                if (!this.mdobj.getTotal().equals("")) {
//                    Authorization();
//                }
//            case REQUEST_SIGN /*4*/:
//                this.mdobj.setEMVStatus("");
//                System.out.println("Test For Signature");
//            default:
//        }
//    }
//
//    private void Authorization() {
//        new SendToHost(context).execute(new String[DISPLAY_CHILD_LOG]);
//    }
//
//    protected void alertbox(String mymessage, String heading, Context context) {
//        UtilClass UCLS = new UtilClass(this);
//        this.AltDialog = new Dialog(context);
//        this.AltDialog.getWindow().setBackgroundDrawable(new ColorDrawable(DISPLAY_CHILD_LOG));
//        this.AltDialog.setContentView(R.layout.dialogbox);
//        ((TextView) this.AltDialog.findViewById(R.id.res_heading)).setText(heading);
//        TextView ACode = (TextView) this.AltDialog.findViewById(R.id.AuthCod);
//        TextView RRN = (TextView) this.AltDialog.findViewById(R.id.RefNum);
//        ((TextView) this.AltDialog.findViewById(R.id.responsestring)).setText(UCLS.SplitResVal(mymessage));
//        String Res = this.mdobj.getResCode();
//        if (Res == null) {
//            ACode.setText("");
//            RRN.setText("");
//        } else if (Res.equals("0")) {
//            String AC = this.mdobj.getInvoice();
//            String REF = this.mdobj.getRRN();
//            ACode.setText("Auth Code: " + AC);
//            RRN.setText("REF NO: " + REF);
//        } else {
//            ACode.setText("");
//            RRN.setText("");
//        }
//        this.AltDialog.findViewById(R.id.dialogbutton1).setOnClickListener(new 7());
//        this.AltDialog.show();
//    }
//
//    String GenData(String AmtTotal) {
//        String CrdHldName = "";
//        String CType = "";
//        String Track2 = "";
//        String MSISDN = getSharedPreferences(PREFFS_NAME, DISPLAY_CHILD_LOG).getString("MSISDN", null);
//        String PAN = this.mdobj.getPAN();
//        String ExpDate = this.mdobj.getExpData();
//        CrdHldName = this.mdobj.getCardHolderName();
//        CType = this.mdobj.getCardType();
//        Track2 = this.mdobj.getTrack2();
//        String TxnData = new StringBuilder(String.valueOf(PAN)).append("|").append(ExpDate).append("|").append(AmtTotal).append("|").append(CrdHldName).append("|").append(CType).append("|").append(Track2).append("|").append("1234").toString();
//        String Rnd = new UtilClass(this).RandomNumber();
//        String KeyData = new StringBuilder(String.valueOf(this.mdobj.getPAN())).append(Rnd).toString();
//        System.out.println("PAN DATA:" + PAN);
//        System.out.println("EXP DATE DATA:" + ExpDate);
//        System.out.println("Total Amount:" + AmtTotal);
//        System.out.println("Card Holder Name:" + CrdHldName);
//        System.out.println("Track 2:" + Track2);
//        System.out.println("Card Type:" + CType);
//        System.out.println("KEY DATA:" + KeyData);
//        System.out.println("TXN DATA:" + TxnData);
//        String USR3DESDATA = new TripleDES().encrypt(TxnData, KeyData);
//        System.out.println("Card Encrypted Data:" + USR3DESDATA);
//        String IMEIorDID = new UtilClass(this).getIMEINo();
//        System.out.println("Device ID:" + IMEIorDID);
//        String finalString = "005" + "|" + KeyData + "|" + USR3DESDATA + "|" + IMEIorDID + "|" + MSISDN;
//        System.out.println("Final Data:" + finalString);
//        String encryptedtext = new TripleDES().get3DESEncryptedMessage1(finalString, "");
//        String CompleteData = getString(R.string.UrlString) + encryptedtext;
//        System.out.println("Complete Data:" + CompleteData);
//        return CompleteData;
//    }
//
//    private void CallSignature() {
//        startActivityForResult(new Intent(this, CaptureSignature.class), REQUEST_SIGN);
//    }
//
//    private void CallAmountEntry() {
//        startActivityForResult(new Intent(this, GetDataActivity.class), REQUEST_AMOUNT);
//    }
//
    private void startTransaction() {
        runAudioReaderTaskAync(new AudioReaderRunnable() {
            @Override
            public void run(ProgressDialog progressDialog, AudioReader audioReader) throws IOException {
                audioReader.powerOn();
                System.out.println(audioReader.getSerialNumber());
                System.out.println(audioReader.getBattery());
                EMVProcessor emvProcessor = new EMVProcessor(audioReader);
                MainActivity.this.setDialogText(progressDialog, R.string.msg_please_insert_card);
                audioReader.setMagneticCardMode(3, 1, 1, 1);
                audioReader.setMagneticCardMaskMode(true, 6, 4);
                CardInfo cardInfo = audioReader.waitForCard(50000);
                System.out.println(cardInfo);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "1.reader.waitForCard:";
//            r26.<init>(r27);
//            r0 = r15.cardType;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r26 = 2131099733; // 0x7f060055 float:1.7811828E38 double:1.052903166E-314;
//            r0 = r25;
//            r1 = r31;
//            r2 = r26;
                MainActivity.this.setDialogText(progressDialog, R.string.msg_Reading_Card);
//            r0 = r15.cardType;
//            r25 = r0;
//            if (r25 != 0) goto L_0x0480;
//        L_0x00d8:
//            r25 = java.lang.System.out;
//            r26 = "\u00bb Init Magstripe processing\n";
//            r25.println(r26);
                System.out.println("Init Magstripe processing");
//            r20
                FinancialCard financialCardData = audioReader.getFinancialCardData();
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r20;
//            r0 = r0.holder;
//            r26 = r0;
//            r25.setCardHolderName(r26);
//            r0 = r20;
//            r0 = r0.number;
//            r25 = r0;
//            r0 = r20;
//            r0 = r0.number;
//            r26 = r0;
//            r26 = r26.length();
//            r26 = r26 + -4;
//            r0 = r20;
//            r0 = r0.number;
//            r27 = r0;
//            r27 = r27.length();
//            r11 = r25.substring(r26, r27);
//            r0 = r20;
//            r0 = r0.number;
//            r25 = r0;
//            r26 = 0;
//            r27 = 6;
//            r12 = r25.substring(r26, r27);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r20;
//            r0 = r0.number;
//            r26 = r0;
//            r0 = r20;
//            r0 = r0.number;
//            r27 = r0;
//            r27 = r27.length();
//            r27 = r27 + -4;
//            r0 = r20;
//            r0 = r0.number;
//            r28 = r0;
//            r28 = r28.length();
//            r26 = r26.substring(r27, r28);
//            r25.setPAN4(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = new java.lang.StringBuilder;
//            r27 = "XXXX XXXX ";
//            r26.<init>(r27);
//            r0 = r20;
//            r0 = r0.number;
//            r27 = r0;
//            r0 = r20;
//            r0 = r0.number;
//            r28 = r0;
//            r28 = r28.length();
//            r28 = r28 + -4;
//            r0 = r20;
//            r0 = r0.number;
//            r29 = r0;
//            r29 = r29.length();
//            r27 = r27.substring(r28, r29);
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.setCardNumber(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = new java.lang.StringBuilder;
//            r0 = r20;
//            r0 = r0.month;
//            r27 = r0;
//            r27 = java.lang.String.valueOf(r27);
//            r26.<init>(r27);
//            r0 = r20;
//            r0 = r0.year;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.setExpData(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = new java.lang.StringBuilder;
//            r0 = r20;
//            r0 = r0.month;
//            r27 = r0;
//            r27 = java.lang.String.valueOf(r27);
//            r26.<init>(r27);
//            r27 = "/";
//            r26 = r26.append(r27);
//            r0 = r20;
//            r0 = r0.year;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.setExpirayDate(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r26 = r0;
//            r0 = r26;
//            r0 = r0.TxnDt;
//            r26 = r0;
//            r25.setTxnDate(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = new java.lang.StringBuilder;
//            r27 = java.lang.String.valueOf(r12);
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r11);
//            r26 = r26.toString();
//            r25.setPAN(r26);
//            r0 = r20;
//            r0 = r0.number;
//            r25 = r0;
//            r26 = 0;
//            r27 = 2;
//            r6 = r25.substring(r26, r27);
//            r25 = "40";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x0246:
//            r25 = "41";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x0250:
//            r25 = "42";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x025a:
//            r25 = "43";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x0264:
//            r25 = "44";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x026e:
//            r25 = "45";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x0278:
//            r25 = "46";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x0282:
//            r25 = "47";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x028c:
//            r25 = "48";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x02a0;
//        L_0x0296:
//            r25 = "49";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 == 0) goto L_0x0377;
//        L_0x02a0:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "VISA";
//            r25.setCardType(r26);
//        L_0x02b1:
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "  Holder: ";
//            r26.<init>(r27);
//            r0 = r20;
//            r0 = r0.holder;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r27 = "\n";
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "  PAN: ";
//            r26.<init>(r27);
//            r0 = r20;
//            r0 = r0.number;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r27 = "\n";
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "  Expiry: ";
//            r26.<init>(r27);
//            r0 = r20;
//            r0 = r0.month;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r27 = "/";
//            r26 = r26.append(r27);
//            r0 = r20;
//            r0 = r0.year;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r27 = "\n";
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "  Encrypted block: ";
//            r26.<init>(r27);
//            r0 = r20;
//            r0 = r0.data;
//            r27 = r0;
//            r27 = com.estel.AREMV.HexUtil.byteArrayToHexString(r27);
//            r26 = r26.append(r27);
//            r27 = "\nData Only:";
//            r26 = r26.append(r27);
//            r0 = r20;
//            r0 = r0.data;
//            r27 = r0;
//            r27 = r27.toString();
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "DONE";
//            r25.setEMVStatus(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            com.estel.AREMV.SwipeCardActivity.setMd(r25);
//        L_0x0373:
//            r32.powerOff();
//            return;
//        L_0x0377:
//            r25 = "51";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03a9;
//        L_0x0381:
//            r25 = "52";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03a9;
//        L_0x038b:
//            r25 = "53";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03a9;
//        L_0x0395:
//            r25 = "54";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03a9;
//        L_0x039f:
//            r25 = "55";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 == 0) goto L_0x03bc;
//        L_0x03a9:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "MasterCard";
//            r25.setCardType(r26);
//            goto L_0x02b1;
//        L_0x03bc:
//            r25 = "50";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03ee;
//        L_0x03c6:
//            r25 = "58";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03ee;
//        L_0x03d0:
//            r25 = "63";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03ee;
//        L_0x03da:
//            r25 = "67";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x03ee;
//        L_0x03e4:
//            r25 = "62";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 == 0) goto L_0x0401;
//        L_0x03ee:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "MaestroCard";
//            r25.setCardType(r26);
//            goto L_0x02b1;
//        L_0x0401:
//            r25 = "34";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x0415;
//        L_0x040b:
//            r25 = "37";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 == 0) goto L_0x0428;
//        L_0x0415:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "AMEX";
//            r25.setCardType(r26);
//            goto L_0x02b1;
//        L_0x0428:
//            r25 = "35";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 == 0) goto L_0x0445;
//        L_0x0432:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "JCB";
//            r25.setCardType(r26);
//            goto L_0x02b1;
//        L_0x0445:
//            r25 = "30";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x046d;
//        L_0x044f:
//            r25 = "20";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x046d;
//        L_0x0459:
//            r25 = "21";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 != 0) goto L_0x046d;
//        L_0x0463:
//            r25 = "36";
//            r0 = r25;
//            r25 = r6.equals(r0);
//            if (r25 == 0) goto L_0x02b1;
//        L_0x046d:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "Diners Club";
//            r25.setCardType(r26);
//            goto L_0x02b1;
//        L_0x0480:
//            r0 = r15.cardType;
//            r25 = r0;
//            r26 = 1;
//            r0 = r25;
//            r1 = r26;
//            if (r0 != r1) goto L_0x0373;
//        L_0x048c:
//            r21 = new java.util.ArrayList;
//            r21.<init>();
//            r25 = 196; // 0xc4 float:2.75E-43 double:9.7E-322;
//            r26 = "40008000";
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r25 = 156; // 0x9c float:2.19E-43 double:7.7E-322;
//            r26 = "00";
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r25 = 40732; // 0x9f1c float:5.7078E-41 double:2.01243E-319;
//            r26 = "00000001";
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r25 = 40769; // 0x9f41 float:5.713E-41 double:2.01426E-319;
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r26 = r0;
//            r26 = r26.getTransactionSequence();
//            r26 = com.estel.AREMV.EMVProcessorHelper.encodeTransactionSequence(r26);
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r25 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
//            r26 = "100";
//            r26 = com.estel.AREMV.EMVProcessorHelper.encodeAmount(r26);
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r25 = 154; // 0x9a float:2.16E-43 double:7.6E-322;
//            r26 = java.util.Calendar.getInstance();
//            r26 = com.estel.AREMV.EMVProcessorHelper.encodeTransactionDate(r26);
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r25 = 40737; // 0x9f21 float:5.7085E-41 double:2.01268E-319;
//            r26 = java.util.Calendar.getInstance();
//            r26 = com.estel.AREMV.EMVProcessorHelper.encodeTransactionTime(r26);
//            r25 = com.estel.AREMV.EMVProcessorHelper.createTlv(r25, r26);
//            r0 = r21;
//            r1 = r25;
//            r0.add(r1);
//            r19 = new com.estel.AREMV.MainActivity$8$1;
//            r0 = r19;
//            r1 = r30;
//            r0.<init>();
//            r25 = com.datecs.audioreader.emv.BerTlv.listToByteArray(r21);
//            r0 = r18;
//            r1 = r25;
//            r2 = r19;
//            r23 = r0.initEMVProcessing(r1, r2);
//            r16 = r32.performCardPowerOff();
//            r0 = r16;
//            r0 = r0.result;
//            r25 = r0;
//            if (r25 == 0) goto L_0x0834;
//        L_0x053c:
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "  Result: ";
//            r26.<init>(r27);
//            r0 = r16;
//            r0 = r0.result;
//            r27 = r0;
//            r27 = com.estel.AREMV.EMVProcessorHelper.getMessageResultDescription(r27);
//            r26 = r26.append(r27);
//            r27 = "\n";
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//        L_0x0560:
//            r22 = r23.getProcessingResult();
//            if (r22 != 0) goto L_0x08b3;
//        L_0x0566:
//            r24 = 0;
//            r25 = 90;
//            r0 = r23;
//            r1 = r25;
//            r24 = r0.getValue(r1);
//            r25 = com.estel.AREMV.EMVProcessorHelper.decodeNib(r24);
//            r26 = 0;
//            r27 = 4;
//            r11 = com.estel.AREMV.EMVProcessorHelper.getMaskedString(r25, r26, r27);
//            r25 = r11.length();
//            r25 = r25 + -4;
//            r26 = r11.length();
//            r0 = r25;
//            r1 = r26;
//            r14 = r11.substring(r0, r1);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "PAN VALUE LAST 4:";
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r14);
//            r26 = r26.toString();
//            r25.println(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r25;
//            r0.setPAN4(r14);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = new java.lang.StringBuilder;
//            r27 = "XXXX XXXX ";
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r14);
//            r26 = r26.toString();
//            r25.setCardNumber(r26);
//            r25 = com.estel.AREMV.EMVProcessorHelper.decodeNib(r24);
//            r26 = 6;
//            r27 = 0;
//            r12 = com.estel.AREMV.EMVProcessorHelper.getMaskedString(r25, r26, r27);
//            r25 = 0;
//            r26 = 6;
//            r0 = r25;
//            r1 = r26;
//            r13 = r12.substring(r0, r1);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "PAN VALUE FIRST 6:";
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r13);
//            r26 = r26.toString();
//            r25.println(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = new java.lang.StringBuilder;
//            r27 = java.lang.String.valueOf(r13);
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r14);
//            r26 = r26.toString();
//            r25.setPAN(r26);
//            r24 = 0;
//            r25 = 80;
//            r0 = r23;
//            r1 = r25;
//            r24 = r0.getValue(r1);
//            r5 = com.estel.AREMV.EMVProcessorHelper.decodeAscii(r24);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r25;
//            r0.setCardType(r5);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "APP LAVEL VALUE:";
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r5);
//            r26 = r26.toString();
//            r25.println(r26);
//            r24 = 0;
//            r25 = 24352; // 0x5f20 float:3.4124E-41 double:1.20315E-319;
//            r0 = r23;
//            r1 = r25;
//            r24 = r0.getValue(r1);
//            r8 = com.estel.AREMV.EMVProcessorHelper.decodeAscii(r24);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r25;
//            r0.setCardHolderName(r8);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "CARD HOLDRR VALUE:";
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r8);
//            r26 = r26.toString();
//            r25.println(r26);
//            r24 = 0;
//            r25 = 24356; // 0x5f24 float:3.413E-41 double:1.20335E-319;
//            r0 = r23;
//            r1 = r25;
//            r24 = r0.getValue(r1);
//            r7 = com.estel.AREMV.EMVProcessorHelper.decodeHex(r24);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r26 = 2;
//            r27 = 4;
//            r0 = r26;
//            r1 = r27;
//            r26 = r7.substring(r0, r1);
//            r0 = r26;
//            r1 = r25;
//            r1.ExpDate = r0;
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.ExpDate;
//            r26 = r0;
//            r27 = new java.lang.StringBuilder;
//            r26 = java.lang.String.valueOf(r26);
//            r0 = r27;
//            r1 = r26;
//            r0.<init>(r1);
//            r26 = 0;
//            r28 = 2;
//            r0 = r26;
//            r1 = r28;
//            r26 = r7.substring(r0, r1);
//            r0 = r27;
//            r1 = r26;
//            r26 = r0.append(r1);
//            r26 = r26.toString();
//            r0 = r26;
//            r1 = r25;
//            r1.ExpDate = r0;
//            r25 = 2;
//            r26 = 4;
//            r0 = r25;
//            r1 = r26;
//            r10 = r7.substring(r0, r1);
//            r25 = new java.lang.StringBuilder;
//            r26 = java.lang.String.valueOf(r10);
//            r25.<init>(r26);
//            r26 = "/";
//            r25 = r25.append(r26);
//            r10 = r25.toString();
//            r25 = new java.lang.StringBuilder;
//            r26 = java.lang.String.valueOf(r10);
//            r25.<init>(r26);
//            r26 = 0;
//            r27 = 2;
//            r0 = r26;
//            r1 = r27;
//            r26 = r7.substring(r0, r1);
//            r25 = r25.append(r26);
//            r10 = r25.toString();
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r26 = r0;
//            r0 = r26;
//            r0 = r0.ExpDate;
//            r26 = r0;
//            r25.setExpData(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r25;
//            r0.setExpirayDate(r10);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r26 = r0;
//            r0 = r26;
//            r0 = r0.TxnDt;
//            r26 = r0;
//            r25.setTxnDate(r26);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "EXPIRY DATE 1 VALUE:";
//            r26.<init>(r27);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r27 = r0;
//            r0 = r27;
//            r0 = r0.ExpDate;
//            r27 = r0;
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "EXPIRY DATE VALUE:";
//            r26.<init>(r27);
//            r0 = r26;
//            r26 = r0.append(r10);
//            r26 = r26.toString();
//            r25.println(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            r26 = "DONE";
//            r25.setEMVStatus(r26);
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r0 = r0.mdobj;
//            r25 = r0;
//            com.estel.AREMV.SwipeCardActivity.setMd(r25);
//            r25 = java.lang.System.out;
//            r26 = "Transaction: Successful:40736:40735";
//            r25.println(r26);
//            r24 = 0;
//            r25 = 40735; // 0x9f1f float:5.7082E-41 double:2.0126E-319;
//            r0 = r23;
//            r1 = r25;
//            r24 = r0.getValue(r1);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "Track 1 Data:";
//            r26.<init>(r27);
//            r27 = com.estel.AREMV.EMVProcessorHelper.decodeAscii(r24);
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r24 = 0;
//            r25 = 40736; // 0x9f20 float:5.7083E-41 double:2.01263E-319;
//            r0 = r23;
//            r1 = r25;
//            r24 = r0.getValue(r1);
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "Track 2 Data:";
//            r26.<init>(r27);
//            r27 = com.estel.AREMV.EMVProcessorHelper.decodeAscii(r24);
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r25 = java.lang.System.out;
//            r26 = "Transaction: Successful:40736:40735";
//            r25.println(r26);
//        L_0x0825:
//            r0 = r30;
//            r0 = com.estel.AREMV.MainActivity.this;
//            r25 = r0;
//            r0 = r25;
//            r1 = r23;
//            r0.showStatus(r1);
//            goto L_0x0373;
//        L_0x0834:
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "  Result: ";
//            r26.<init>(r27);
//            r0 = r16;
//            r0 = r0.result;
//            r27 = r0;
//            r27 = com.estel.AREMV.EMVProcessorHelper.getMessageResultDescription(r27);
//            r26 = r26.append(r27);
//            r27 = "\n";
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            r25 = r16.isCardAvailable();
//            if (r25 == 0) goto L_0x08aa;
//        L_0x085e:
//            r25 = java.lang.System.out;
//            r26 = "Card available\n";
//            r25.println(r26);
//            r25 = r16.isCardPowered();
//            if (r25 == 0) goto L_0x08a1;
//        L_0x086b:
//            r25 = java.lang.System.out;
//            r26 = "Card powered\n";
//            r25.println(r26);
//            r25 = r16.isUnknownProtocol();
//            if (r25 == 0) goto L_0x0881;
//        L_0x0878:
//            r25 = java.lang.System.out;
//            r26 = "Unknown protocol type\n";
//            r25.println(r26);
//            goto L_0x0560;
//        L_0x0881:
//            r25 = java.lang.System.out;
//            r26 = new java.lang.StringBuilder;
//            r27 = "Protocol type ";
//            r26.<init>(r27);
//            r27 = r16.getProtocol();
//            r26 = r26.append(r27);
//            r27 = "\n";
//            r26 = r26.append(r27);
//            r26 = r26.toString();
//            r25.println(r26);
//            goto L_0x0560;
//        L_0x08a1:
//            r25 = java.lang.System.out;
//            r26 = "Card not powered";
//            r25.println(r26);
//            goto L_0x0560;
//        L_0x08aa:
//            r25 = java.lang.System.out;
//            r26 = "Card not available";
//            r25.println(r26);
//            goto L_0x0560;
//        L_0x08b3:
//            r25 = java.lang.System.out;
//            r26 = "Transaction: Aborted";
//            r25.println(r26);
//            goto L_0x0825;
//            */
//            throw new UnsupportedOperationException("Method not decompiled: com.estel.AREMV.MainActivity.8.run(android.app.ProgressDialog, com.datecs.audioreader.AudioReader):void");
//        }
            }
        });
    }
}
