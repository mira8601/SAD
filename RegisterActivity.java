package com.example.umtouch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private EditText email, password;
    Button register;
    TextView alreadyAccount;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.newEmail_input);
        password = findViewById(R.id.newPassword_input);
        alreadyAccount = findViewById(R.id.log_text);
        register = findViewById(R.id.register_button);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);

        /*TextView btn = findViewById(R.id.alreadyAccount_text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptRegistration();
            }
        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);//intent for go to other class
                startActivity(intent);
            }
        });
    }

    private void AtemptRegistration() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if(emailText.isEmpty() || !emailText.toLowerCase().contains("um.edu.my")){
            showError(email, "Email is not valid");
        } else if (passwordText.isEmpty() || passwordText.length()<6) {
            showError(password, "Password must be greater than 6 letters");
        }else {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mLoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration is successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        mLoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }

    private void showError(EditText field, String text) {
        field.setError(text);
        field.requestFocus();

    }
}
