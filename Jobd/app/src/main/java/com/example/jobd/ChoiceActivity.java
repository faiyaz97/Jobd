package com.example.jobd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {

    private Button button, button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        button = (Button) findViewById(R.id.buttonHiringHome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHiringHomeActivity();
            }
        });

        button1 = (Button) findViewById(R.id.buttonWorkingHome);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWorkingHomeActivity();
            }
        });
    }

    public void openHiringHomeActivity(){
        Intent intent = new Intent(this, HiringHomeActivity.class );
        startActivity(intent);
    }

    public void openWorkingHomeActivity(){
        Intent intent = new Intent(this, WorkingHomeActivity.class );
        startActivity(intent);
    }
}
