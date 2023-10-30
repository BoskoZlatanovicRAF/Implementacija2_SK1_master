package com.raf.bosko_impl2.implementation2;

import com.raf.bosko_impl2.model.MeetingImpl2;
import implementation.ScheduleImportExport;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVImportExport implements ScheduleImportExport {
    @Override
    public Object serialize(Object o) {
        return null;
    }

    @Override
    public Object deserialize(Object o) {
        return null;
    }


    public List<MeetingImpl2> parseCsv(String csvInput) {
        List<MeetingImpl2> meetings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new StringReader(csvInput))) {
            String line;

            // Read the header row
            String[] headers = br.readLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].replaceAll("\"", ""), i);
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                MeetingImpl2.MeetingBuilder builder = new MeetingImpl2.MeetingBuilder();

                // Extract mandatory attributes
               // builder.withDay(LocalDate.of(values[headerMap.get("Dan")].replaceAll("[\\s\"]", "").toUpperCase()))
                        builder.withTimeStart(LocalTime.parse(values[headerMap.get("Termin")].split("-")[0], DateTimeFormatter.ofPattern("HH:mm")))
                        .withTimeEnd(LocalTime.parse(values[headerMap.get("Termin")].split("-")[1], DateTimeFormatter.ofPattern("HH:mm")))
                        .withAttribute("Učionica", values[headerMap.get("Učionica")].replaceAll("\"", ""));

                // Extract optional attributes
                for (String header : headerMap.keySet()) {
                    if (!"Dan".equals(header) && !"Termin".equals(header) && !"Učionica".equals(header)) {
                        builder.withAttribute(header, values[headerMap.get(header)].replaceAll("\"", ""));
                    }
                }

                meetings.add(builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meetings;
    }
}
