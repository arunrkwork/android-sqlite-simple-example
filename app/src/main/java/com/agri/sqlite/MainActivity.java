package com.agri.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    DatabaseHelper db;
    EditText edName, edSurName, edMark, edId;
    Button btnAdd, btnUpdate, btnDelete, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        edName = findViewById(R.id.edName);
        edSurName = findViewById(R.id.edSurName);
        edMark = findViewById(R.id.edMark);
        edId = findViewById(R.id.edId);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        btnView = findViewById(R.id.btnView);
        btnView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAdd) {
            addDetails();
        } else if (id == R.id.btnUpdate) {
            updateDetails();
        } else if (id == R.id.btnDelete) {
            deleteItem();
        } else if (id == R.id.btnView) {
            viewDetails();
        }
    }

    private void viewDetails() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            showAlertDialog("Error", "Data not found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("SurName: " + res.getString(2) + "\n");
            buffer.append("Mark: " + res.getString(3) + "\n\n");
        }
        showAlertDialog("Data", buffer.toString());
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void deleteItem() {
        String id = edId.getText().toString().trim();
        int result = db.deleteData(id);
        Log.d(TAG, "deleteItem: " + result);
        // result is greater than 0 then the item will deleted else unsuccessful
        if (result > 0)
            Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Not Deleted", Toast.LENGTH_SHORT).show();
        clearAll();
    }

    private void updateDetails() {
        String name = edName.getText().toString().trim();
        String surname = edSurName.getText().toString().trim();
        String mark = edMark.getText().toString().trim();
        String id = edId.getText().toString().trim();
        boolean result = db.updateData(id, name, surname, mark);
        Log.d(TAG, "updateDetails: " + result);
        clearAll();
    }

    private void clearAll() {
        edId.setText("");
        edName.setText("");
        edSurName.setText("");
        edMark.setText("");
    }

    private void addDetails() {
        String name = edName.getText().toString().trim();
        String surname = edSurName.getText().toString().trim();
        String mark = edMark.getText().toString().trim();
        boolean result = db.insertData(name, surname, mark);
        Log.d(TAG, "addDetails: " + result);
        clearAll();
    }

}