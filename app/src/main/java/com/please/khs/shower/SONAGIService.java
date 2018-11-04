package com.please.khs.shower;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
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
    String firstEm = null, secondEm = null;

    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();

        SONAGIGlobalClass.Sdb = new SONAGIDatabase(getApplicationContext(), "SONAGI", null, 1);


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastProcessMsgActionService);
        mIntentFilter.addAction(mBroadcastContentActionService);
        mIntentFilter.addAction("tester");
        // Broadcast intent 확인할 Intent Filter를 추가.

        registerReceiver(mReceiver, mIntentFilter); // mReceiver = BroadcastReceiver

        contentWorker cW = new contentWorker();
        cW.start(); // Start Thread
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
                    case "tester":
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

                    SONAGIGlobalClass.Sdb.putMsgData(processTime, fmsg, processedEmotion);
                    Log.d("test","process time : "+processTime+"  emotion : "+Integer.toString(processedEmotion));

                    // GraphRefresh
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("graphRefresh");
                    //getApplicationContext().sendBroadcast(broadcastIntent);

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

                Notification.Builder mNotificationBuilder = new Notification.Builder(getApplicationContext());
                mNotificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.xs_logo))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.xs_logo)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("오늘 같은 기분엔...?")
                        .setAutoCancel(true)
                        .setStyle(new Notification.BigTextStyle().bigText(decodeContent))
                        .setPriority(Notification.PRIORITY_MAX);

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
                    SONAGIData latestData = SONAGIGlobalClass.Sdb.getLatestEmotion();
                    if (latestData != null) {
                        requestContent(latestData.emotion);
                        s = 1000 * 60 * 60;
                        Thread.sleep(s); // One Hour Latency
                    } else {
                        s = 1000 * 60 * 30;
                        Thread.sleep(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
