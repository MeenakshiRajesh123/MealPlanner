/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;

    Button loginButton;
    ImageButton returnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        loginButton = findViewById(R.id.loginUser);
        returnButton = (ImageButton)findViewById(R.id.returnButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    //new LogInTask(LoginActivity.this).execute(username, password);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            InputStream inputStream = QueryHelper.EstablishConnection("?Action=LoginUser&User=" + username + "&Password=" + password);
                            String responseToken = QueryHelper.BuildString(inputStream);
                            Log.d ("Stuff", responseToken);
                            if (responseToken.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("sessionToken", responseToken);
                                editor.apply();
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    // Finish the activity when the background task is done
                                    finish();
                                }
                            });
                        }
                    });
                    thread.start();
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}