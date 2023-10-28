package com.raf.bosko_impl2.model;

import model.Meeting;
import model.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MettingImpl2 extends Meeting {
    List<LocalDate> exceptions = new ArrayList<>();
    public MettingImpl2(MeetingBuilder builder) {
        super(builder.day, builder.timeStart, builder.timeEnd, builder.room);
        this.exceptions = builder.exceptions;
    }

    public void addException(LocalDate exceptionDate) {
        this.exceptions.add(exceptionDate);
    }

    public void removeException(LocalDate date) {
        exceptions.remove(date);
    }

    public boolean isException(LocalDate date) {
        return exceptions.contains(date);
    }
    public static class MeetingBuilder {
        private LocalDate day;
        private LocalTime timeStart;
        private LocalTime timeEnd;
        private Room room;
        private List<LocalDate> exceptions = new ArrayList<>();

        public MeetingBuilder withDay(LocalDate day) {
            this.day = day;
            return this;
        }

        public MeetingBuilder withTimeStart(LocalTime start) {
            this.timeStart = start;
            return this;
        }

        public MeetingBuilder withTimeEnd(LocalTime end) {
            this.timeEnd = end;
            return this;
        }

        public MeetingBuilder withRoom(Room room) {
            this.room = room;
            return this;
        }

        public MeetingBuilder withException(LocalDate date) {
            this.exceptions.add(date);
            return this;
        }

        public MettingImpl2 build() {
            // Perform validation if necessary
            return new MettingImpl2(this);
        }
    }

    @Override
    protected void isValid() {
        return;
    }
}
