package com.please.khs.shower;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SONAGIGlobalClass extends Application {
    public static SONAGIDatabase Sdb;
    public static ArrayList<SONAGIData> graphData = new ArrayList<>();
    //public static HashMap<Integer, MemoData> memoData = new HashMap<>();
    public static ArrayList<MemoData> memoData = new ArrayList<>();
    public static ArrayList<MemoData> timeData = new ArrayList<>();
    public static ArrayList<String> emotionSet = new ArrayList<>();
}