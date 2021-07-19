package com.gabriel.scannerbandtec;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexao extends SQLiteOpenHelper {

    private static String name = "bdMaquina.db";
    private static int version = 1;


    public Conexao(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table tbMaquina(" +
                "idMaquina integer primary key autoincrement," +
                "hsl varchar(50)," +
                "modelo varchar(50)," +
                "serial varchar(100)," +
                "patrimonio varchar(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
