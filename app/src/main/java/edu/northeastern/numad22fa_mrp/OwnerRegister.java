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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class OwnerRegister extends AppCompatActivity {

    public static final String OWNER = "Owner";
    public static final String HOUSES = "houses";

    TextView login;
    Button register;
    EditText email, password, confirmPassword, username,phoneNumber;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    Pattern pattern = Pattern.compile(emailRegex);
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);

        username = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        confirmPassword = findViewById(R.id.et_confirmPassword);
        register = findViewById(R.id.btn_register);
        login = findViewById(R.id.tv_loginButton);
        phoneNumber = findViewById(R.id.et_phoneNumber);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerRegister.this, OwnerLogin.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });

    }

    private void authenticate() {
        String emailStr = this.email.getText().toString();
        String passwordStr = this.password.getText().toString();
        String confirmPasswordStr = this.confirmPassword.getText().toString();
        String username = this.username.getText().toString();
        String phone = this.phoneNumber.getText().toString();

        if (emailStr.isEmpty()) {
            this.email.setError("Please Enter Email");
        } else if (!pattern.matcher(emailStr).matches()) {
            this.email.setError("Please Enter a valid Email");
        }  else if(phone.length() != 10){
            this.phoneNumber.setError("Please Enter a 10 digit phone number");
        }else if (passwordStr.isEmpty()) {
            this.password.setError("Please Enter Password");
        }
        else if (passwordStr.length() < 6) {
            this.password.setError("Password should be more than six characters");
        } else if (!confirmPasswordStr.equals(passwordStr)) {
            this.confirmPassword.setError("Password doesn't matches");
        } else {
            progressDialog.setTitle("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userId = firebaseUser.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(OwnerRegister.OWNER).child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("email",emailStr);
                        hashMap.put("phoneNumber",phone);
                        hashMap.put("id", userId);
                        hashMap.put("username", username);
                        hashMap.put("imageUrl", "default");
                        hashMap.put("search", username.toLowerCase());

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    navigateOwnerToHomePage();
                                }
                            }
                        });


                        Toast.makeText(OwnerRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch(FirebaseAuthUserCollisionException e) {

                            Toast.makeText(getApplicationContext(), "Email already taken!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(OwnerRegister.this, "Email Already Existing, please Log In", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void navigateOwnerToHomePage() {
        Intent intent = new Intent(OwnerRegister.this, PropertyList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}