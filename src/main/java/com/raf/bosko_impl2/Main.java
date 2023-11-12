package com.raf.bosko_impl2;

import com.raf.bosko_impl2.implementation2.WeeklySchedule;
import importExport.CSVImportExport;
import model.Meeting;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        WeeklySchedule weeklySchedule = WeeklySchedule.getInstance(LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 31));
        CSVImportExport csvImportExport = new CSVImportExport();
        List<Meeting> meetingList = new ArrayList<>();
        try {
            meetingList = csvImportExport.importData("C:\\Users\\User\\IdeaProjects\\Implementacija2_SK1_master\\src\\main\\resources\\raspored.csv",
                    "C:\\Users\\User\\IdeaProjects\\Implementacija2_SK1_master\\src\\main\\resources\\config.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        weeklySchedule.setMeetings(meetingList);

        weeklySchedule.filterMeetings(DayOfWeek.MONDAY, LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 31), LocalTime.of(12, 0), LocalTime.of(18, 0));
        System.out.println("--------");
    }
}