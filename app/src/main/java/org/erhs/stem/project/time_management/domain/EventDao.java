package org.erhs.stem.project.time_management.domain;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM events WHERE plannedStart BETWEEN :start AND :end")
    LiveData<List<Event>> getEventsByDateRange(Long start, Long end);

    @Insert
    void insertEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Update
    void updateEvent(Event event);
}
