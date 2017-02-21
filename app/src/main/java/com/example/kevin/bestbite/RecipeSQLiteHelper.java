package com.example.kevin.bestbite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jing on 26-Nov-2016.
 */

public class RecipeSQLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "RecipeDB.db";
    private static final String TABLE_RECIPE = "Recipe";
    private static final String TAG = "RecipeSQLiteHelper";

    public RecipeSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        String databasePath = context.getDatabasePath("RecipeDB.db").getPath();
        Log.i("My DB path is: ", databasePath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String  CREATE_TABLE_RECIPE = "CREATE TABLE Recipe ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "method TEXT, " +
                "time INTEGER, " +
                "image BLOB, " +
                "cookTimes INTEGER)";
        db.execSQL(CREATE_TABLE_RECIPE);
        Log.d(TAG, "onCreate Recipe table");
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath("RecipeDB.db");
        Log.d(TAG, "doesDatabaseExist");
        return dbFile.exists();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS Recipe");
        Log.d(TAG, "onUpgrade");
        this.onCreate(db);
    }

    public int getRecipeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RECIPE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_METHOD = "method";
    private static final String KEY_TIME = "time";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_COOKTIMES = "cookTimes";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_METHOD,KEY_TIME,KEY_IMAGE,KEY_COOKTIMES};

    public void addRecipe(Recipe recipe){
        Log.d("addRecipe", recipe.getTitle());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, recipe.getTitle());
        values.put(KEY_METHOD, recipe.getMethod());
        values.put(KEY_TIME, recipe.getTime());
        values.put(KEY_IMAGE, recipe.getImage());
        values.put(KEY_COOKTIMES, recipe.getCookTimes());

        // 3. insert
        db.insert(TABLE_RECIPE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        db.close();
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + "Recipe");
    }

    public Recipe getRecipeByTitle(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_RECIPE, // a. table
                        COLUMNS, // b. column names
                        " title = ?", // c. selections
                        new String[] { String.valueOf(title) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        Recipe recipe = new Recipe();
        if (cursor != null){
            if(cursor.moveToFirst()){
                recipe.setTitle( cursor.getString(cursor.getColumnIndex("title")) );
                recipe.setMethod( cursor.getString(cursor.getColumnIndex("method")) );
                recipe.setTime( cursor.getInt(cursor.getColumnIndex("time")) );
                recipe.setImage( cursor.getBlob(cursor.getColumnIndex("image")) );
                recipe.setcookTimes( cursor.getInt(cursor.getColumnIndex("cookTimes")) );
            }
        }

        Log.d("getRecipe("+title+")", recipe.getTitle());
        db.close();
        return recipe;
    }




    public Recipe getRecipe(int id){
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_RECIPE, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        Recipe recipe = new Recipe();
        if (cursor != null){
            if(cursor.moveToFirst()){
                recipe.setTitle( cursor.getString(cursor.getColumnIndex("title")) );
                recipe.setMethod( cursor.getString(cursor.getColumnIndex("method")) );
                recipe.setTime( cursor.getInt(cursor.getColumnIndex("time")) );
                recipe.setImage( cursor.getBlob(cursor.getColumnIndex("image")) );
                recipe.setcookTimes( cursor.getInt(cursor.getColumnIndex("cookTimes")) );
            }
        }

        Log.d("getRecipe("+id+")", recipe.getTitle());
        db.close();
        return recipe;
    }

    public int[] getAllrecipesId(){
        int size = this.getRecipeCount();
        int[] array = new int[size];
        int index = 0;
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        String selectQuery = "SELECT * FROM Recipe";

        Log.d(TAG, "getAllrecipesId");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setTitle( cursor.getString(cursor.getColumnIndex("title")) );
                recipe.setMethod( cursor.getString(cursor.getColumnIndex("method")) );
                recipe.setTime( cursor.getInt(cursor.getColumnIndex("time")) );
                recipe.setImage( cursor.getBlob(cursor.getColumnIndex("image")) );
                recipe.setcookTimes( cursor.getInt(cursor.getColumnIndex("cookTimes")) );
                int id = cursor.getInt((cursor.getColumnIndex(KEY_ID)));

                recipes.add(recipe);
                array[index] = id;
                index++;
            } while (cursor.moveToNext());
        }

        return array;
    }

    public ArrayList<Recipe> getAllrecipes() {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        String selectQuery = "SELECT * FROM Recipe";

        Log.e("select allRecipes", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setTitle( cursor.getString(cursor.getColumnIndex("title")) );
                recipe.setMethod( cursor.getString(cursor.getColumnIndex("method")) );
                recipe.setTime( cursor.getInt(cursor.getColumnIndex("time")) );
                recipe.setImage( cursor.getBlob(cursor.getColumnIndex("image")) );
                recipe.setcookTimes( cursor.getInt(cursor.getColumnIndex("cookTimes")) );

                // adding to todo list
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }

        return recipes;
    }

    /*
 * Deleting a tag
 */
    public void deleteRecipeByTitle(String title, boolean should_delete_all_tag_todos) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting tag
        // check if todos under this tag should also be deleted
        /*
        if (should_delete_all_tag_todos) {
            // get all todos under this tag
            List<Todo> allTagToDos = getAllToDosByTag(tag.getTagName());

            // delete all todos
            for (Todo todo : allTagToDos) {
                // delete todo
                deleteToDo(todo.getId());
            }
        }*/

        // now delete the tag
        db.delete(TABLE_RECIPE, KEY_TITLE + " = ?",
                new String[] { String.valueOf(title) });
    }

    /*
 * Updating a todo
 */
    public int updateCookTimes(Recipe recipe, Boolean increase) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if(increase){
            values.put(KEY_COOKTIMES, recipe.getCookTimes()+1);
        }else{
            if(recipe.getCookTimes()>0){
                values.put(KEY_COOKTIMES, recipe.getCookTimes()-1);
            }
        }
        // updating row
        return db.update(TABLE_RECIPE, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(recipe.getTitle()) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
