package com.example.dell.chat.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell.chat.bean.Chat;
import com.example.dell.chat.model.database.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.dell.chat.model.database.DbSchema.*;

/**
 * Created by courageface on 2018/4/8.
 */

public class ChatLab {
    private static ChatLab sChatLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ChatLab get(Context context){
        if (sChatLab == null){
            sChatLab = new ChatLab(context);
        }
        return sChatLab;
    }

    private ChatLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new SQLHelper(mContext,"Base.db",null, 1).getWritableDatabase();

        for(int i = 0; i<100;i++){
            Chat chat = new Chat();
            chat.setDirection(0);
            chat.setImage(i);
            chat.setMsg("sth");
            chat.setTime(i);

            sChatLab.addChat(chat);
        }
    }

    public List<Chat> getChats(){
        List<Chat> chats = new ArrayList<>();

        Cursor cursor = queryChat(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                chats.add(getChatObject(cursor));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return chats;
    }


    public String getMsg(UUID id){
        Cursor cursor = queryChat(ChatTable.Cols.UUID+" = ?",new String[] {id.toString()});
        return getChatObject(cursor).getMsg();
    }

    public int getTime(UUID id){
        Cursor cursor = queryChat(ChatTable.Cols.UUID+" = ?",new String[] {id.toString()});
        return getChatObject(cursor).getTime();
    }

    //增加记录
    public void addChat(Chat c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(ChatTable.NAME, null, values);
    }


    //删除记录
    public void deleteChat(Chat c){
        String uuidString = c.getId().toString();
        mDatabase.delete(ChatTable.NAME,ChatTable.Cols.UUID+" = ?",
                new String[] {uuidString});
    }


    //更新记录
    public void updateChat(Chat c){
        String uuidString = c.getId().toString();
        ContentValues values = getContentValues(c);

        mDatabase.update(ChatTable.NAME,values,ChatListTable.Cols.UUID+" = ?",
                new String[] {uuidString});
    }


    //查询工具
    public Cursor queryChat(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ChatTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    private static ContentValues getContentValues(Chat chat){
        ContentValues values = new ContentValues();
        values.put(ChatTable.Cols.UUID, chat.getId().toString());
        values.put(ChatTable.Cols.CONTENT, chat.getMsg().toString());
        values.put(ChatTable.Cols.TIME,chat.getTime());
        values.put(ChatTable.Cols.DIRECTION,chat.getDirection());

        return values;
    }

    public Chat getChatObject(Cursor cursor) {
        String uuidString = cursor.getString(cursor.getColumnIndex(ChatTable.Cols.UUID));

        Chat c = new Chat(UUID.fromString(uuidString));
        c.setMsg(cursor.getString(cursor.getColumnIndex(ChatTable.Cols.CONTENT)));
        c.setTime(cursor.getInt(cursor.getColumnIndex(ChatTable.Cols.TIME)));
        c.setDirection(cursor.getInt(cursor.getColumnIndex(ChatTable.Cols.DIRECTION)));
        //c.setImage(image);
        //c.setName(name);
        return c;
    }
}
