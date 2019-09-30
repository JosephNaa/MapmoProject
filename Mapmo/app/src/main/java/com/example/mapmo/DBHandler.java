package com.example.mapmo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHandler {
    DBHelper dbHelper = null;
    SQLiteDatabase db = null;

    //데이터베이스 생성
    public DBHandler(Context context){
        dbHelper = new DBHelper(context, "mapmo.db", null, 10);
    }

    //핸들러 사용
    public static DBHandler open(Context context){
        return new DBHandler(context);
    }

    //데이터베이스 종료
    public void close(){
        dbHelper.close();
    }

    //memo 테이블 레코드 생성
    public long insert_memo(String title, String start, String finish, String loc, String lat, String lon, String phchk, int all){
        //데이터베이스 읽기, 쓰기 가능
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("start_date", start);
        v.put("finish_date", finish);
        v.put("location", loc);
        v.put("latitude", lat);
        v.put("lontitude", lon);
        v.put("pushCheck", phchk);
        v.put("allCheck", all);

        return db.insert("memo", null, v);
    }

    //content 테이블 레코드 생성
    public long insert_content(int id, String content, int checking) {
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("id", id);
        v.put("content", content);
        v.put("checking", checking);

        return db.insert("content", null, v);
    }

    //content 테이블 레코드 생성
    public long insert_radius(int id, int radius) {
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("id", id);
        v.put("radius", radius);

        return db.insert("radius", null, v);
    }

    //memo 테이블 레코드 삭제
    public void delete_memo(int id){
        db = dbHelper.getWritableDatabase();

        db.delete("memo", "id = ? ", new String[]{String.valueOf(id)});
    }

    //content 테이블 레코드 삭제
    public void delete_content(int id_){
        db = dbHelper.getWritableDatabase();

        db.delete("content", "id_ = ? ", new String[]{String.valueOf(id_)});
    }

    //memo 테이블 레코드 수정
    public void update_memo(int id, String title, String start, String finish, String phchk, int all){
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("start_date", start);
        v.put("finish_date", finish);
        v.put("pushCheck", phchk);
        v.put("allCheck", all);

        db.update("memo", v, "id = ?", new String[] {String.valueOf(id)});
    }

    //memo 테이블 레코드 수정
    public void update_radius(int id, int radius){
        db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("radius", radius);

        db.update("radius", v, "id = ?", new String[] {String.valueOf(id)});
    }

    //content 테이블 레코드 수정

    //memo 테이블 조회
    public Cursor select_memo(){
        //데이터베이스 읽기 가능
        db = dbHelper.getReadableDatabase();

        Cursor c = db.query("memo", null, null,null,null,null,null);

        return c;
    }

    //content 테이블 조회
    public Cursor select_content(){
        //데이터베이스 읽기 가능
        db = dbHelper.getReadableDatabase();

        Cursor c = db.query("content", null, null,null,null,null,null);

        return c;
    }

    //radius 테이블 조회
    public Cursor select_radius(){
        //데이터베이스 읽기 가능
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query("radius", null, null,null,null,null,null);
        return c;
    }

}