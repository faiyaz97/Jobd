package com.example.jobd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText titleTask, descTask, dateTask, timeTask, addressTask, postcodeTask;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private int taskCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        titleTask = findViewById(R.id.task_title);
        descTask = findViewById(R.id.task_desc);
        dateTask = findViewById(R.id.task_date);
        timeTask = findViewById(R.id.task_time);
        addressTask = findViewById(R.id.task_address);
        postcodeTask = findViewById(R.id.task_postcode);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_task_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_task_submit:
                addTask();
                break;
        }
    }

    private void addTask() {
        final String title = titleTask.getText().toString().trim();
        final String desc = descTask.getText().toString().trim();
        final String date = dateTask.getText().toString().trim();
        final String time = timeTask.getText().toString().trim();
        final String address = addressTask.getText().toString().trim();
        final String postcode = postcodeTask.getText().toString().trim();
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



        progressDialog = new ProgressDialog(this);
        progressBar = findViewById(R.id.progressbar);
        //progressBar.setVisibility(View.GONE);

        if (title.isEmpty()) {
            titleTask.setError("forename required");
            titleTask.requestFocus();
            return;
        }

        if (desc.isEmpty()) {
            descTask.setError(getString(R.string.input_error_surname));
            descTask.requestFocus();
            return;
        }

        if (date.length() > 10) {
            dateTask.setError(getString(R.string.input_error_dob));
            dateTask.requestFocus();
            return;
        }

        if(date.isEmpty()) {
            dateTask.setError(getString(R.string.input_error_dob_required));
            dateTask.requestFocus();
            return;
        }

        if(time.isEmpty()) {
            timeTask.setError(getString(R.string.input_error_dob_required));
            timeTask.requestFocus();
            return;
        }


        if (address.isEmpty()) {
            addressTask.setError(getString(R.string.input_error_address_required));
            addressTask.requestFocus();
            return;
        }

        if (postcode.isEmpty()) {
            postcodeTask.setError(getString(R.string.input_error_postcode_required));
            postcodeTask.requestFocus();
            return;
        }

        progressDialog.setMessage("Creating task");
        progressDialog.show();

        Task1 task1 = new Task1(
                title,
                desc,
                date,
                time,
                address,
                postcode,
                userID
        );

        FirebaseDatabase.getInstance().getReference("Tasks")
                .push()
                .setValue(task1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                //progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(NewTaskActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewTaskActivity.this, ChoiceActivity.class);
                    startActivity(intent);
                } else {
                    //display a failure message
                    Toast.makeText(NewTaskActivity.this, getString(R.string.registration_unsuccess), Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
