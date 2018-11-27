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
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button bCall;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bCall = findViewById(R.id.button);
        bCall.setOnClickListener(this);
        tvLog = findViewById(R.id.textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String ussdCode = "*" + "124" + Uri.encode("#");
                //startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));

                TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                } else {
                    manager.sendUssdRequest("*124#", new TelephonyManager.UssdResponseCallback() {
                        @Override
                        public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                            Log.e("DEBUG", response.toString());
                            tvLog.setText(response.toString());
                        }

                        @Override
                        public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                            Log.e("DEBUG", Integer.toString(failureCode));
                        }
                    }, new Handler());
                }
                break;
        }
    }
}
