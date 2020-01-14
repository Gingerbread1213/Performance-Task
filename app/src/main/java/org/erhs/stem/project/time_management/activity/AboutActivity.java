package org.erhs.stem.project.time_management.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.service.ApplicationMonitor;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.setTheme(getApplicationContext(), this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_about);

        ApplicationMonitor.getInstance(getApplicationContext()).getActivityRepository().add(this);
    }

    @Override
    protected void onDestroy() {
        ApplicationMonitor.getInstance(getApplicationContext()).getActivityRepository().remove(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
