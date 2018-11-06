package com.please.khs.shower;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.please.khs.shower.Main.GraphActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.net.URLDecoder;

public class SONAGIService extends Service {
    public static final String mBroadcastProcessMsgActionService = "p.m.a.action";
    public static final String mBroadcastContentActionService = "c.a.action";
    // Broadcast를 위한 액션 등록
    String firstEm = "a", secondEm = "b";
    public static SONAGIDatabase SDB; // 접근권한을 가지고 있어야 하는데...ㅠ

    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "hello service");

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastProcessMsgActionService);
        mIntentFilter.addAction(mBroadcastContentActionService);
        mIntentFilter.addAction("contentRequest");
        // Broadcast intent 확인할 Intent Filter를 추가.

        registerReceiver(mReceiver, mIntentFilter); // mReceiver = BroadcastReceiver

        contentWorker cW = new contentWorker();
        cW.start(); // Start Thread

        SDB = SONAGIGlobalClass.Sdb;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            Log.d("test","start command... ! " + action);
            if (CommandActions.CONTENTS.equals(action)) {
                Log.d("test","start command url");
                String url = intent.getStringExtra("URL");
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(url);
                urlIntent.setData(u);
                startActivity(urlIntent);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); // intent string 값
            Log.d("test", action);
            if (action != null) {
                switch (action) {
                    case mBroadcastProcessMsgActionService:
                        String msg = intent.getStringExtra("msgData");
                        Log.d("Broadcast : ", msg);
                        processMsg(msg);
                        break;
                    case mBroadcastContentActionService:
                        String url = intent.getStringExtra("URL");
                        Log.d("test", url);
                        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(urlIntent);
                    case "contentRequest":
                        Log.d("test", "content request");
                        requestContent(intent.getIntExtra("emotion", 0));
                        break;
                    default:
                        Log.d("test", "Unknown Intent Name " + action);
                        break;
                }
            }
        }
    };

    // SONAGI Data Process
    public void processMsg(String msg) {
        final String fmsg = msg;
        final String processedMsg;
        // Request using Volley
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Response
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA); // Process 에서는 mm:ss까지
                    String processTime = sdf.format(new Date(System.currentTimeMillis()));
                    int processedEmotion = Integer.parseInt(response);

                    Log.d("test","process time : "+processTime+"  emotion : "+Integer.toString(processedEmotion));
                    SDB.putMsgData(processTime, fmsg, processedEmotion);

                    // GraphRefresh
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("graphRefresh");
                    getApplicationContext().sendBroadcast(broadcastIntent); // graph refresh

                    int emotionAnalyzedTime = getPreferencesInt("EmotionAnalyzedTime");
                    emotionAnalyzedTime++;
                    savePreferencesInt("EmotionAnalyzedTime", emotionAnalyzedTime);

                    if (emotionAnalyzedTime < 10) {
                        savePreferencesInt("UserGrade", 0);
                    } else if (emotionAnalyzedTime < 200) {
                        savePreferencesInt("UserGrade", 1);
                    } else if (emotionAnalyzedTime < 5000) {
                        savePreferencesInt("UserGrade", 2);
                    } else if (emotionAnalyzedTime < 20000) {
                        savePreferencesInt("UserGrade", 3);
                    } else if (emotionAnalyzedTime < 150000) {
                        savePreferencesInt("UserGrade", 4);
                    } else {
                        savePreferencesInt("UserGrade", 5);
                    }

                    String tempEmotion = SONAGIGlobalClass.emotionSet.get(processedEmotion);
                    // 3 감정 연속인지 확인
                    if (firstEm.equals("a")) { // 처음인 경우
                        firstEm = tempEmotion;
                    } else {
                        if (firstEm.equals(secondEm)) {
                            requestContent(processedEmotion); // 연속이다
                            firstEm = "a";
                            secondEm = "b";
                        } else {
                            firstEm = tempEmotion;
                            secondEm = "b";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        RequestEmotionProcess requestEmotionProcess = new RequestEmotionProcess(msg, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(requestEmotionProcess);
    }

    // Content Requestor

    public void requestContent(int emotions) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String data[] = response.split("@@");

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Intent actionContent = new Intent(getApplicationContext(), InternetActivity.class);
                actionContent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                actionContent.putExtra("URL", data[1]);
                Log.d("test", "URL : " + data[1]);
                // data URL parcel and broadcast..?? NOPE Pending
                PendingIntent pendingIntent = PendingIntent.getActivity(SONAGIService.this, 0, actionContent, PendingIntent.FLAG_UPDATE_CURRENT);
                String decodeContent = "";
                try {
                    decodeContent = URLDecoder.decode(data[0], "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(decodeContent);

                bigText.setSummaryText("컨텐츠");

                NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "Shower");
                mNotificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.xs_logo))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.xs_logo)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("오늘 같은 기분엔..?")
                        .setContentText(decodeContent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setStyle(bigText)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                try {
                    nm.notify(246, mNotificationBuilder.build()); // 244 lol
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        RequestContent requestContent = new RequestContent(emotions, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(requestContent);
    }

    public class contentWorker extends Thread {
        public void run() {
            try {
                long s = 1000 * 60 * 30;
                Thread.sleep(s);

                while(true) {
                    if (getPreferencesInt("ContentUse") == 1) {
                        SONAGIData latestData = SDB.getLatestEmotion();
                        if (latestData != null) {
                            requestContent(latestData.emotion);
                            s = calcTime(); // 뻘짓입니다 ㅎㅎ
                            Thread.sleep(s); // One Hour Latency
                        } else {
                            s = 1000 * 60 * 30;
                            Thread.sleep(s);
                        }
                    } else {
                        Thread.sleep(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private int calcTime() {
        switch(getPreferencesInt("ContentTime")) {
            case 0:
                return 1000 * 60 * 60;
            case 1:
                return 1000 * 60 * 60 * 3;
            case 2:
                return 1000 * 60 * 60 * 10;
        }
        return -1; //error
    }

    private String getPreferencesString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    private int getPreferencesInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    private void savePreferencesString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    private void savePreferencesInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

}