package com.example.dell.chat.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dell.chat.model.database.DbSchema.ChatListTable;

/**
 * Created by courageface on 2018/4/8.
 */

public class SQLHelper extends SQLiteOpenHelper{


    public static final String CREATE_MSG = "create table MSG ("+
            " id integer ,"+
            ChatListTable.Cols.UUID+ " text primary key, " +
            ChatListTable.Cols.NAME+" text, "+
            ChatListTable.Cols.IMAGE+" integer)";

    public static final String CREATE_CHAT = "create table CHAT ("+
            " id integer ,"+
            ChatListTable.Cols.UUID+ " text primary key, " +
            ChatListTable.Cols.NAME+" text, "+
            ChatListTable.Cols.IMAGE+" integer)";

    private Context mContext;

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_MSG);
        db.execSQL(CREATE_CHAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
