package com.smithboys.acdaddy.data;

import com.github.mikephil.charting.data.Entry;

public class LineSegment implements Comparable<LineSegment>{
    private float slope;
    private float intercept;
    private float startX;
    private float endX;
    private float startY;
    private float endY;

    public LineSegment(float slope, float intercept, float startX, float endX) {
        this.slope = slope;
        this.intercept = intercept;
        this.startX = startX;
        this.endX = endX;
        this.startY = this.getSlope() * this.getStartX() + this.getIntercept();
        this.endY = this.getSlope() * this.getEndX() + this.getIntercept();
    }
    public LineSegment(Entry point1, Entry point2){
        this.slope = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
        this.intercept = point1.getY() - this.getSlope() * point1.getX();
        this.startX = point1.getX();
        this.endX = point2.getX();
        this.startY = this.getSlope() * this.getStartX() + this.getIntercept();
        this.endY = this.getSlope() * this.getEndX() + this.getIntercept();
    }

    public float getSlope() {
        return slope;
    }

    public float getIntercept() {
        return intercept;
    }

    public float getStartX() {
        return startX;
    }

    public float getEndX() {
        return endX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndY() {
        return endY;
    }

    public Entry findIntersection(LineSegment otherSegment){

        //System.out.println("this segment: " + this + ", other segment: " + otherSegment);

        float intersectionX = (otherSegment.getIntercept() - this.getIntercept()) / (this.getSlope() - otherSegment.getSlope());
        float intersectionY = this.getSlope() * intersectionX + this.getIntercept();

        Entry intersection = new Entry((float)intersectionX, (float)intersectionY);
        //System.out.println("intersection point: " + intersection);
        if(this.checkIfInXBounds(intersection) && otherSegment.checkIfInXBounds(intersection)){
            return intersection;
        } else {
            return null;
        }
    }

    public Boolean checkIfInXBounds(Entry point){
        return point.getX() >= this.getStartX() && point.getX() <= this.getEndX();
    }

    public int compareTo(LineSegment ls) {
        return Float.compare(this.getStartX(), ls.getStartX());
    }

    public float getAverageY(){
        return (this.getStartY() + this.getEndY()) / 2;
    }

    @Override
    public String toString(){
        return "Start: " + this.startX + " End: " + this.endX + " Slope: " + this.slope + " Intercept:" + this.intercept;
    }

}
