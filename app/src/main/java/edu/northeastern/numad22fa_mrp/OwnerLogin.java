package edu.northeastern.numad22fa_mrp;

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
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class OwnerLogin extends AppCompatActivity {

    TextView register;
    Button login;
    EditText email, password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.tv_registerButton);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerLogin.this, OwnerRegister.class));
            }
        });

    }

    private void login() {
        String emailStr = this.email.getText().toString();
        String passwordStr = this.password.getText().toString();

        if (emailStr.isEmpty()) {
            this.email.setError("Please Enter Email");
        } else if (!pattern.matcher(emailStr).matches()) {
            this.email.setError("Please Enter a valid Email");
        } else if (passwordStr.isEmpty()) {
            this.password.setError("Please Enter Password");
        }
        //TODO
        else if (passwordStr.length() < 6) {
            this.password.setError("Password should be more than six characters");
        } else {
            progressDialog.setTitle("Logging In");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        navigateOwnerToHomePage();
                        Toast.makeText(OwnerLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OwnerLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void navigateOwnerToHomePage() {
        startActivity(new Intent(OwnerLogin.this, PropertyList.class));

    }
}