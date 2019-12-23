package org.erhs.stem.project.time_management.activity;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.erhs.stem.project.time_management.R;
import org.erhs.stem.project.time_management.common.Utility;
import org.erhs.stem.project.time_management.domain.Event;
import org.erhs.stem.project.time_management.service.EventRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh:mm a");

    private static final String TBD = "TBD";

    private Resources resources;
    private List<Event> events;
    private MainActivity.OnEditCallback onEditCallback;
    private MainActivity.OnDeleteCallback onDeleteCallback;
    private MainActivity.OnProcessCallback onProcessCallback;

    public EventAdapter(Resources resources, List<Event> events,
                        MainActivity.OnEditCallback onEditCallback,
                        MainActivity.OnDeleteCallback onDeleteCallback,
                        MainActivity.OnProcessCallback onProcessCallback) {
        this.resources = resources;
        this.events = events;
        this.onEditCallback = onEditCallback;
        this.onDeleteCallback = onDeleteCallback;
        this.onProcessCallback = onProcessCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cardview_event, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Event event = events.get(position);

        holder.eventType.setImageDrawable(resources.getDrawable(Utility.getEventImageId(event.type)));

        if (event.actualStart == null) {
            holder.status.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_launch_black_24dp, 0, 0);
            holder.status.setText(resources.getString(R.string.not_started));
        } else if (event.actualEnd == null) {
            holder.status.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_directions_run_black_24dp, 0, 0);
            holder.status.setText(resources.getString(R.string.in_progress));
        } else {
            holder.status.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_done_black_24dp, 0, 0);
            holder.status.setText(resources.getString(R.string.done));
        }

        holder.description.setText(event.description);
        holder.planned.setText(resources.getString(R.string.event_planned,
                formatTime(event.plannedStart), formatTime(event.plannedEnd)));
        holder.actual.setText(resources.getString(R.string.event_actual,
                formatTime(event.actualStart), formatTime(event.actualEnd)));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditCallback.onEdit(event);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteCallback.onDelete(event);
                events.remove(event);
                EventRepository.deleteEvent(v.getContext(), event);
                EventAdapter.this.notifyItemRemoved(position);
            }
        });

        holder.process.setEnabled(event.actualStart == null || event.actualEnd == null);
        holder.process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.actualStart == null) {
                    event.actualStart = new Date();
                } else if (event.actualEnd == null) {
                    event.actualEnd = new Date();
                }
                onProcessCallback.onProcess(event);
                EventRepository.updateEvent(v.getContext(), event);
                EventAdapter.this.notifyItemChanged(position);
            }
        });
    }

    private String formatTime(Date date) {
        if (date != null) {
            return TIME_FORMATTER.format(date);
        }
        return TBD;
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
        private TextView status;

        private ImageButton edit;
        private ImageButton delete;
        private ImageButton process;

        public ViewHolder(View itemView) {
            super(itemView);
            eventType = itemView.findViewById(R.id.event_type);
            description = itemView.findViewById(R.id.description);
            planned = itemView.findViewById(R.id.planned);
            actual = itemView.findViewById(R.id.actual);
            status = itemView.findViewById(R.id.status);
            edit = itemView.findViewById(R.id.action_edit);
            delete = itemView.findViewById(R.id.action_delete);
            process = itemView.findViewById(R.id.action_process);
        }
    }
}
