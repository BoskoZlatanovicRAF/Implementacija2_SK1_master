package com.raf.bosko_impl2;

import com.raf.bosko_impl2.implementation2.CSVImportExport;
import com.raf.bosko_impl2.implementation2.JSONImportExport;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        CSVImportExport csvImportExport = new CSVImportExport();
          JSONImportExport jsonImportExport = new JSONImportExport();
        try {
            jsonImportExport.importData("C:\\Users\\User\\IdeaProjects\\Implementacija2_SK1_master\\src\\main\\resources\\raspored.json",
                                    "C:\\Users\\User\\IdeaProjects\\Implementacija2_SK1_master\\src\\main\\resources\\config.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try{
            jsonImportExport.exportData("test2.json");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}