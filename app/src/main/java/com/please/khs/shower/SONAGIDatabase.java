package com.please.khs.shower;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SONAGIDatabase extends SQLiteOpenHelper{
    private Context context;

    /*
    public int updateMsgDataByTime(SQLiteDatabase sqliteDB, String tableName, String time, int emotion) {
        // DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        // String strTime = df.format(time);
        String sql = String.format("UPDATE %s SET emotion=%s, processed=-1 WHERE time=%s", tableName, Integer.toString(emotion), time);

        try {
            sqliteDB.execSQL(sql);
            return 1;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1;
        }
    }*/

    /// ------------------- WHAT??


    public void putMsgDataProcessed(String time, String msg, int emotion) {
        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String strTime = df.format(time);*/

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT time, msg FROM tableEmotion ORDER BY time DESC limit 1";  // 가장 최근의 감정정보 불러오기
        String loadedTime = "";
        String loadedMsg = "";
        try {
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                try {
                     loadedTime = cursor.getString(0);
                     loadedMsg = cursor.getString(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA);

            Date now = new Date();
            Date tempDate = dateFormat.parse(loadedTime);

            long diff = now.getTime() - tempDate.getTime();
            if (diff == 0) {  // 처리된 시간단위가 같다면

                // UPDATE 구문으로 msg 추가시켜주고, emotion을 최신 으로 업데이트 시켜준다

            } else {

                // 그냥 emotionData를 추가해준다. 또한 모든 메모는 Hour 단위로 저장이 된다. 그러니 신경 ㄱㅊ  아자아자

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO tableEmotion ( time, msg, emotion ) VALUES ( ?, ?, ?)";

        db.execSQL(sql, new Object[]{ time, msg, Integer.toString(emotion) });
        Log.d("test", "msg data inserted");
    }

    public void putMemoData(String time, String msg, int emotion) {
        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String strTime = df.format(time);*/
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO tableMemo ( time, msg, emotion ) VALUES (?, ?, ?)";

        db.execSQL(sql, new Object[]{time, msg, Integer.toString(emotion) });
        Log.d("test", "memo data inserted");
    }

    public void editMemoData(String time, String msg) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE tableMemo SET msg WHERE time = %s";
    }

    // sqliteDB를 이용해 tableName의 테이블 안에서 timeStart ~ timeEnd 까지의 메시지, 감정, 타입을 묶은 SONAGIData ArrayList를 반환함.
    // timeStart, timeEnd의 형식은 "2018-9-15 09:20:15" 와 같아야 함.
    public ArrayList<SONAGIData> getEmotionDataListFromTime(SQLiteDatabase sqliteDB, String tableName, String timeStart, String timeEnd) {
        // String sqlQuery = String.format("SELECT time, emotion, msg FROM %s WHERE processed == 1 AND time >= %s AND time <= %s", tableName, timeStart, timeEnd);

        /*Cursor cursor = null;

        ArrayList<SONAGIData> tempArr = new ArrayList<>();

        try {
            cursor = sqliteDB.rawQuery(sqlQuery, null);
            SONAGIData tempSAD = new SONAGIData();
            while(cursor.moveToNext()) {
                try {
                    tempSAD.dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).parse(cursor.getString(0));
                    tempSAD.emotion = cursor.getInt(1);
                    tempSAD.msg = cursor.getString(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } */

        String query = String.format("SELECT time, emotion, msg FROM tableEmotion WHERE time >= %s AND time <= %s", timeStart, timeEnd);

        ArrayList<SONAGIData> tempArr = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        SONAGIData sonagiData = null;

        try {
            while(cursor.moveToNext()) {
                sonagiData = new SONAGIData();
                sonagiData.dateTime = cursor.getString(0);
                sonagiData.msg = cursor.getString(1);
                sonagiData.emotion = cursor.getInt(2);

                tempArr.add(sonagiData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();

        return tempArr;
    }

    // getLastestEmotionData
    public SONAGIData getLatestEmotion() {
        SQLiteDatabase db = getReadableDatabase();

        SONAGIData tempData = new SONAGIData();
        String sqlQuery = "SELECT * FROM tableEmotion ORDER BY time DESC limit 1";

        tempData.dateTime = null;

        try {
            Cursor cursor = db.rawQuery(sqlQuery, null);
            while(cursor.moveToNext()) {
                try {
                    tempData.dateTime = cursor.getString(0);
                    tempData.msg = cursor.getString(1);
                    tempData.emotion = cursor.getInt(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (tempData.dateTime == null) {  // 반환된 데이터가 없는 경우
            return null;
        }

        return tempData;
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
