package com.example.shoaibsaikat.ussdtester;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "USSD";

    private Button bCall;
    private TextView tvLog;
    private EditText etUSSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bCall = findViewById(R.id.button);
        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Call button clicked, please wait for response...");
                tvLog.setText("Call button clicked, please wait for response...");
                String USSDCode = etUSSD.getText().toString();
                if (USSDCode == null || USSDCode == "") {
                    Log.e(LOG_TAG, "USSD code not found");
                    tvLog.setText("USSD code not found");
                    return;
                }

                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    Log.e(LOG_TAG, "CALL_PHONE permission failure");
                    tvLog.setText("Consider giving call permission to this app from settings > app menu");
                    return;
                } else {
                    TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    manager.sendUssdRequest(USSDCode, new TelephonyManager.UssdResponseCallback() {
                        @Override
                        public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                            Log.i(LOG_TAG, response.toString());
                            tvLog.setText(response.toString());
                        }

                        @Override
                        public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                            Log.e(LOG_TAG, Integer.toString(failureCode));
                            tvLog.setText(Integer.toString(failureCode));
                        }
                    }, new Handler());
                }
            }
        });
        tvLog = findViewById(R.id.textView);
        etUSSD = findViewById(R.id.editTextUSSD);
    }
}
