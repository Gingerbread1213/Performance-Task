package org.erhs.stem.project.time_management.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_statistics);

        createPieChart();
        createBarChart();
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

    private void createPieChart() {
        pieChart = findViewById(R.id.pie_chart);
        pieChart.getDescription().setEnabled(false);

        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -6);

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

                        pieChart.setData(new PieData(pieDataSet));
                    }
                });
    }

    private void createBarChart() {
        barChart = findViewById(R.id.bar_chart);
        barChart.getDescription().setEnabled(false);

        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -7);

        Calendar to = Calendar.getInstance();

        EventRepository
                .getEventsByDateRange(getApplicationContext(), from.getTime(), to.getTime())
                .observe(this, new Observer<List<Event>>() {
                    @Override
                    public void onChanged(List<Event> events) {
                        Map<Long, Long> wasteByDate = new HashMap<>();
                        for (Event event : events) {
                            if (event.actualStart != null && event.actualEnd != null) {
                                long plannedElapsed = event.plannedEnd.getTime() - event.plannedStart.getTime();
                                long actualElapsed = event.actualEnd.getTime() - event.actualStart.getTime();
                                if (actualElapsed > plannedElapsed) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(event.plannedStart);
                                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                                    calendar.set(Calendar.MINUTE, 0);
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MILLISECOND, 0);

                                    long key = calendar.getTimeInMillis();

                                    if (!wasteByDate.containsKey(key)) {
                                        wasteByDate.put(key, 0L);
                                    }
                                    wasteByDate.put(key, wasteByDate.get(key) + actualElapsed - plannedElapsed);
                                }
                            }
                        }
                        List<BarEntry> barEntries = new ArrayList<>();
                        for (int i = 6; i >= 0; i--) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            calendar.add(Calendar.DATE, -i);

                            long key = calendar.getTimeInMillis();
                            if (wasteByDate.containsKey(key)) {
                                barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_YEAR), wasteByDate.get(key) / 60000));
                            } else {
                                barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_YEAR), 0));
                            }
                        }

                        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.last_week));
                        barChart.setData(new BarData(barDataSet));
                    }
                });
    }
}
