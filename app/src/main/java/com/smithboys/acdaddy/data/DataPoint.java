package com.smithboys.acdaddy.data;

public class DataPoint {
    private float time, temp;

    public DataPoint(float time, float temp){
        this.time = time;
        this.temp = temp;
    }
    public DataPoint(){
    }

    public float getTime() {
        return time;
    }

    public float getTemp() {
        return temp;
    }
}
