package com.destroinc.currencyconverter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class CurrencyConverterActivity extends AppCompatActivity implements View.OnClickListener{


    private StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    private static RadioButton radioButton1;
    private static RadioButton radioButton2;
    private EditText input;
    public EditText output;
    private static Button convert;
    private static Button reconnect;
    private Intent service;
    MyBroadcastReceiver broadcastReceiver;
    public static CurrencyConverterActivity mThis = null;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetworkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_currency_converter);

        StrictMode.setThreadPolicy(policy);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        input = (EditText) findViewById(R.id.editText);
        output = (EditText) findViewById(R.id.editText2);
        convert = (Button) findViewById(R.id.button);
        reconnect = (Button) findViewById(R.id.reconnect);

        service = new Intent(getApplicationContext(), ConverterService.class);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (radioButton1.isChecked()) {
                            service.putExtra("choice", "0");
                            service.putExtra("value", CurrencyConverterActivity.this.input.getText().toString());
                            startService(service);

                        } else if (radioButton2.isChecked()) {

                            service.putExtra("choice", "1");
                            service.putExtra("value", CurrencyConverterActivity.this.input.getText().toString());
                            startService(service);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please select a conversion option.", Toast.LENGTH_SHORT).show();
                        }


                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reconnect.setOnClickListener(this);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        isConnected(activeNetworkInfo);
        IntentFilter filter = new IntentFilter(MyBroadcastReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastReceiver = new MyBroadcastReceiver();
        this.registerReceiver(broadcastReceiver, filter);



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThis=this;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
       // EditText editText = (EditText)v;
        if (button==reconnect){
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            isConnected(activeNetworkInfo);

        }
        try {
            if (button == convert) {

                if (radioButton1.isChecked()) {
                        service.putExtra("choice", "0");
                        service.putExtra("value", CurrencyConverterActivity.this.input.getText().toString());
                        startService(service);

                } else if (radioButton2.isChecked()) {

                        service.putExtra("choice", "1");
                        service.putExtra("value", CurrencyConverterActivity.this.input.getText().toString());
                        startService(service);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please select a conversion option.", Toast.LENGTH_SHORT).show();
                }

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void isConnected(NetworkInfo activeNetworkInfo){
        if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            Toast.makeText(getApplicationContext(),"Network is available",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Network is not available",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mThis=null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (broadcastReceiver == null)
        {
            Log.d("MSG","Do not unregister receiver as it was never registered");
        }
        else
        {
            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver == null)
        {
            Log.d("MSG","Do not unregister receiver as it was never registered");
        }
        else
        {
            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver=null;
        }
    }
}