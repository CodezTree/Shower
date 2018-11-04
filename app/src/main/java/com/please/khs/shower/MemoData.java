package com.please.khs.shower;

public class MemoData {
    public String time;
    public String memo;
    public int emotion;

    public String getMemo() {
        return memo;
    }

    public String getTime() {
        return time;
    }

    public int getEmotion() {
        return emotion;
    }

    public MemoData (String time, String memo, int emotion){
        this.time = time;
        this.memo = memo;
        this.emotion = emotion;
    }

    public MemoData() {

    }
}
