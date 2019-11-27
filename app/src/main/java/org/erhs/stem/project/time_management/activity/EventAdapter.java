package org.erhs.stem.project.time_management.activity;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.domain.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cardview_event, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);
        Resources resources = context.getResources();

        switch (event.type) {
            case DINE:
                break;
            case OTHER:
                break;
            default:
                break;
        }

        holder.description.setText(event.description);
        holder.planned.setText(resources.getString(R.string.event_planned));
        holder.actual.setText(resources.getString(R.string.event_actual));

        if (event.actualStart == null) {

        } else if (event.actualEnd == null) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView eventType;
        private TextView description;
        private TextView planned;
        private TextView actual;
        private ProgressBar status;

        public ViewHolder(View itemView) {
            super(itemView);
            eventType = itemView.findViewById(R.id.event_type);
            description = itemView.findViewById(R.id.description);
            planned = itemView.findViewById(R.id.planned);
            actual = itemView.findViewById(R.id.actual);
            status = itemView.findViewById(R.id.status);
        }
    }
}
