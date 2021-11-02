package com.example.publictransportationapp;

public class Transport {

    String type, number, start, stop;

    public Transport(){}

    public Transport(String type, String number, String start, String stop)
    {
        this.type = type;
        this.number = number;
        this.start = start;
        this.stop = stop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", start='" + start + '\'' +
                ", stop='" + stop + '\'' +
                '}';
    }
}
