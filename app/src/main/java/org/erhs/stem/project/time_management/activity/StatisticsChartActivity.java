package org.erhs.stem.project.time_management.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.domain.EventType;
import org.erhs.stem.project.time_management.service.EventRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StatisticsChartActivity extends AppCompatActivity {

    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_statistics);

        chart = findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);

        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -7);

        Calendar to = Calendar.getInstance();

        EventRepository
                .getEventsByDateRange(getApplicationContext(), from.getTime(), to.getTime())
                .observe(this, new Observer<List<Event>>() {
                    @Override
                    public void onChanged(List<Event> events) {
                        List<PieEntry> pieEntries = new ArrayList<>();

                        Map<EventType, Integer> countByEventType = new EnumMap<>(EventType.class);
                        for (Event event : events) {
                            if (!countByEventType.containsKey(event.type)) {
                                countByEventType.put(event.type, 0);
                            }
                            countByEventType.put(event.type, countByEventType.get(event.type) + 1);
                        }

                        for (Map.Entry<EventType, Integer> entry : countByEventType.entrySet()) {
                            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey().name()));
                        }

                        PieDataSet pieDataSet = new PieDataSet(pieEntries, getString(R.string.last_week));
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        chart.setData(new PieData(pieDataSet));
                    }
                });
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
