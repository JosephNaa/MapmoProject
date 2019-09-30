package com.example.mapmo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String memo_table =
                "CREATE TABLE memo(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT NOT NULL DEFAULT '제목 없음'," +
                        "start_date TEXT NOT NULL," +
                        "finish_date TEXT NOT NULL," +
                        "location TEXT NOT NULL," +
                        "latitude TEXT NOT NULL," +
                        "lontitude TEXT NOT NULL," +
                        "pushCheck TEXT NOT NULL," +
                        "allCheck INTEGER );";

        String content_able =
                "CREATE TABLE content(" +
                        "id_ INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "id INTEGER NOT NULL," +
                        "content TEXT NOT NULL," +
                        "checking INTEGER DEFAULT 0," +
                        "FOREIGN KEY(id)" +
                        "REFERENCES memo_table(id) ON DELETE CASCADE);";

        String radius_table =
                "CREATE TABLE radius(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "radius INTEGER NOT NULL);";




        //외래키 활성화
        db.execSQL("PRAGMA foreign_key = 1");

        db.execSQL(memo_table);
        db.execSQL(content_able);
        db.execSQL(radius_table);
    }


    //나중에 테이블 수정 시 사용
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memo");
        db.execSQL("DROP TABLE IF EXISTS content");
        db.execSQL("DROP TABLE IF EXISTS radius");
        onCreate(db);
    }
}