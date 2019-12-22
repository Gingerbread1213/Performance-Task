package org.erhs.stem.project.time_management.service;

public class ApplicationMonitor {

    private volatile static ApplicationMonitor INSTANCE;

    private boolean notificationEnabled = true;

    private ApplicationMonitor() {
        // do nothing
    }

    public static ApplicationMonitor getInstance() {
        if (INSTANCE == null) {
            synchronized (ApplicationMonitor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApplicationMonitor();
                }
            }
        }
        return INSTANCE;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
}
