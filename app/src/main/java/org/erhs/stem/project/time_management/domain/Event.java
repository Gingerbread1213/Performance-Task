package org.erhs.stem.project.time_management.domain;

import java.util.Date;

/**
 * Event class represents an event
 */
public class Event {

    private final EventType eventType;
    private final String eventTypeComplement;

    private Date plannedStartTime;
    private Date plannedEndTime;
    private Date actualEndTime;

    /**
     * Constructor
     * @param eventType
     * @param eventTypeComplement
     * @param plannedStartTime
     * @param plannedEndTime
     */
    public Event(EventType eventType, String eventTypeComplement, Date plannedStartTime,
                 Date plannedEndTime) {
        this.eventType = eventType;
        this.eventTypeComplement = eventTypeComplement;
        this.plannedStartTime = plannedStartTime;
        this.plannedEndTime = plannedEndTime;
    }
}
