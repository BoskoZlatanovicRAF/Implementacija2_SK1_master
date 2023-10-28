package com.raf.bosko_impl2.implementation2;

import implementation.ScheduleInterface;
import model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class WeeklySchedule implements ScheduleInterface<WeeklySchedule> {
    List<Meeting> meetings = new ArrayList<>();

    // Singleton pattern lazy synchronized
    private static class Loader {
        static final WeeklySchedule INSTANCE = new WeeklySchedule();
    }

    private WeeklySchedule () {}

    public static WeeklySchedule getInstance() {
        return Loader.INSTANCE;
    }

    @Override
    public WeeklySchedule initSchedule() {
        return getInstance();
    }

    @Override
    public WeeklySchedule addMeeting(Meeting meeting) {
        meetings.add(meeting);
        return null;
    }

    @Override
    public WeeklySchedule removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
        return null;
    }

    @Override
    public WeeklySchedule filterMeetings(Object... objects)
    {

        return null;
    }

    @Override
    public WeeklySchedule rescheduleMeeting(Meeting meeting)
    {

        return null;
    }
}
