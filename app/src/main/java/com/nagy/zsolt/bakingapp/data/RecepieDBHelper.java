package com.nagy.zsolt.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecepieDBHelper  extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "recepie.db";

    private static final int DATABASE_VERSION = 1;

    public RecepieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECEPIE_TABLE = "CREATE TABLE " + RecepieContract.RecepieEntry.TABLE_NAME + " (" +
                RecepieContract.RecepieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecepieContract.RecepieEntry.COLUMN_RECEPIE_ID + " TEXT NOT NULL, " +
                RecepieContract.RecepieEntry.COLUMN_RECEPIE_NAME + " TEXT NOT NULL" +
                "); ";

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + RecepieContract.IngredientEntry.TABLE_NAME + " (" +
                RecepieContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecepieContract.IngredientEntry.COLUMN_RECEPIE_NAME + " TEXT NOT NULL, " +
                RecepieContract.IngredientEntry.COLUMN_RECEPIE_QUANTITY + " TEXT NOT NULL," +
                RecepieContract.IngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL," +
                RecepieContract.IngredientEntry.COLUMN_INGREDIENT + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_RECEPIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecepieContract.RecepieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecepieContract.IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}