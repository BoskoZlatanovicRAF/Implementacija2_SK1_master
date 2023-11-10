package com.raf.bosko_impl2.implementation2;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.raf.bosko_impl2.model.MeetingImpl2;
import com.raf.bosko_impl2.model.RoomImpl2;
import implementation.ScheduleImportExport;
import model.Meeting;
import model.Room;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONImportExport extends ScheduleImportExport {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Override
    public boolean importData(String filePath, String configPath) throws IOException {
        Gson gson = new Gson();

        // Read and parse config.json
        Reader configReader = new FileReader(configPath);
        Map<String, Map<String, String>> config = gson.fromJson(configReader, new TypeToken<Map<String, Map<String, String>>>(){}.getType());
        configReader.close();
        Map<String, String> mandatoryFields = config.get("mandatoryFields");

        // Read and parse schedule.json
        Reader scheduleReader = new FileReader(filePath);
        List<Map<String, Object>> scheduleList = gson.fromJson(scheduleReader, new TypeToken<List<Map<String, Object>>>(){}.getType());
        scheduleReader.close();

        for (Map<String, Object> scheduleItem : scheduleList) {

            LocalDateTime timeStart = LocalDateTime.parse((String) scheduleItem.get(mandatoryFields.get("start")), formatter);
            LocalDateTime timeEnd = LocalDateTime.parse((String) scheduleItem.get(mandatoryFields.get("end")), formatter);
            Room room = new RoomImpl2((String) scheduleItem.get(mandatoryFields.get("place")));

            HashMap<String, String> additionalAttributes = new HashMap<>();
            for (Map.Entry<String, Object> entry : scheduleItem.entrySet()) {
                if (!mandatoryFields.containsValue(entry.getKey())) {
                    additionalAttributes.put(entry.getKey(), entry.getValue().toString());
                }
                if (mandatoryFields.get("room").equals(entry.getKey())) {
                    room.getFeatures().put(entry.getKey(), entry.getValue().toString());
                }
            }

            MeetingImpl2 meeting =  new MeetingImpl2(timeStart, timeEnd, room, additionalAttributes, null);
            WeeklySchedule.getInstance().addMeeting(meeting);
        }
        return true;

//        Gson gson = new Gson();
//
//        // Read and parse config.json
//        JsonObject configJson = gson.fromJson(new FileReader(configPath), JsonObject.class);
//        JsonObject mandatoryFields = configJson.getAsJsonObject("mandatoryFields");
//
//        // Read and parse schedule.json
//        JsonArray jsonArray = gson.fromJson(new FileReader(filePath), JsonArray.class);
//
//        for (JsonElement jsonElement : jsonArray) {
//            JsonObject jsonItem = jsonElement.getAsJsonObject();
//
//            MeetingImpl2 meetingImpl2 = new MeetingImpl2();
//
//            Room room = new RoomImpl2(jsonItem.get(mandatoryFields.get("place").getAsString()).getAsString(), null);
//            LocalDateTime timeStart = LocalDateTime.parse(jsonItem.get(mandatoryFields.get("start").getAsString()).getAsString(), formatter);
//            LocalDateTime timeEnd = LocalDateTime.parse(jsonItem.get(mandatoryFields.get("end").getAsString()).getAsString(), formatter);
//
//            for (Map.Entry<String, JsonElement> entry : jsonItem.entrySet()) {
//                // If the entry is not a mandatory field, add it to additional attributes
//                if (!mandatoryFields.has(entry.getKey())) {
//                    meetingImpl2.getAdditionalAttributes().put(entry.getKey(), entry.getValue().getAsString());
//                }
//                if(mandatoryFields.get("room").equals(entry.getKey())){
//                    room.getFeatures().put(entry.getKey(), entry.getValue().getAsString());
//                }
//            }
//            meetingImpl2.setTimeStart(timeStart);
//            meetingImpl2.setTimeEnd(timeEnd);
//            meetingImpl2.setRoom(room);
//            WeeklySchedule.getInstance().addMeeting(meetingImpl2);
//        }
//
//        // If everything went well, return true
//        return true;

    }


    @Override
    public boolean exportData(String filepath) throws IOException{
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonArray meetingsArray = new JsonArray();
//
//        for (Meeting meeting : WeeklySchedule.getInstance().getMeetings()) {
//            JsonObject meetingJson = new JsonObject();
//            meetingJson.addProperty("room", meeting.getRoom().getName()); // Assuming there's a method to get the room name
//            meetingJson.addProperty("time_start", meeting.getTimeStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            meetingJson.addProperty("time_end", meeting.getTimeEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//
//            JsonObject additionalAttributes = new JsonObject();
//
//            for (Map.Entry<String, String> entry : meeting.getAdditionalAttributes().entrySet()) {
//                additionalAttributes.addProperty(entry.getKey(), entry.getValue());
//            }
//            meetingJson.add("additional_attributes", additionalAttributes);
//
//            JsonObject roomFeatures = new JsonObject();
//            for (Map.Entry<String, String> entry : meeting.getRoom().getFeatures().entrySet()) {
//                roomFeatures.addProperty(entry.getKey(), entry.getValue());
//            }


//
//            meetingJson.add("room", roomFeatures);
//
//            meetingsArray.add(meetingJson);
//        }
//        String json = gson.toJson(meetingsArray);
//
//        try (FileWriter fileWriter = new FileWriter(filepath)) {
//            fileWriter.write(json);
//        }
//        return true;

        for(Meeting meeting : WeeklySchedule.getInstance().getMeetings()){
            System.out.println(meeting.toString());
        }
        return true;
    }
    //        // Get the list of meetings from the weekly schedule
//
//
//        // Create a Gson instance with pretty printing for better readability
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        // Convert the list of meetings to JSON
//        String json = gson.toJson(WeeklySchedule.getInstance().getMeetings());
//
//        // Write the JSON string to the specified file
//        try (FileWriter fileWriter = new FileWriter(filepath)) {
//            fileWriter.write(json);
//        } // try-with-resources will auto close the FileWriter
//
//        // If everything went well, return true
//        return true;

}
