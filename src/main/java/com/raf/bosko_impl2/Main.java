package com.raf.bosko_impl2;

import com.raf.bosko_impl2.implementation2.CSVImportExport;
import com.raf.bosko_impl2.implementation2.JSONImportExport;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CSVImportExport csvImportExport = new CSVImportExport();
//          JSONImportExport jsonImportExport = new JSONImportExport();
        try {
            csvImportExport.importData("C:\\Users\\User\\IdeaProjects\\Implementacija2_SK1_master\\src\\main\\resources\\raspored.csv",
                                    "C:\\Users\\User\\IdeaProjects\\Implementacija2_SK1_master\\src\\main\\resources\\config.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}