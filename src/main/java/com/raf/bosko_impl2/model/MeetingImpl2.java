package com.raf.bosko_impl2.model;

import lombok.Getter;
import lombok.Setter;
import model.Meeting;
import model.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Getter
@Setter
public class MeetingImpl2 extends Meeting {
    private List<LocalDate> exceptions;

    // Constructor with all the necessary parameters
    public MeetingImpl2(){
        super();
    }

    public MeetingImpl2(LocalDateTime timeStart, LocalDateTime timeEnd, Room room, HashMap<String, String> additionalAttributes) {
        super(timeStart, timeEnd, room, additionalAttributes);
    }

    public MeetingImpl2(LocalDateTime timeStart, LocalDateTime timeEnd, Room room, HashMap<String, String> additionalAttributes, List<LocalDate> exceptions) {
        super(timeStart, timeEnd, room, additionalAttributes);
        this.exceptions = exceptions;
    }

    public String getTimeStartString() {
        return getTimeStart() != null ? getTimeStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    // Add a method to get the timeEnd as a string
    public String getTimeEndString() {
        return getTimeEnd() != null ? getTimeEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

}
