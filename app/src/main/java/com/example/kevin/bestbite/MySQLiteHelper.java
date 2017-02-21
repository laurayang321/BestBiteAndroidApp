package com.example.kevin.bestbite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;


/**
 * Created by Jing on 07-Nov-2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FoodImgDB.db";
    private static final String TABLE_FOODIMG = "foodImg";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        String databasePath = context.getDatabasePath("FoodImgDB.db").getPath();
        Log.i("My DB path is: ", databasePath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_FOODIMAGE_TABLE = "CREATE TABLE foodImg ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image_name TEXT)";
        db.execSQL(CREATE_FOODIMAGE_TABLE);

    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath("FoodImgDB.db");
        return dbFile.exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS foodImg");
        // create fresh foodImage table
        this.onCreate(db);
    }

    public int getFoodImgCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FOODIMG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }



    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMG_NAME = "image_name";

    private static final String[] COLUMNS = {KEY_ID,KEY_IMG_NAME};

    public void addImage(String imgName){
        Log.d("addImage", imgName);
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_IMG_NAME, imgName); // get image name

        // 3. insert
        db.insert(TABLE_FOODIMG, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public void dropTable(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "foodImg");
    }

    public String getImage(int id){
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_FOODIMG, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        String image = "";
        if (cursor != null){
            if(cursor.moveToFirst()){
                image = cursor.getString(cursor.getColumnIndex("image_name"));
                Log.d("image_name - ", image);
            }
        }


        // 4. build book object
        //String image = cursor.getString(1);
        //book.setId(Integer.parseInt(cursor.getString(0)));
        //book.setTitle(cursor.getString(1));
        //book.setAuthor(cursor.getString(2));

        Log.d("getFoodImg("+id+")", image);
        // 5. return book
        db.close();
        return image;
    }


}
