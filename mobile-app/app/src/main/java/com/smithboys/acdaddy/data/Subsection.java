package com.smithboys.acdaddy.data;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class Subsection {
    private float startX;
    private float endX;
    private int line;
    private List<Entry> values;

    public Subsection(float startX, float endX, int line){
        this.startX = startX;
        this.endX = endX;
        this.line = line;
    }

    public Subsection(List<Entry> values, int line){
        this.values = values;
        this.startX = values.get(0).getX();
        this.endX = values.get(values.size()-1).getX();
        this.line = line;
    }

    public float getEndX() {
        return endX;
    }

    public float getStartX() {
        return startX;
    }

    public int getLine() {
        return line;
    }

    public void setValues(List<Entry> values) {
        this.values = values;
    }

    public List<Entry> getValues(){
        return this.values;
    }

    @Override
    public String toString(){
        return "Start X:" + this.getStartX() + " End X: " + this.getEndX() + " Line: " + this.getLine();
    }
}
