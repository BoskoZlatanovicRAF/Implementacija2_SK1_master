package com.raf.bosko_impl2.implementation2;

import implementation.Schedule;
import lombok.Getter;
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
        List<Meeting> meetings = this.getMeetings().stream().filter(meeting -> ((Meeting)meeting).getDayOfWeek().
                equals(dayOfWeek)).
                sorted(Comparator.comparing(Meeting::getTimeStart)).
                collect(Collectors.toList());

        Map<Room, List<Meeting>> groupedMeetings = groupMeetingsByRoom(meetings);
        LocalTime previousEndTime = null;

        LocalTime globalTimeStart = LocalTime.of(9, 0);
        LocalTime globalTimeEnd = LocalTime.of(21, 0);

        for (LocalDate date = dateStart; !date.isAfter(dateEnd); date = date.plusDays(1)) {

            if (date.getDayOfWeek() == dayOfWeek) {

                for (Map.Entry<Room, List<Meeting>> entry : groupedMeetings.entrySet()) {
                    Room room = entry.getKey();

                    List<Meeting> meetingsForRoom = entry.getValue();
                    if(previousEndTime != null && timeStart.isBefore(previousEndTime)){
                        previousEndTime = timeStart;
                    }
                    else{
                        previousEndTime = globalTimeStart;
                    }
                    boolean first = true;
                    for(Meeting m: meetingsForRoom){
                        if(!(m.getTimeStart().toLocalDate().isEqual(m.getTimeEnd().toLocalDate()) && m.getTimeStart().toLocalDate().isEqual(date)) && !(m.getTimeStart().toLocalDate().isEqual(this.getTimeValidFrom()) && m.getTimeEnd().toLocalDate().isEqual(this.getTimeValidTo()))){
                            continue;
                        }
                        if(m.getTimeStart().toLocalTime().isAfter(previousEndTime)){
                            if(m.getTimeStart().toLocalTime().isAfter(globalTimeStart)){
                                LocalDateTime start = LocalDateTime.of(m.getTimeStart().toLocalDate(), globalTimeStart);
                                LocalDateTime end = LocalDateTime.of(m.getTimeEnd().toLocalDate(), m.getTimeStart().toLocalTime());
                                Gap gap = new Gap(start, end,room);
                                if (gaps.contains(gap)){
                                    gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                                }
                                else {
                                    gaps.add(gap);
                                }
                            }

                        }
                        else {
                            int index = meetingsForRoom.indexOf(m);
                            if(index + 1 < meetingsForRoom.size()){
                                Meeting nextMeeting = meetingsForRoom.get(index + 1);
                                boolean flag = false;
                                LocalDateTime start = LocalDateTime.of(m.getTimeStart().toLocalDate(), m.getTimeEnd().toLocalTime());
                                LocalDateTime end = LocalDateTime.of(m.getTimeEnd().toLocalDate(), nextMeeting.getTimeStart().toLocalTime());
                                if(end.toLocalTime().isAfter(timeEnd)){
                                    end = LocalDateTime.of(m.getTimeEnd().toLocalDate(), timeEnd);
                                    flag = true;
                                }

                                Gap gap = new Gap(start, end,room);
                                if (gaps.contains(gap)){
                                    gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                                }
                                else {
                                    gaps.add(gap);
                                }

                                if(flag){
                                    break;
                                }

                            }
                            else {
                                LocalDateTime start = LocalDateTime.of(m.getTimeStart().toLocalDate(), m.getTimeEnd().toLocalTime());
                                LocalDateTime end = LocalDateTime.of(m.getTimeEnd().toLocalDate(), globalTimeEnd);

                                if(end.toLocalTime().isAfter(timeEnd)){
                                    end = LocalDateTime.of(m.getTimeEnd().toLocalDate(), timeEnd);

                                }

                                Gap gap = new Gap(start, end,room);
                                if (gaps.contains(gap)){
                                    gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                                }
                                else {
                                    gaps.add(gap);
                                }
                            }
                        }
                        previousEndTime = m.getTimeEnd().toLocalTime();

                    }
//                    if (previousEndTime.isBefore(globalTimeEnd) ) {
//                        LocalDateTime start = LocalDateTime.of(date, previousEndTime);
//                        LocalDateTime end = LocalDateTime.of(date, globalTimeEnd);
//
//                        Gap gap = new Gap(start, end, room);
//
//                        if (gaps.contains(gap)) {
//                            gaps.get(gaps.indexOf(gap)).getRooms().add(room);
//                        } else {
//                            gaps.add(gap);
//                        }
//                    }

                }

            }

        }

        return gaps;
    }



    @Override
    public WeeklySchedule filterMeetings(LocalDateTime localDateTime) {
        return null;
    }

    @Override
    public WeeklySchedule rescheduleMeeting(Meeting meeting, LocalDateTime localDateTime, LocalDateTime localDateTime1) {
        return null;
    }


    private Map<Room, List<Meeting>> groupMeetingsByRoom(List<Meeting> meetings) {
        return meetings.stream()
                .collect(Collectors.groupingBy(Meeting::getRoom));
    }


}
/*




                                        for(Meeting m: meetingsForRoom){
                        if(first) {
                            if (m.getTimeStart().toLocalDate().isBefore(date) && m.getTimeEnd().toLocalDate().isAfter(date) || m.getTimeStart().toLocalDate().isEqual(date)) {
                                if (m.getTimeStart().toLocalTime().isAfter(globalTimeStart)) {
                                    LocalDateTime gapStart = LocalDateTime.of(date, globalTimeStart);
                                    LocalDateTime gapEnd = LocalDateTime.of(date, m.getTimeEnd().toLocalTime());
                                    Gap gap = new Gap(gapStart, gapEnd, room);

                                    if (gaps.contains(gap)) {
                                        gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                                    } else {
                                        gaps.add(gap);
                                    }
                                }
                                else if(previousEndTime != null && previousEndTime.isBefore(m.getTimeStart().toLocalTime())){
                                    LocalDateTime start = LocalDateTime.of(date, previousEndTime);
                                    LocalDateTime end = m.getTimeStart();
                                    Gap gap = new Gap(start, end,room);

                                    if (gaps.contains(gap)){
                                        gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                                    }
                                    else {
                                        gaps.add(gap);
                                    }
                                }
                            }
                            first = false;
                            previousEndTime = m.getTimeEnd().toLocalTime();
                        }
                        if (previousEndTime.isBefore(globalTimeEnd) ) {
                            LocalDateTime start = LocalDateTime.of(date, previousEndTime);;
                            LocalDateTime end = LocalDateTime.of(date, LocalTime.of(21, 0));

                            Gap gap = new Gap(start, end, room);

                            if (gaps.contains(gap)) {
                                gaps.get(gaps.indexOf(gap)).getRooms().add(room);
                            } else {
                                gaps.add(gap);
                            }
                        }


                    }

*/