package com.example.dell.chat.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell.chat.model.database.SQLHelper;
import com.example.dell.chat.model.database.DbSchema.ChatListTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.dell.chat.bean.Message;

/**
 * Created by courageface on 2018/4/3.
 */

public class MessageLab {
    private static MessageLab sMessageLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MessageLab get(Context context){
        if (sMessageLab == null){
            sMessageLab = new MessageLab(context);
        }
        return sMessageLab;
    }

    //初始化数据库数据
    public MessageLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new SQLHelper(mContext,"Base.db",null, 1).getWritableDatabase();

        for(int i = 0; i<100;i++){
            Message message = new Message();
            message.setName("User "+i);
            message.setMsg("sth");
            message.setImage(i);
            message.setLasttime(i);
            sMessageLab.addMsg(message);
        }
    }


    //获取全体数据以List返回
    public List<Message> getMessages(){
        List<Message> messages = new ArrayList<>();

        Cursor cursor = queryMsg(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                messages.add(getMsgObject(cursor));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return messages;
    }

    public int getImage(UUID id){
        Cursor cursor = queryMsg(ChatListTable.Cols.UUID+" = ?",new String[]{id.toString()});
        return getMsgObject(cursor).getImage();
    }

    public String getName(UUID id){
        Cursor cursor = queryMsg(ChatListTable.Cols.UUID+" = ?",new String[]{id.toString()});
        return getMsgObject(cursor).getName();
    }


    //增加记录
    public void addMsg(Message m){
        ContentValues values = getContentValues(m);

        mDatabase.insert(ChatListTable.NAME, null, values);
    }


    //删除记录
    public void deleteMsg(Message m){
        String uuidString = m.getId().toString();
        mDatabase.delete(ChatListTable.NAME,ChatListTable.Cols.UUID+" = ?",
                new String[] {uuidString});
    }


    //更新记录
    public void updateMsg(Message m){
        String uuidString = m.getId().toString();
        ContentValues values = getContentValues(m);

        mDatabase.update(ChatListTable.NAME,values,ChatListTable.Cols.UUID+" = ?",
                new String[] {uuidString});
    }


    //查询工具
    public Cursor queryMsg(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ChatListTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }


    private static ContentValues getContentValues(Message message){
        ContentValues values = new ContentValues();
        values.put(ChatListTable.Cols.UUID, message.getId().toString());
        values.put(ChatListTable.Cols.NAME, message.getName().toString());
        values.put(ChatListTable.Cols.IMAGE, message.getImage());
        values.put(ChatListTable.Cols.MESSAGE,message.getMsg());
        values.put(ChatListTable.Cols.TIME,message.getLasttime());

        return values;
    }

    public  Message getMsgObject(Cursor cursor){
        Message m = new Message();
        String id = cursor.getString(cursor.getColumnIndex(ChatListTable.Cols.UUID));
        m.setId(UUID.fromString(id));
        m.setName(cursor.getString(cursor.getColumnIndex(ChatListTable.Cols.NAME)));
        m.setImage(cursor.getInt(cursor.getColumnIndex(ChatListTable.Cols.IMAGE)));
        m.setMsg(cursor.getString(cursor.getColumnIndex(ChatListTable.Cols.MESSAGE)));
        m.setLasttime(cursor.getInt(cursor.getColumnIndex(ChatListTable.Cols.TIME)));

        return m;
    }
}
