package com.hs.monagcmsample;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hs.mona.GCM;

public class MainActivity extends AppCompatActivity {

    TextView mGCMToken;
    TextView mStatus;
    private boolean subscribed;
    Button mSubscribeButton;
    ProgressDialog progressDialog;
    GCM gcm;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mGumTokenReceiver, new IntentFilter(GCM.GCM_TOKEN_BROADCAST_INTENT));
    }

    BroadcastReceiver mGumTokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("GCM Token", intent.getExtras().getString(GCM.TOKEN));
            mGCMToken.setText("GCM Token :" + intent.getExtras().getString(GCM.TOKEN) + " (Copy the token from log cat)");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGCMToken = (TextView) findViewById(R.id.tv_reg_token);
        mStatus = (TextView) findViewById(R.id.tv_status);
        mSubscribeButton = (Button) findViewById(R.id.subscribe_btn);
        gcm = GCM.getInstance(this);

        String token = gcm.getGCMToken();
        if (token != null) {
            Log.d("GCM Token", token);
            mGCMToken.setText("GCM Token : " + token);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGumTokenReceiver);
    }

    public void initializeGCM(View view) {
        //Call this method to generate token
        gcm.generateToken();
        progressDialog = ProgressDialog.show(this, "Please wait", "Generating GCM Token");
    }

    public void subscribe(View view) {
        if (gcm.getGCMToken() == null) {
            mStatus.setText("GCM is not initialized. Please initialize before subscribing.");
            return;
        }
        if (subscribed) {
            gcm.unSubscribe("sample");
            mStatus.setText("");
            subscribed = false;
            mSubscribeButton.setText(getString(R.string.subscribe));
        } else {
            gcm.subscribe("sample");
            mStatus.setText("You are now subscribed to topic : sample, send the GCM push via rest client.");
            subscribed = true;
            mSubscribeButton.setText(getString(R.string.unsubscribe));
        }
    }
}
