package com.example.francescop.cul8r;

/**
 * Created by francescop on 18/05/2017.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;

public class ServiceHandler {
    static InputStream is = null;
    static String response = null;
    static String Server_url = "http://fpculcasi.altervista.org";

    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key),"UTF-8");
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0) sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    public String makeServiceCall(String my_url, Map<String,String> params) {
        response=null;
        //Encoded String - we will have to encode string by our custom method
        String encodedStr = getEncodedData(params);

        BufferedReader reader = null;
        HttpURLConnection con = null;

        try {
            //Converting address String to URL
            URL url = new URL(Server_url + "/" + my_url);
            //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
            con = (HttpURLConnection) url.openConnection();

            //Post Method
            con.setRequestMethod("POST");
            //To enable inputting values using POST method
            //(Basically, after this we can write the params to the body of POST method)
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            //Writing params to outputstreamwriter
            writer.write(encodedStr);
            //Sending the data to the server
            writer.flush();

            //Data Read Procedure - Reading the data comming line by line
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) { //Read until there is something available
                sb.append(line + "\n"); //Reading and saving line by line - not all at once
            }
            response = sb.toString(); //Saving complete data received in string, you can do it differently

            //Check to the values received in Logcat
            Log.i("custom_check","The values received in the store part are as follows:");
            Log.i("custom_check",line);

            reader.close();

            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}