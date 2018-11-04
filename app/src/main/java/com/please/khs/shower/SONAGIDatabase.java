package com.please.khs.shower;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SONAGIDatabase extends SQLiteOpenHelper{
    private Context context;

    public void clearDatabase() {
        SQLiteDatabase wdb = getWritableDatabase();

        String emotionSql = "DELETE FROM tableEmotion";
        String memoSql = "DELETE FROM tableMemo";

        wdb.execSQL(emotionSql);
        wdb.execSQL(memoSql);

        Log.d("test", "Table Clear");
    }

    public void putMsgData(String time, String msg, int emotion) {
        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String strTime = df.format(time);*/

        SQLiteDatabase wdb = getWritableDatabase();
        SQLiteDatabase rdb = getWritableDatabase();

        String query = "SELECT time, msg FROM tableEmotion ORDER BY time DESC limit 1";  // 가장 최근의 감정정보 불러오기
        String loadedTime = "";
        String loadedMsg = "";
        try {
            Cursor cursor = rdb.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    try {
                        loadedTime = cursor.getString(0);
                        loadedMsg = cursor.getString(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA);

            Date now = new Date();
            Date tempDate = dateFormat.parse(loadedTime);

            long diff = cutToHour(now.getTime()) - cutToHour(tempDate.getTime()); // 한시간 단위 아래 초는 다 잘라버리기

            if (diff == 0) {  // 처리된 시간단위가 같다면
                // UPDATE 구문으로 msg 추가시켜주고, emotion을 최신 으로 업데이트 시켜준다
                String sql = String.format("UPDATE tableMemo SET time = '%s', msg = '%s', emotion = %d WHERE time = '%s'", time, loadedMsg + msg, emotion, loadedTime); // Locale 왜 쓰는지 알아보기. Warning 이유
                wdb.execSQL(sql);
            } else {

                // 그냥 emotionData를 추가해준다. 또한 모든 메모는 Hour 단위로 저장이 된다. 그러니 신경 ㄱㅊ  아자아자
                String sql = String.format("INSERT INTO tableEmotion ( time, msg, emotion ) VALUES ( '%s', '%s', %s)", time, msg, Integer.toString(emotion));

                wdb.execSQL(sql);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "msg data inserted");
    }

    public void putMemoData(String time, String memo, int emotion) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA);

        Date now = new Date(); // 현재 시간
        Date recentDate = new Date();

        SQLiteDatabase wdb = getWritableDatabase();
        SQLiteDatabase rdb = getReadableDatabase();

        String query = "SELECT time FROM tableMemo ORDER BY time DESC limit 1";
        String tempTime = null;

        try {
            Cursor cursor = rdb.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                tempTime = cursor.getString(0);
            }

            recentDate = dateFormat.parse(tempTime);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tempTime == null) {
            // Add New Memo
            String sql = String.format("INSERT INTO tableMemo ( time, memo, emotion ) VALUES ('%s', '%s', %s)", time, memo, Integer.toString(emotion));

            wdb.execSQL(sql);
            Log.d("test", "new memo data");
        } else {
            if (cutToHour(now.getTime()) - cutToHour(recentDate.getTime()) == 0) {   // recent memo exists with same 'hour'
                // Update Memo
                String sql = String.format("UPDATE tableMemo SET memo='%s' WHERE time='%s'", memo, time);
                wdb.execSQL(sql);
                Log.d("test", "updated memo data");
            } else {
                // Add New Memo
                String sql = String.format("INSERT INTO tableMemo ( time, memo, emotion ) VALUES ('%s', '%s', %s)", time, memo, Integer.toString(emotion));

                wdb.execSQL(sql);
                Log.d("test", "new memo data");
            }
        }
    }

    private long cutToHour(long time) {
        return time - (time % (60 * 60 * 1000));
    }

    /*
    public void editMemoData(String time, String memo) {
        SQLiteDatabase wdb = getWritableDatabase();
        String sql = String.format("UPDATE tableMemo SET msg WHERE time = %s", memo);

        wdb.execSQL(sql);
    }*/

    public ArrayList<MemoData> getMemoDataListFromTime(String timeStart, String timeEnd) {

        String query = String.format("SELECT time, memo, emotion  FROM tableMemo WHERE time >= '%s' AND time <= '%s'", timeStart, timeEnd);

        ArrayList<MemoData> tempArr = new ArrayList<>();

        SQLiteDatabase rdb = getReadableDatabase();

        Cursor cursor;
        MemoData tempData = new MemoData();
        try {
            cursor = rdb.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    tempData.time = cursor.getString(0);
                    tempData.memo = cursor.getString(1);
                    tempData.emotion = Integer.parseInt(cursor.getString(2));

                    tempArr.add(tempData);
                } while (cursor.moveToNext());
            }
            // get memo data list

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempArr;
    }

    // sqliteDB를 이용해 tableName의 테이블 안에서 timeStart ~ timeEnd 까지의 메시지, 감정, 타입을 묶은 SONAGIData ArrayList를 반환함. -> DataRefresh에 사용될 예정이다.
    // timeStart, timeEnd의 형식은 "2018-9-15 09:20:15" 와 같아야 함.
    public ArrayList<SONAGIData> getEmotionDataListFromTime(String timeStart, String timeEnd) {

        String query = String.format("SELECT time, msg, emotion FROM tableEmotion WHERE time >= '%s' AND time <= '%s' limit 24", timeStart, timeEnd); // limit 는 혹시 모르니까...

        ArrayList<SONAGIData> tempArr = new ArrayList<>();

        SQLiteDatabase rdb = getReadableDatabase();

        Cursor cursor = rdb.rawQuery(query, null);
        SONAGIData sonagiData = null;

        try {
            if (cursor.moveToFirst()) {
                do {
                    sonagiData = new SONAGIData();
                    sonagiData.dateTime = cursor.getString(0);
                    sonagiData.msg = cursor.getString(1);
                    sonagiData.emotion = cursor.getInt(2);

                    tempArr.add(sonagiData);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();

        return tempArr;
    }

    // getLastestEmotionData -> 사용자 컨텐츠 추천에 사용된다.
    public SONAGIData getLatestEmotion() {
        SQLiteDatabase rdb = getReadableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

        SONAGIData tempData = new SONAGIData();
        String sqlQuery = "SELECT * FROM tableEmotion ORDER BY time DESC limit 1";

        tempData.dateTime = null;
        Date now = new Date();

        try {
            Cursor cursor = rdb.rawQuery(sqlQuery, null);
            if (cursor.moveToFirst()) {
                tempData.dateTime = cursor.getString(0);
                tempData.msg = cursor.getString(1);
                tempData.emotion = cursor.getInt(2);
            }

            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        Date recent = new Date();
        try {
            recent = dateFormat.parse(tempData.dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (tempData.dateTime == null) {  // 반환된 데이터가 없는 경우
            return null;
        }

        if ((now.getTime() - recent.getTime()) < 1000 * 60 * 60 * 5) { // Maximum 5 hour
            return tempData;
        } else {
            return null;
        }
    }

    // DB Creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        String emotionSql = "CREATE TABLE IF NOT EXISTS tableEmotion (time DATETIME, msg VARCHAR(500), emotion INT)";
        String memoSql = "CREATE TABLE IF NOT EXISTS tableMemo (time DATETIME, memo VARCHAR(1000), emotion INT)";

        db.execSQL(emotionSql);
        db.execSQL(memoSql);

        Log.d("test", "DB created successful");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("test", "DB structure updated");
    }

    public SONAGIDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.context = context;
    }

}
