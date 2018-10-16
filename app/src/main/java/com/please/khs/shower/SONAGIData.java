package com.please.khs.shower;


import java.util.Date;

public class SONAGIData {
    public String dateTime;
    public String msg;
    public int emotion;
    //int processed; // 필요할지는 모르겠음
    public SONAGIData() {

    }

    public SONAGIData(String time, String msg, int emotion) {
        this.dateTime = time;
        this.msg = msg;
        this.emotion = emotion;
    }
}
