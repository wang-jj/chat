package com.example.dell.chat.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.view.CommentActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by wang on 2018/5/8.
 */

public class Notify {
    public static void createCommentNofity(String user_name, PersonalState personalState){
        NotificationManager notificationManager= (NotificationManager) MyApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
        Intent intent=new Intent(MyApplication.getContext(), CommentActivity.class);
        intent.putExtra("personalstate",personalState);
        PendingIntent pendingIntent=PendingIntent.getActivities(MyApplication.getContext(),0,new Intent[]{intent},0);
        Notification notification=new NotificationCompat.Builder(MyApplication.getContext())
                .setContentTitle("收到一条评论")
                .setContentText(user_name+" 评论/回复了你")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.icon_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,1000})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLights(Color.GREEN,1500,1500)
                .build();
        notificationManager.notify(1,notification);
    }
}
