package com.please.khs.shower;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SONAGIGlobalClass extends Application {
    public static SONAGIDatabase Sdb;
    public static ArrayList<SONAGIData> graphData = new ArrayList<>();  // 감정처리 결과
    //public static HashMap<Integer, MemoData> memoData = new HashMap<>();
    public static ArrayList<MemoData> memoData = new ArrayList<>();  // timeline memo data
    public static ArrayList<String> emotionSet = new ArrayList<>();  // 감정 순서대로.. 슬픔 기쁨..
}