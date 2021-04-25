package com.agri.sqlite;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "BackgroundTask";

    String loginUrl = "https://vcw.ezrasoftwaresolutions.com/login.php";
    Context ctx;
    ProgressDialog progressDialog;
    AppCompatActivity activity;
    AlertDialog.Builder alertDialogBuilder;

    public BackgroundTask(Context ctx) {
        this.ctx = ctx;
        this.activity = (AppCompatActivity) ctx;
        Log.d(TAG, "BackgroundTask: ");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialogBuilder = new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Connecting to server");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
        Log.d(TAG, "onPreExecute: ");
    }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        if (method.equals("login")) {
            try {
                URL url = new URL(loginUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String name, pass;
                name = params[1];
                pass = params[2];
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                httpURLConnection.disconnect();
                Thread.sleep(5000);
                String result = stringBuilder.toString().trim();
                Log.d(TAG, "doInBackground: " + result);
                return result;
            } catch (MalformedURLException e) {
                Log.d(TAG, "doInBackground: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: " + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);

        try {
            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            JSONObject jo = jsonArray.getJSONObject(0);
            String code = jo.getString("code");
            String message = jo.getString("message");

            if (code.equals("login_true")) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                Log.d(TAG, "onPostExecute: " + code + " --- " + message);
            } else {
                Log.d(TAG, "onPostExecute: login failed");
                Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.d(TAG, "onPostExecute: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
