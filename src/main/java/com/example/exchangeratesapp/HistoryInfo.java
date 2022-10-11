package com.example.exchangeratesapp;

public class HistoryInfo {
    private int id;
    private double rate;
    private String date;
    private double result;

    HistoryInfo(int id, double rate, String date, double result){
        this.id = id;
        this.rate = rate;
        this.date = date;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
