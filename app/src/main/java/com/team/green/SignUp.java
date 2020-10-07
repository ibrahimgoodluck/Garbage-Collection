package com.team.green;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.team.green.models.User;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    Button regTologin,register;
    EditText emailTxt, fullnameTxt, passwordTxt, phoneTxt;
    private String fullname, password, email, phone;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        regTologin = findViewById(R.id.txtRegToLogin);
        register = findViewById(R.id.btnRegister);
        fullnameTxt = findViewById(R.id.txtFullName);
        emailTxt = findViewById(R.id.input_email);
        passwordTxt = findViewById(R.id.input_password);
        phoneTxt = findViewById(R.id.inputPhone);

        regTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = emailTxt.getText().toString();
                fullname = fullnameTxt.getText().toString();
                password = passwordTxt.getText().toString();
                phone = phoneTxt.getText().toString();

                if (email.isEmpty()){
                    emailTxt.setError("Email Required");
                    emailTxt.requestFocus();
                    return;
                }

                if (fullname.isEmpty()){
                    fullnameTxt.setError("Full Required");
                    fullnameTxt.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    passwordTxt.setError("Password Required");
                    passwordTxt.requestFocus();
                    return;
                }

                if (password.length() < 6){
                    passwordTxt.setError("6 or more characters are required");
                    passwordTxt.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailTxt.setError("Enter a valid email");
                    emailTxt.requestFocus();
                    return;
                }

                if(phone.isEmpty()){
                    phoneTxt.setError("Phone number required");
                    phoneTxt.requestFocus();
                    return;

                }

                if(phone.length() < 10){
                    phoneTxt.setError("Enter a valid phone number");
                    phoneTxt.requestFocus();
                    return;

                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    updateUI(user, email, fullname, password, phone, "customer");

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUp.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });


            }
        });
    }

    private void updateUI(FirebaseUser user, String email, String fullname, String password,
                          String phone, String role) {

        User user1 = new User(
                user.getUid(),
                fullname,
                phone,
                email,
                role
        );

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignUp.this, "Login to Continue", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Registration failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isNull(String string) { return string.equals("");}
}