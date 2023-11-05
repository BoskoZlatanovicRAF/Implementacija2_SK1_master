package com.raf.bosko_impl2.implementation2;

import com.raf.bosko_impl2.model.MeetingImpl2;
import implementation.ScheduleImportExport;
import model.Meeting;
import model.Room;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CSVImportExport extends ScheduleImportExport {
    @Override
    public boolean importData(String filePath, String configPath) throws IOException {

        List<ConfigMapping> columnMappings = readConfig(configPath);
        Map<Integer, String> mappings = new HashMap<>();
        FileReader fileReader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

        for(ConfigMapping configMapping : columnMappings) {
            mappings.put(configMapping.getIndex(), configMapping.getOriginal());
        }

        // Create formatter from the config file
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mappings.get(-1));

        for (CSVRecord record : parser) {
            MeetingImpl2.MeetingBuilder builder = new MeetingImpl2.MeetingBuilder();
            for (ConfigMapping entry : columnMappings) {
                int columnIndex = entry.getIndex();

                if (columnIndex == -1) continue;

                String columnName = entry.getCustom();

                switch (mappings.get(columnIndex)) {
                    case "place":
                        builder.withRoom(new Room(record.get(columnIndex)));
                        break;

                    case "start":
                        LocalDateTime startDateTime = LocalDateTime.parse(record.get(columnIndex), formatter);
                        builder.withTimeStart(startDateTime);
                        break;

                    case "end":
                        LocalDateTime endDateTime = LocalDateTime.parse(record.get(columnIndex), formatter);
                        builder.withTimeEnd(endDateTime);
                        break;

                }
            }

            MeetingImpl2 meeting = builder.build();
            WeeklySchedule.getInstance().addMeeting(meeting);
        }
        for(Meeting meeting : WeeklySchedule.getInstance().getMeetings()) {
            System.out.println(meeting.toString());
        }
        System.out.println("----------------------------");
        return true;
    }


    private static List<ConfigMapping> readConfig(String configPath) throws FileNotFoundException {

            List<ConfigMapping> mappings = new ArrayList<>();

            File file = new File(configPath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] splitLine = line.split(" ", 3);

                mappings.add(new ConfigMapping(Integer.valueOf(splitLine[0]), splitLine[1], splitLine[2]));
            }
            scanner.close();

            return mappings;
    }

    @Override
    public boolean exportData(String s) {
        return false;
    }
}
