package com.raf.bosko_impl2.model;

import model.Meeting;
import model.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeetingImpl2 extends Meeting {

    public MeetingImpl2() {
        super(null, null, null);
    }
    private MeetingImpl2(MeetingBuilder builder) {
        super(builder.timeStart, builder.timeEnd, builder.room, builder.additionalAttributes);
    }


    public static class MeetingBuilder {
        private LocalDateTime timeStart;
        private LocalDateTime timeEnd;
        private Room room;
        private List<LocalDate> exceptions = new ArrayList<>();
        private HashMap<String, String> additionalAttributes = new HashMap<>();


        public MeetingBuilder withTimeStart(LocalDateTime start) {
            this.timeStart = start;
            return this;
        }

        public MeetingBuilder withTimeEnd(LocalDateTime end) {
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

        public MeetingBuilder withAttribute(String key, String value) {
            this.additionalAttributes.put(key, value);
            return this;
        }


        public MeetingImpl2 build() {
            // Perform validation if necessary
            return new MeetingImpl2(this);
        }
    }
}
