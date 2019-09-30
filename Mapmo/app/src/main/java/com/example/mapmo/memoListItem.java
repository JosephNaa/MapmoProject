package com.example.mapmo;

public class memoListItem {

    private int id;
    private String title;
    private String start_date;
    private String finish_date;
    private String address;

    public void setId(int id){ this.id = id; }
    public void setTitle(String title){ this.title = title; }
    public void setStart_date(String start_date){ this.start_date = start_date; }
    public void setFinish_date(String finish_date){ this.finish_date = finish_date; }
    public void setAddress(String address){ this.address = address; }

    public int getId(){ return id; }
    public String getTitle() { return title; }
    public String getStart_date() { return start_date; }
    public String getFinish_date() { return finish_date; }
    public String getAddress() { return address;}
}
