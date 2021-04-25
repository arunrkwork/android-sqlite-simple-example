package com.agri.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edName, edPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edName = findViewById(R.id.edName);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnLogin) {
            String uname = edName.getText().toString().trim();
            String pass = edPassword.getText().toString().trim();
            login(uname, pass);
        }
    }

    private void login(String uname, String pass) {
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute("login", uname, pass);
    }
}