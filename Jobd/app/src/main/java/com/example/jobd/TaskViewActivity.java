package com.example.jobd;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TaskViewActivity extends AppCompatActivity implements View.OnClickListener {

    private String task_key;
    private String title, desc, date, time, postcode, currentUserName, username, taskUserId, currentUserId, userImage, image;

    DatabaseReference task_info;

    ImageView imageView;
    TextView titleView, descView, dateView, timeView, postcodeView, nameView;
    EditText priceInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        imageView = (ImageView) findViewById(R.id.task_view_image);
        titleView = (TextView) findViewById(R.id.task_view_title);
        descView = (TextView) findViewById(R.id.task_view_desc);
        dateView = (TextView) findViewById(R.id.task_view_date);
        timeView = (TextView) findViewById(R.id.task_view_time);
        postcodeView = (TextView) findViewById(R.id.task_view_postcode);
        nameView = (TextView) findViewById(R.id.task_view_name);

        task_key = getIntent().getExtras().get("task_key").toString();
        task_info = FirebaseDatabase.getInstance().getReference();

        task_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title = dataSnapshot.child("Tasks").child(task_key).child("title").getValue().toString();
                desc = dataSnapshot.child("Tasks").child(task_key).child("description").getValue().toString();
                date = dataSnapshot.child("Tasks").child(task_key).child("date").getValue().toString();
                time = dataSnapshot.child("Tasks").child(task_key).child("time").getValue().toString();
                postcode = dataSnapshot.child("Tasks").child(task_key).child("postcode").getValue().toString();
                currentUserName = dataSnapshot.child("Users").child(currentUserId).child("name").getValue().toString();
                taskUserId = dataSnapshot.child("Tasks").child(task_key).child("userID").getValue().toString();
                username =  dataSnapshot.child("Users").child(taskUserId).child("name").getValue().toString();
                userImage = dataSnapshot.child("Users").child(taskUserId).child("image").getValue().toString();
                image = dataSnapshot.child("Users").child(currentUserId).child("image").getValue().toString();

                nameView.setText(username);
                titleView.setText(title);
                descView.setText(desc);
                dateView.setText(date);
                timeView.setText(time);
                postcodeView.setText(postcode);
                Picasso.get().load(userImage).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(TaskViewActivity.this, "halaa", Toast.LENGTH_LONG).show();

        priceInput = findViewById(R.id.task_input_price);
        findViewById(R.id.button_price_submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_price_submit:
                addOffer();
                break;
        }
    }

    private void addOffer () {

        final String price = priceInput.getText().toString().trim();

        if (price.isEmpty()) {
            priceInput.setError("forename required");
            priceInput.requestFocus();
            return;
        }

        Offer offer = new Offer(
                currentUserName,
                price,
                image
        );

        FirebaseDatabase.getInstance().getReference("Offers")
                .child(task_key)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(TaskViewActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TaskViewActivity.this, ChoiceActivity.class);
                    startActivity(intent);
                } else {
                    //display a failure message
                    Toast.makeText(TaskViewActivity.this, getString(R.string.registration_unsuccess), Toast.LENGTH_LONG).show();

                }
            }
        });


    }
}
