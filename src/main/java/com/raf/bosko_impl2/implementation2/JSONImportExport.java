package com.raf.bosko_impl2.implementation2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raf.bosko_impl2.model.MeetingImpl2;
import implementation.ScheduleImportExport;
import model.Meeting;
import model.Room;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONImportExport extends ScheduleImportExport {
    @Override
    public boolean importData(String filePath, String configPath) throws IOException {
        // Read the JSON file into a String
        String json = new String(Files.readAllBytes(Paths.get(filePath)));

        // Read the config JSON file into a String
        String configJson = new String(Files.readAllBytes(Paths.get(configPath)));

        // Parse the configuration file
        Gson gson = new Gson();
        Type configType = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
        Map<String, Map<String, String>> config = gson.fromJson(configJson, configType);

        // Extract mandatory fields mapping
        Map<String, String> mandatoryFields = config.get("mandatoryFields");

        // Parse the JSON String into a List of Maps
        Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> listOfMaps = gson.fromJson(json, listType);

        // Define your date time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

        // Iterate over the list and create MeetingImpl2 instances
        for (Map<String, String> map : listOfMaps) {
            MeetingImpl2.MeetingBuilder builder = new MeetingImpl2.MeetingBuilder();

            // Process mandatory fields
            builder.withTimeStart(LocalDateTime.parse(map.get(mandatoryFields.get("start")), formatter));
            builder.withTimeEnd(LocalDateTime.parse(map.get(mandatoryFields.get("end")), formatter));
            builder.withRoom(new Room(map.get(mandatoryFields.get("place"))));

            // Remove mandatory fields and treat the rest as additional
            map.keySet().removeAll(mandatoryFields.values());

            // Add remaining attributes as additional
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.withAttribute(entry.getKey(), entry.getValue());
            }

            MeetingImpl2 meeting = builder.build();
            WeeklySchedule.getInstance().addMeeting(meeting);
        }
        // Print all meetings
        for(Meeting meeting : WeeklySchedule.getInstance().getMeetings()) {
            System.out.println(meeting.toString());
        }
        return true;
    }


    @Override
    public boolean exportData(String s) {
        return false;
    }
}
