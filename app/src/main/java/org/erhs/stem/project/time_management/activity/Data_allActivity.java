package org.erhs.stem.project.time_management.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.erhs.stem.project.time_management.R;

public class Data_allActivity extends AppCompatActivity {
    private ImageButton ImBtnAlldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

       ImBtnAlldata = findViewById(R.id.ImBtnAlldata);
       ImBtnAlldata.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) { Intent intent = new Intent(Data_allActivity.this, StatisticsActivity.class);
               startActivity(intent);
           }
       });
    }
}

