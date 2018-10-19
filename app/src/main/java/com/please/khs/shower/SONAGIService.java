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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SONAGIService extends Service {
    public static final String mBroadcastProcessMsgActionService = "p.m.a.action";
    public static final String mBroadcastContentActionService = "c.a.action";
    // Broadcast를 위한 액션 등록

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
        String processedMsg;
        // Request using Volley
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Response
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                    String processTime = sdf.format(new Date(System.currentTimeMillis()));
                    int processedEmotion = Integer.parseInt(response);

                    SONAGIGlobalClass.Sdb.putMsgDataProcessed(processTime, fmsg, processedEmotion);
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
                Intent actionContent = new Intent(SONAGIService.this, InternetActivity.class);
                actionContent.putExtra("URL", data[1]);
                Log.d("test", "URL : " + data[1]);
                // data URL parcel and broadcast..?? NOPE Pending
                PendingIntent pendingIntent = PendingIntent.getActivity(SONAGIService.this, 0, actionContent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder mNotificationBuilder = new Notification.Builder(getApplicationContext());
                mNotificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.xs_logo))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.mipmap.xs_logo)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                mNotificationBuilder.setContentTitle(data[0])
                        .setContentText("오늘 같은 기분엔...?");

                mNotificationBuilder.setPriority(Notification.PRIORITY_HIGH);
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
                while(true) {
                    SONAGIData latestData = SONAGIGlobalClass.Sdb.getLatestEmotion();
                    if (latestData != null) {
                        requestContent(latestData.emotion);
                        long s = 1000 * 60 * 60;
                        Thread.sleep(s); // One Hour Latency
                    } else {
                        long s = 1000 * 60 * 30;
                        Thread.sleep(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
