package com.arikehparsi.reyhanh.yedarbast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class IService extends Service {
    Handler handler;
    NotificationCompat.Builder mbuilder;
    String context;
    String channel_id = "yedarbast_trip";
    private NotificationManagerCompat notificationManagerCompat;
    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(
                    channel_id,channel_id,NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("yederbast_trip");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Socket socket;
        createNotificationChannel();
        notificationManagerCompat= NotificationManagerCompat.from(this);
       // Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_SHORT).show();
        try
        {
            handler = new Handler();
            socket = IO.socket(Lib.Service.baseURL);
            socket.connect();
            SharedPreferences sp5;
            sp5 = getSharedPreferences("Taxi_user_datas",MODE_PRIVATE);
            String token7 = sp5.getString("token","no");
            socket.emit("User_Connected",token7);
            socket.on("end_of_trip2", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    context ="سفر شما تمام شد";
                    showNotificatio();
                    stopService(new Intent(getApplicationContext(),IService.class));
                }
            });
            socket.on("set_status23", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                String status = data.getString("status");

                                if(status.equals("2"))
                                {
                                    context ="راننده ی شما رسید";
                                    showNotificatio();
                                }else if(status.equals("3"))
                                {
                                    context ="در حال سفر";
                                    showNotificatio();

                                }else if(status.equals("1"))
                                {
                                    context ="راننده پیدا شد";
                                    showNotificatio();


                                }else if(status.equals("5"))
                                {


                                }else if(status.equals("-10"))
                                {
                                    context ="سفر لفو شد";
                                    showNotificatio();
                                    stopService(new Intent(getApplicationContext(),IService.class));
                                }
                            }catch (JSONException E){E.printStackTrace();}

                        }
                    });

                }
            });
        }catch (URISyntaxException E){E.printStackTrace();}
        onTaskRemoved(intent);
        return  START_STICKY;
    }
    private void showNotificatio()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            Notification notification = new NotificationCompat.Builder(getApplicationContext(),channel_id)
                    .setSmallIcon(R.drawable.logo_back)
                    .setContentTitle("یه دربست")
                    .setContentText(context)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .build();
            notificationManagerCompat.notify(1,notification);




        }else
        {
            mbuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.logo_back).setContentTitle("یه دربست").setContentText(context);
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(getApplicationContext(),IService.class);
            PendingIntent contentintent  = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            mbuilder.setContentIntent(contentintent);
            manager.notify(0,mbuilder.build());
        }
    }


    public IService() {
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
