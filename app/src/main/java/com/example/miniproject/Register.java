package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText name, email, password;
    String username, useremail, userpassword;
    Button registerButton;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering!");
        progressDialog.setMessage("Please wait while we register your details!!");
        progressDialog.setCanceledOnTouchOutside(false);

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        registerButton = findViewById(R.id.signup);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = name.getText().toString();
                useremail = email.getText().toString();
                userpassword = password.getText().toString();

                if (username.isEmpty()){
                    name.setError("Required!");
                    name.requestFocus();
                }
                else if (useremail.isEmpty()){
                    email.setError("Required!");
                    email.requestFocus();
                }
                else if (userpassword.isEmpty()){
                    name.setError("Required!");
                    name.requestFocus();
                }else if (userpassword.length()<=8){
                    password.setError("Minimum 8 character required!");
                    password.requestFocus();
                }else {
                    progressDialog.show();
                    userRegister(useremail, userpassword);
                }
            }
        });
    }

    private void userRegister(String useremail, String userpassword) {
        auth.createUserWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            UserData userData = new UserData(
                                    username,
                                    useremail,
                                    System.currentTimeMillis()+""
                            );
                            reference
                                    .child("personal_details")
                                    .child(userData.getId())
                                    .setValue(userData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(Register.this,"Registered",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Register.this,LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}