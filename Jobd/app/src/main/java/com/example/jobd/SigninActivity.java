package com.example.jobd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.ProgressDialog;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone, editTextSurname, editTextDob, editTextAddress, editTextPostcode, editTextGender;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextName = findViewById(R.id.edit_text_name);
        editTextSurname = findViewById(R.id.edit_text_surname);
        //editTextGender = findViewById(R.id.edit_text_gender);
        editTextDob = findViewById(R.id.edit_text_dob);
        editTextPostcode = findViewById(R.id.edit_text_postcode);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhone = findViewById(R.id.edit_text_phone);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
            //Intent i = new Intent(SigninActivity.this, LoginActivity.class);
            //startActivity(i);
        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();
        //final String gender = editTextGender.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String dob = editTextDob.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final String postcode = editTextPostcode.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();


        progressDialog = new ProgressDialog(this);
        progressBar = findViewById(R.id.progressbar);
        //progressBar.setVisibility(View.GONE);

        if (name.isEmpty()) {
            editTextName.setError("forename required");
            editTextName.requestFocus();
            return;
        }

        if (surname.isEmpty()) {
            editTextSurname.setError(getString(R.string.input_error_surname));
            editTextSurname.requestFocus();
            return;
        }

        if (dob.length() > 10) {
            editTextDob.setError(getString(R.string.input_error_dob));
            editTextDob.requestFocus();
            return;
        }

        if(dob.isEmpty()) {
            editTextDob.setError(getString(R.string.input_error_dob_required));
            editTextDob.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError(getString(R.string.input_error_phone));
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextPhone.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            editTextAddress.setError(getString(R.string.input_error_address_required));
            editTextAddress.requestFocus();
            return;
        }

        if (postcode.isEmpty()) {
            editTextPostcode.setError(getString(R.string.input_error_postcode_required));
            editTextPostcode.requestFocus();
            return;
        }

        progressDialog.setMessage("Creating account");
        progressDialog.show();
        //progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    surname,
                                    dob,
                                    email,
                                    phone,
                                    address,
                                    postcode
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    //progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SigninActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                        Toast.makeText(SigninActivity.this, getString(R.string.registration_unsuccess), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(SigninActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }
}
