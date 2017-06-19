package com.example.francescop.cul8r;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private EditText editTextUserName;
    private EditText editTextPassword;
    Dialog loadingDialog;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        Button button = (Button) findViewById(R.id.buttonLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                if (username.equals(""))
                    Toast.makeText(getApplicationContext(),
                            "Insert an username!", Toast.LENGTH_LONG).show();
                else
                    new Login().execute(username,password);
            }
        });
    }

    private class Login extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(LoginActivity.this,
                    "Login", "Issuing the request...");
        }

        @Override
        protected String doInBackground(String... params) {
            String uname = params[0];
            String pass = params[1];

            Map<String,String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("username", uname);
            nameValuePairs.put("password", pass);

            ServiceHandler jsonParser = new ServiceHandler();
            String result = jsonParser.makeServiceCall("login.php", nameValuePairs);

            Log.i("***Response: ", "> " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if (result == null){
                Log.e("***Data", "Didn't receive any data from server!");
                Toast.makeText(getApplicationContext(),
                        "Server unreachable!", Toast.LENGTH_LONG).show();
            }
            else{
                String s = result.trim();
                if(s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),
                            "Success!", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getApplicationContext(),
                            "Username or Password not valid!", Toast.LENGTH_LONG).show();
            }
            if(loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
    }
}