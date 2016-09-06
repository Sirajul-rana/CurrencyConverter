package com.destroinc.currencyconverter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by siraj on 03-Apr-16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String PROCESS_RESPONSE = "com.destroinc.currencyconverter.intent.action.PROCESS_RESPONSE";
    public EditText output;
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            String result = intent.getStringExtra(ConverterService.RESPONSE_MESSAGE);
            Intent i = new Intent(context, CurrencyConverterActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("message", result);
            context.sendBroadcast(i);
            if (CurrencyConverterActivity.mThis !=null)
            {
                ((EditText)CurrencyConverterActivity.mThis.findViewById(R.id.editText2)).setText(result);
            }
            Log.d("Broadcast",intent.getStringExtra(ConverterService.RESPONSE_MESSAGE));
        }
        catch (Exception e){
        }

    }
}
