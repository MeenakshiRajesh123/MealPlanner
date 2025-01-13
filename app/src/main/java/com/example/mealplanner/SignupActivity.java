/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.os.Bundle;
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

public class SignupActivity extends AppCompatActivity {

    EditText editTextUsernameSignUp;
    EditText editTextPasswordSignUp;

    Button signupButton;
    ImageButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        returnButton = findViewById(R.id.returnButton);
        signupButton = findViewById(R.id.signupUser);
        editTextPasswordSignUp = findViewById(R.id.editTextPasswordSignUp);
        editTextUsernameSignUp = findViewById(R.id.editTextUsernameSignUp);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsernameSignUp.getText().toString().trim();
                String password = editTextPasswordSignUp.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    new SignUpTask(SignupActivity.this).execute(username, password);
                } else {
                    Toast.makeText(SignupActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}