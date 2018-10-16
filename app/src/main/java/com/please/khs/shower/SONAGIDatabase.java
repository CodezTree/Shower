package com.please.khs.shower;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SONAGIDatabase extends SQLiteOpenHelper{
    private Context context;

    /*
    // databaseName을 입력하여 해당 이름을 가진 데이터베이스를 생성. 만약에 이미 존재한다면 그
    // 데이터베이스를 자동으로 불러온다. (반환값은 SQLiteDatabase 이다)
    // databaseName은 sharedPreference로 저장해놓고 꺼내다 쓰는 것을 추천.
    public SQLiteDatabase loadOrCreateDatabase(String databaseName) {
        SQLiteDatabase sqliteDB = null;
        if (databaseName != null) {
            try {
                sqliteDB = SQLiteDatabase.openOrCreateDatabase(databaseName,);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }

        return sqliteDB;
    }

    // 데이터를 저장할 테이블을 만들어 준다. (반환값은 없다)
    // 딱 한번만 실행하고, tableName도 sharedPreference에 저장해 두는게 좋다.
    public void createEmotionTable(SQLiteDatabase sqliteDB, String tableName) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (time DATETIME, msg VARCHAR(500), emotion INT, processed INT)", tableName);

        try {
            sqliteDB.execSQL(sql);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void createMemoTable(SQLiteDatabase sqliteDB, String tableName) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (time DATETIME, memo VARCHAR(1000), emotion INT",tableName);

        try {
            sqliteDB.execSQL(sql);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }


    /// ------------------- WHAT??

    // 수집한 메시지 데이터를 저장하는 함수.
    // sqliteDB 의 tableName의 테이블에다 msg를 저장한다. 이때, time은 자동으로 저장되는 시점의 시간을
    // 저장하고, emotion은 -1 ( 처리되지 않음 ), processed (처리여부) 는 0 ( 처리안됨 ) 을 지정해준다.
    public int putMsgData(String msg) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String time = sdf.format(date);

        String sql = String.format("INSERT INTO %s ( time, msg, emotion, processed ) VALUES ( %s, %s, -1, 0)", tableName, time, msg);
        try {
            sqliteDB.execSQL(sql);
            return 1; // 정상적으로 처리됨
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1; // 비정상적인 처리
        }
    }

    */

    public int updateMsgDataByTime(SQLiteDatabase sqliteDB, String tableName, String time, int emotion) {
        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        String strTime = df.format(time);*/
        String sql = String.format("UPDATE %s SET emotion=%s, processed=-1 WHERE time=%s", tableName, Integer.toString(emotion), time);

        try {
            sqliteDB.execSQL(sql);
            return 1;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /// ------------------- WHAT??


    public void putMsgDataProcessed(String time, String msg, int emotion) {
        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String strTime = df.format(time);*/
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO tableEmotion ( time, msg, emotion, processed ) VALUES ( ?, ?, ?, 1 )";

        db.execSQL(sql, new Object[]{ time, msg, Integer.toString(emotion) });
        Log.d("test", "msg data inserted");
    }

    public void putMemoData(String time, String msg, int emotion) {
        /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String strTime = df.format(time);*/
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO tableMemo ( time, msg, emotion ) VALUES (?, ?, ?)";

        db.execSQL(sql, new Object[]{ time, msg, Integer.toString(emotion) });
        Log.d("test", "memo data inserted");
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

        String query = String.format("SELECT time, emotion, msg FROM tableEmotion WHERE processed == 1 AND time >= %s AND time <= %s", timeStart, timeEnd);

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

        return tempArr;
    }

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String emotionSql = "CREATE TABLE IF NOT EXISTS tableEmotion (time DATETIME, msg VARCHAR(500), emotion INT, processed INT)";
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
