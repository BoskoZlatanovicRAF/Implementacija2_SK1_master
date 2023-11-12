package com.raf.bosko_impl2.implementation2;

import implementation.Schedule;
import lombok.Getter;
import lombok.Setter;
import model.Gap;
import model.Meeting;
import model.Room;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter

public class WeeklySchedule extends Schedule<WeeklySchedule> {

    private List<LocalDate> exceptions;

    // Singleton pattern lazy synchronized
    private static class Loader {
        static final WeeklySchedule INSTANCE = new WeeklySchedule();
    }

    private WeeklySchedule () {

    }

    public static WeeklySchedule getInstance() {
        return Loader.INSTANCE;
    }

    public static WeeklySchedule getInstance(LocalDate start, LocalDate end) {

        WeeklySchedule weeklySchedule = getInstance();
        weeklySchedule.setTimeValidFrom(start);
        weeklySchedule.setTimeValidTo(end);
        return weeklySchedule;
    }

    public void setMeetings(List<Meeting> meetings) {
        super.setMeetings(meetings);
        validateMeetings();
    }


    @Override
    public WeeklySchedule initSchedule() {
        return getInstance();
    }

    private void validateMeetings() {
        for(Meeting meeting: WeeklySchedule.getInstance().getMeetings()){
            if(meeting.getTimeStart().toLocalDate().isEqual(LocalDate.of(1000, 1,1))){
                meeting.setTimeStart(LocalDateTime.of(getTimeValidFrom(), meeting.getTimeStart().toLocalTime()));
                meeting.setTimeEnd(LocalDateTime.of(getTimeValidTo(), meeting.getTimeEnd().toLocalTime()));
            }
        }
    }

    @Override
    public WeeklySchedule addMeeting(Meeting meeting) {
        getMeetings().add(meeting);
        return null;
    }

    @Override
    public WeeklySchedule removeMeeting(Meeting meeting) {
        getMeetings().remove(meeting);
        return null;
    }

    @Override
    public WeeklySchedule filterMeetings(Object... objects)
    {


        return null;
    }

    @Override
    public WeeklySchedule filterMeetings(DayOfWeek dayOfWeek, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd) {
        filterMeetingsGap(dayOfWeek, dateStart, dateEnd, timeStart, timeEnd);
        return null;
    }

    private List<Gap> filterMeetingsGap(DayOfWeek dayOfWeek, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd) {
        List<Gap> gaps = new ArrayList<>();
        Map<Room, List<Meeting>> groupedMeetings = groupMeetingsByRoom(getMeetings()); // Assuming 'meetings' is a list of all meetings.
        LocalDateTime previousEndTime = null;
        // Iterate over each date in the range.
        for (LocalDate date = dateStart; !date.isAfter(dateEnd); date = date.plusDays(1)) {
            // Check if the current date is the day of the week we're looking for.
            if (date.getDayOfWeek() == dayOfWeek) {
                // Now check for each room.
                for (Map.Entry<Room, List<Meeting>> entry : groupedMeetings.entrySet()) {
                    Room room = entry.getKey();
                    List<Meeting> meetingsForRoom = entry.getValue();
                    boolean first = true;

                    for(Meeting m: meetingsForRoom){
                        if(first){
                            if(m.getTimeStart().toLocalTime().isAfter(LocalTime.of(9, 0))){
                                LocalDateTime start = LocalDateTime.of(m.getTimeStart().toLocalDate(), LocalTime.of(9, 0));
                                LocalDateTime end = m.getTimeStart();
                                Gap gap = new Gap(start, end,room);
                                first = false;
                                if (gaps.contains(gap)){
                                    gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                                }
                                else {
                                    gaps.add(gap);
                                }
                            }

                        }
                        else if(previousEndTime.toLocalTime().isBefore(m.getTimeStart().toLocalTime())){
                            LocalDateTime start = previousEndTime;
                            LocalDateTime end = m.getTimeStart();
                            Gap gap = new Gap(start, end,room);

                            if (gaps.contains(gap)){
                                gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                            }
                            else {
                                gaps.add(gap);
                            }
                        }
                        previousEndTime = m.getTimeEnd();
                    }
                    // Check if the time slot is available for this room. TODO globalTimeStart
                    if (previousEndTime.toLocalTime().isBefore(LocalTime.of(21, 0))) {
                        LocalDateTime start = previousEndTime;
                        LocalDateTime end = LocalDateTime.of(previousEndTime.toLocalDate(), LocalTime.of(21, 0));

                        Gap gap = new Gap(start, end, room);

                        if (gaps.contains(gap)) {
                            gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                        } else {
                            gaps.add(gap);
                        }
                    }
                }

            }

        }

        return gaps;
    }




    // Helper method to find an existing gap
    private Gap findExistingGap(List<Gap> gaps, Gap newGap) {
        for (Gap existingGap : gaps) {
            if (existingGap.getStartTime().equals(newGap.getStartTime()) &&
                    existingGap.getEndTime().equals(newGap.getEndTime())) {
                return existingGap;
            }
        }
        return null;
    }


    private boolean isTimeSlotFree(List<Meeting> meetings, LocalDate date, LocalTime start, LocalTime end) {
        LocalDateTime startDateTime = LocalDateTime.of(date, start);
        LocalDateTime endDateTime = LocalDateTime.of(date, end);

        for (Meeting meeting : meetings) {
            // If the meeting overlaps with the desired time slot, return false.
            if (meeting.getTimeStart().isBefore(endDateTime) && meeting.getTimeEnd().isAfter(startDateTime)) {
                return false;
            }
        }
        // If no meetings overlap, the time slot is free.
        return true;
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


    private Map<Room, List<Meeting>> groupMeetingsByRoom(List<Meeting> meetings) {
        return meetings.stream()
                .collect(Collectors.groupingBy(Meeting::getRoom));
    }


}
