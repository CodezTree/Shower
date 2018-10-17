package com.please.khs.shower;


import java.util.Date;

public class SONAGIData {
    /*
    해당 정점에 대한 처리 된 메시지, 시간(처리시간), 처리된 감정
    */
    public String dateTime;
    public String msg;
    public int emotion;

    public SONAGIData() {
        // empty creator
    }

    public SONAGIData(String time, String msg, int emotion) {
        this.dateTime = time;
        this.msg = msg;
        this.emotion = emotion;
    }
}
