package com.example.jobd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class WorkingHomeActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_home);

        button = (Button) findViewById(R.id.buttonShowTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShowTaskActivity();
            }
        });

        /** bottom navigation bar **/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void openShowTaskActivity(){
        Intent intent = new Intent(this, ShowTaskActivity.class );
        startActivity(intent);
    }

    /** Bottom navigation bar **/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(WorkingHomeActivity.this, WorkingHomeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_profile:
                    Intent intent1 = new Intent(WorkingHomeActivity.this, ProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_help:
                    Intent intent2 = new Intent(WorkingHomeActivity.this, HelpActivity.class);
                    startActivity(intent2);
                    break;
            }

            return true;
        }
    };

}
