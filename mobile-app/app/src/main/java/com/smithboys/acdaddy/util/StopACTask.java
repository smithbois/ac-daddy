package com.smithboys.acdaddy.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.TimerTask;

public class StopACTask extends TimerTask {
    @Override
    public void run() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("config");
        myRef.child("onOrOff").setValue("0");

    }
}