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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowTaskActivity extends AppCompatActivity {

    private RecyclerView taskList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        /** task list **/
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tasks");

        taskList = (RecyclerView) findViewById(R.id.task_list);
        taskList.setHasFixedSize(true);
        taskList.setLayoutManager(new LinearLayoutManager(this));

        /** bottom navigation bar **/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Task1, taskViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Task1, taskViewHolder>(
                Task1.class,
                R.layout.task_view,
                taskViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(taskViewHolder viewHolder, Task1 model, int position) {

                final String task_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setPostcode(model.getPostcode());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ShowTaskActivity.this, task_key, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ShowTaskActivity.this, TaskViewActivity.class );
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

        public void setTitle(String title){
            TextView task_title = (TextView) mView.findViewById(R.id.task_view_title);
            task_title.setText(title);
        }

        public void setDesc(String desc){
            TextView task_desc = (TextView) mView.findViewById(R.id.task_view_desc);
            task_desc.setText(desc);
        }

        public void setDate(String date){
            TextView task_date = (TextView) mView.findViewById(R.id.task_view_date);
            task_date.setText(date);
        }

        public void setTime(String time){
            TextView task_time = (TextView) mView.findViewById(R.id.task_view_time);
            task_time.setText(time);
        }

        public void setPostcode(String postcode){
            TextView task_postcode = (TextView) mView.findViewById(R.id.task_view_postcode);
            task_postcode.setText(postcode);
        }

    }




    /** Bottom navigation bar **/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(ShowTaskActivity.this, WorkingHomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_profile:
                    Intent intent1 = new Intent(ShowTaskActivity.this, ProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_help:
                    Intent intent2 = new Intent(ShowTaskActivity.this, HelpActivity.class);
                    startActivity(intent2);
                    break;
            }

            return true;
        }
    };
}
