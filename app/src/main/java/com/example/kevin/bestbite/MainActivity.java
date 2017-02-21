package com.example.kevin.bestbite;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static int id_foodImg = 0; //generate the image id show on homepage
    private static final String TAG = "MainActivity";
    private ArrayList<Recipe> recipeList;
    private int countRecipes = 0;
    private int idRecipe;
    private Recipe recipe;
    private int[] allRecipesId;
    private boolean notMockup = false;
    private TextView recipeTitle;
    private ImageView timerIcon;
    public void onInit() {
        MySQLiteHelper db = new MySQLiteHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        db.dropTable();
        db.onCreate(database);

        db.addImage("strawberry");
        db.addImage("blueberry");
        db.addImage("ramen");
        db.addImage("vege");
        db.addImage("cake");
        db.addImage("ingredients");
        db.addImage("desert");
        db.addImage("friesjpg");
        db.addImage("burger");
        Log.d(TAG, "onInit");
    }

    public void onInitRecipeDB() {
        RecipeSQLiteHelper db = new RecipeSQLiteHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        db.dropTable();
        db.onCreate(database);
        Log.d(TAG, "onInitRecipeDB");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerIcon = (ImageView) findViewById(R.id.timer);
        timerIcon.setVisibility(View.INVISIBLE);
        MySQLiteHelper db = new MySQLiteHelper(this);
        //check if db exist
        boolean dbExist = db.doesDatabaseExist(getApplicationContext(), "hello");
        Log.d("Main onCreate existDB1 ", String.valueOf(dbExist));
        if(!dbExist) {
            Log.d(TAG, "!dbExist");
            onInit();
        }

        final RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
        //check if db exist
        boolean dbRecipeExist = recipedb.doesDatabaseExist(getApplicationContext(), "hello");
        Log.d("Main existDBRecipe ", String.valueOf(dbRecipeExist));
        if(!dbRecipeExist) {
            Log.d(TAG, "!dbRecipeExist");
            onInitRecipeDB();
        }

        recipeList = recipedb.getAllrecipes();
        countRecipes = recipeList.size();
        recipeTitle = (TextView) findViewById(R.id.recipeTitle);
        recipeTitle.setVisibility(View.INVISIBLE);

        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                recipeTitle.setVisibility(View.VISIBLE);
                timerIcon.setVisibility(View.VISIBLE);
                notMockup = true;
                Log.d("Count all recipe", String.valueOf(countRecipes));
                //ImageView mainImage = (ImageView)findViewById(R.id.imageView);
                TextView recipeTitle = (TextView) findViewById(R.id.recipeTitle);
                TextView cookTime = (TextView) findViewById(R.id.cookTime);
                allRecipesId = recipedb.getAllrecipesId();

                idRecipe = getRandom(allRecipesId);
                recipe = recipedb.getRecipe(idRecipe);
                // store only image name in database(means not store "R.drawable.image") like "image".

                byte[] photo = recipe.getImage();
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap theImage= BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(theImage);
                recipeTitle.setText(recipe.getTitle());
                cookTime.setText("  Cook time: " + recipe.getTime() + " mins");
            }

            public void onSwipeLeft() {
                notMockup = true;
                recipeTitle.setVisibility(View.VISIBLE);
                timerIcon.setVisibility(View.VISIBLE);
                Log.d("Count all recipe", String.valueOf(countRecipes));
                //ImageView mainImage = (ImageView)findViewById(R.id.imageView);
                TextView recipeTitle = (TextView) findViewById(R.id.recipeTitle);
                TextView cookTime = (TextView) findViewById(R.id.cookTime);
                allRecipesId = recipedb.getAllrecipesId();

                idRecipe = getRandom(allRecipesId);
                recipe = recipedb.getRecipe(idRecipe);
                // store only image name in database(means not store "R.drawable.image") like "image".

                byte[] photo = recipe.getImage();
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap theImage= BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(theImage);
                recipeTitle.setText(recipe.getTitle());
                cookTime.setText("   Cook time: " + recipe.getTime() + " mins");
            }
        });
        imageView.bringToFront();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(), "image clickable", Toast.LENGTH_LONG).show();
                    Log.i("MyTag","Image button is pressed, visible in LogCat");;
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    public void viewRecipe(final View view){
        Toast.makeText(getApplicationContext(), "recipe clickable", Toast.LENGTH_LONG).show();
    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Determine if Action bar item was selected. If true then do corresponding action.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;

            case R.id.action_profile:
                startActivity(new Intent(this, ViewAllRecipeActivity.class));
                return true;
            case R.id.action_upload:
                startActivity(new Intent(this, UploadActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeRecipe(final View view){
        Toast.makeText(this, "image clickable", Toast.LENGTH_LONG).show();
    }

    public void like(final View view) {
        //user cannot update cook times on homepage mockup image
        if(notMockup){
            RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
            recipedb.updateCookTimes(recipe, true);
            Toast.makeText(getApplicationContext(), "Cook times +1!", Toast.LENGTH_LONG).show();
            Log.d("cooked times", String.valueOf(recipe.getCookTimes()));
        }
    }

    public void unlike(final View view) {
        //user cannot update cook times on homepage mockup image
        if(notMockup) {
            if (recipe.getCookTimes() > 0) {
                Toast.makeText(getApplicationContext(), "Cook times -1!", Toast.LENGTH_LONG).show();
                RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
                recipedb.updateCookTimes(recipe, false);
                Log.d("cooked times", String.valueOf(recipe.getCookTimes()));
            } else {
                Toast.makeText(getApplicationContext(), "Cooked 0 times", Toast.LENGTH_LONG).show();
            }

        }

    }

}
