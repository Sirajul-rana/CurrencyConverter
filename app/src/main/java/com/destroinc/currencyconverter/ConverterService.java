package com.destroinc.currencyconverter;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Created by siraj on 03-Apr-16.
 */
public class ConverterService extends IntentService {

    private HttpURLConnection urlConnection;
    private InputStreamReader ins;
    public static final String RESPONSE_MESSAGE = "myResponseMessage";


    public ConverterService() {
        super("ConverterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Converter Service", "Service Started");
        String choice=intent.getStringExtra("choice");
        String second=intent.getStringExtra("value");
        String value=null,result=null;

        try {
            URL url = new URL("http://hrhafiz.com/converter/");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            ins=new InputStreamReader(in);
            Log.d("Converter Service", "Internet Connected");
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        try {
            value = readStream(ins);
            Log.d("Converter Service", "Line reading");

        } catch (Exception e) {
            // TODO: handle exception
        }
        finally
        {
            urlConnection.disconnect();
            Log.d("Converter Service", "Internet Disconnected");
        }

        String[] in2 = value.split(",");

        try {
            switch (choice)
            {
                case "0":{
                    String digit=in2[Integer.parseInt(choice)].replaceAll("[^0-9?!\\.]","");
                    result = (Double.parseDouble(digit)*Double.parseDouble(second))+"";
                    Log.d("Converter Service", result);

                }
                break;
                case "1":{
                    String digit=in2[Integer.parseInt(choice)].replaceAll("[^0-9?!\\.]","");
                    result = (Double.parseDouble(digit)*Double.parseDouble(second))+"";
                    Log.d("Converter Service", result);

                }
                break;
                default:

                    break;
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }


        try {
            Intent intentResponse = new Intent();
            intentResponse.setAction(MyBroadcastReceiver.PROCESS_RESPONSE);
            intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
            intentResponse.putExtra(RESPONSE_MESSAGE, result);
            sendBroadcast(intentResponse);
            Log.d("Converter Service", "send");
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        }

    }
    private String readStream(InputStreamReader ins) {
        try {

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int data = ins.read();
            while (data != -1) {
                bo.write(data);
                data = ins.read();
            }


            return bo.toString();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return "";

        }
        finally {
            urlConnection.disconnect();
            Log.d("Converter Service", "Internet disconnected");

        }
    }
}
