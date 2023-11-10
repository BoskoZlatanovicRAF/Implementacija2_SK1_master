package com.raf.bosko_impl2.implementation2;

import implementation.Schedule;
import lombok.Getter;
import lombok.Setter;
import model.Meeting;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class WeeklySchedule extends Schedule<WeeklySchedule> {
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
    public WeeklySchedule filterMeetings(DayOfWeek dayOfWeek, LocalDate localDate, LocalDate localDate1, LocalTime localTime, LocalTime localTime1) {
        return null;
    }

    @Override
    public WeeklySchedule filterMeetings(LocalDateTime localDateTime) {
        return null;
    }

    @Override
    public WeeklySchedule rescheduleMeeting(Meeting meeting)
    {

        return null;
    }
}
