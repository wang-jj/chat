package com.example.dell.chat.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dell.chat.R;
import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.bean.MyApplication;
import com.example.dell.chat.bean.PersonalState;
import com.example.dell.chat.model.Chat.ChatModelImpl;
import com.example.dell.chat.model.Message.MessageModelImpl;
import com.example.dell.chat.view.ChatActivity;
import com.example.dell.chat.view.CommentActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by courageface on 2018/5/9.
 */

public class NewMessage {
    public static void createNewMessageNofity(String user_name, Chat chat){
        NotificationManager notificationManager= (NotificationManager) MyApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
        Intent intent=new Intent(MyApplication.getContext(), ChatActivity.class);
        intent.putExtra("chat",chat);
        String content = " ";
        if(chat.getType()==1){
            content = new ChatModelImpl().messageCutter(chat.getContent());
        }
        else{
            content = "图片消息";
        }
        Log.e("new message","notify");
        PendingIntent pendingIntent=PendingIntent.getActivities(MyApplication.getContext(),0,new Intent[]{intent},0);
        Notification notification=new NotificationCompat.Builder(MyApplication.getContext())
                .setContentTitle("收到一条新消息")
                .setContentText(user_name+":"+content)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.icon_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0,500})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLights(Color.GREEN,1500,1500)
                .build();
        notificationManager.notify(2,notification);
    }
}
