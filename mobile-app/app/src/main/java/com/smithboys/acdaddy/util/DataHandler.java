package com.smithboys.acdaddy.util;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataHandler {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private DataSnapshot snapshot;
    private HashMap dataMap;
    private HashMap actualTempMap;
    private HashMap desiredTempMap;

    public DataHandler(){
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.myRef = this.firebaseDatabase.getReference("timedata");
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public DataSnapshot getSnapshot() {
        return snapshot;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public HashMap getDataMap() {
        return dataMap;
    }

    public static HashMap getTimeDesiredMap(HashMap dataMap){
        HashMap<Date, Float> actualTempMap = new HashMap<>();
        HashMap<Date, Float> desiredTempMap = new HashMap<>();
        for (Iterator<Map.Entry<String, Map<String, String>>> entries = dataMap.entrySet().iterator(); entries.hasNext(); ) {
            Map.Entry<String, Map<String, String>> entry = entries.next();
            String timestamp = entry.getValue().get("ts");
            Float actualTemp = Float.valueOf(entry.getValue().get("actualTemp"));
            Float desiredTemp = Float.valueOf(entry.getValue().get("desiredTemp"));
            if(desiredTemp == 0f){
                continue;
            }

            //System.out.println("timestamp: " + timestamp);

            timestamp = timestamp.replace("T", " ");
            timestamp = timestamp.replace("S", "");

            String text = "2011-10-02 18:48:05.123";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try{
                date = format.parse(timestamp);
                actualTempMap.put(date, actualTemp);
                desiredTempMap.put(date, desiredTemp);

            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return desiredTempMap;
    }

    public static HashMap getTimeActualMap(HashMap dataMap){
        HashMap<Date, Float> actualTempMap = new HashMap<>();
        HashMap<Date, Float> desiredTempMap = new HashMap<>();
        for (Iterator<Map.Entry<String, Map<String, String>>> entries = dataMap.entrySet().iterator(); entries.hasNext(); ) {
            Map.Entry<String, Map<String, String>> entry = entries.next();
            System.out.println("entry: "+ entry);
            String timestamp = entry.getValue().get("ts");
            Float actualTemp = Float.valueOf(entry.getValue().get("actualTemp"));
            Float desiredTemp = Float.valueOf(entry.getValue().get("desiredTemp"));
            if(desiredTemp == 0f ){
                continue;
            }

            //System.out.println("timestamp: " + timestamp + ", " + actualTemp + " " + desiredTemp);

            timestamp = timestamp.replace("T", " ");
            timestamp = timestamp.replace("S", "");

            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try{
                date = format.parse(timestamp);
                if(desiredTempMap.get(date) != null){
                    continue;
                }
                actualTempMap.put(date, actualTemp);
                desiredTempMap.put(date, desiredTemp);

            } catch (ParseException e){
                e.printStackTrace();
            }
        }
        return actualTempMap;
    }

    public HashMap getActualTempMap() {
        return actualTempMap;
    }

    public HashMap getDesiredTempMap() {
        return desiredTempMap;
    }

    public void setSnapshot(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }
}
