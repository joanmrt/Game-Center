package com.example.gamecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "gamecenter";

    public UserOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user ( _id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);");
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

}
