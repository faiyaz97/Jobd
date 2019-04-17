package com.example.jobd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HiringTaskViewActivity extends AppCompatActivity {

    private String task_key;
    private String title, desc, date, time, postcode, userName, image, userID;

    DatabaseReference mDatabase, mDatabaseOff;

    TextView titleView, descView, dateView, timeView, postcodeView;
    ImageView imageView;

    private RecyclerView taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiring_task_view);

        imageView = (ImageView) findViewById(R.id.task_view_image);
        titleView = (TextView) findViewById(R.id.task_view_title);
        descView = (TextView) findViewById(R.id.task_view_desc);
        dateView = (TextView) findViewById(R.id.task_view_date);
        timeView = (TextView) findViewById(R.id.task_view_time);
        postcodeView = (TextView) findViewById(R.id.task_view_postcode);

        task_key = getIntent().getExtras().get("task_key").toString();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseOff = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title = dataSnapshot.child("Tasks").child(task_key).child("title").getValue().toString();
                desc = dataSnapshot.child("Tasks").child(task_key).child("description").getValue().toString();
                date = dataSnapshot.child("Tasks").child(task_key).child("date").getValue().toString();
                time = dataSnapshot.child("Tasks").child(task_key).child("time").getValue().toString();
                postcode = dataSnapshot.child("Tasks").child(task_key).child("postcode").getValue().toString();
                userName = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString();


                titleView.setText(title);
                descView.setText(desc);
                dateView.setText(date);
                timeView.setText(time);
                postcodeView.setText(postcode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(HiringTaskViewActivity.this, task_key, Toast.LENGTH_LONG).show();

        /** task list **/

        mDatabaseOff = FirebaseDatabase.getInstance().getReference().child("Offers").child(task_key);

        taskList = (RecyclerView) findViewById(R.id.task_list);
        //taskList.setHasFixedSize(true);
        taskList.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Offer, HiringTaskViewActivity.taskViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Offer, HiringTaskViewActivity.taskViewHolder>(
                Offer.class,
                R.layout.offer_view,
                HiringTaskViewActivity.taskViewHolder.class,
                mDatabaseOff
        ) {
            @Override
            protected void populateViewHolder(HiringTaskViewActivity.taskViewHolder viewHolder, Offer model, int position) {

                final String task_key = getRef(position).getKey();
                viewHolder.setUserName(model.getUserName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setUserImage(model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ShowTaskActivity.this, task_key, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HiringTaskViewActivity.this, TaskViewActivity.class );
                        intent.putExtra("task_key", task_key);
                        startActivity(intent);
                    }
                });

            }
        };
        taskList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class taskViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public taskViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUserImage(String image) {
            ImageView offer_image = (ImageView) mView.findViewById(R.id.task_view_image);
            Picasso.get().load(image).into(offer_image);

        }
        public void setUserName(String username){
            TextView offer_username = (TextView) mView.findViewById(R.id.task_view_name);
            offer_username.setText(username);
        }

        public void setPrice(String price){
            TextView offer_price = (TextView) mView.findViewById(R.id.task_view_price);
            offer_price.setText("£" + price);
        }


    }



    /** Bottom navigation bar **/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(HiringTaskViewActivity.this, WorkingHomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_profile:
                    Intent intent1 = new Intent(HiringTaskViewActivity.this, ProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_help:
                    Intent intent2 = new Intent(HiringTaskViewActivity.this, HelpActivity.class);
                    startActivity(intent2);
                    break;
            }

            return true;
        }
    };



}
