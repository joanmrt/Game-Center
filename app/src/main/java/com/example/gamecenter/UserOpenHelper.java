package com.example.gamecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "gamecenter";

    public UserOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user ( _id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);");
        db.execSQL("CREATE TABLE score ( _id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, score INTEGER, game TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Añade un usuario a la base de datos
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        db.insert("user", null, contentValues);
        db.close();
    }

    // Metodo que devuelve true si encuentra el usuario en la base de datos
    public boolean findUser(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean encontrado = false;
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        String[] selectionArgs = { user.getUsername(), user.getPassword() };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.getCount() > 0){
            encontrado = true;
        }

        cursor.close();
        db.close();

        return encontrado;
    }

    public void addScore(Score score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", score.getName());
        contentValues.put("score", score.getPoints());
        contentValues.put("game", score.getGame());
        db.insert("score", null, contentValues);
        db.close();
    }

    public ArrayList<Score> getScores2048(){
        ArrayList<Score> scoreArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM score WHERE game = ? ORDER BY score DESC LIMIT 10";
        String[] selectionArgs = {"2048"};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    int points = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                    String game = cursor.getString(cursor.getColumnIndexOrThrow("game"));
                    Score score = new Score(name, game, points);
                    scoreArrayList.add(score);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return scoreArrayList;
    }

    public ArrayList<Score> getScoresNonogram(){
        ArrayList<Score> scoreArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM score WHERE game = ? ORDER BY score DESC LIMIT 10";
        String[] selectionArgs = {"nonogram"};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    int points = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                    String game = cursor.getString(cursor.getColumnIndexOrThrow("game"));
                    Score score = new Score(name, game, points);
                    scoreArrayList.add(score);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return scoreArrayList;
    }

}
