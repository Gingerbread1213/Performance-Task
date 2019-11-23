package org.erhs.stem.project.time_management.domain;

public enum EventType {

    DINING("dining"),
    STUDY("study"),
    OTHER("other");

    private final String name;

    EventType(String name) {
        this.name = name;
    }

    public EventType parseFrom(String name) {
        return DINING;
    }
}
