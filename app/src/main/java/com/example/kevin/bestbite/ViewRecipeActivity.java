package com.example.kevin.bestbite;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class ViewRecipeActivity extends AppCompatActivity {
    private ImageView uploadImage;
    private TextView uploadPhoto;
    private TextView message;
    private ImageView cameraIcon;
    private RelativeLayout cameraLayout;
    private ImageView closeIcon;
    private Uri selectedImage;

    private ImageView recipeImage;
    private TextView viewTitle;
    private TextView viewMethod;
    private TextView viewTime;
    private RelativeLayout imageLayout;

    private String recipeTitle;
    private String recipeMethod;
    private String cookingTime;
    private String cookTimes;

    private Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        /*
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        if( getSupportActionBar()!= null  ){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        //currect view recipe page
        recipeImage = (ImageView) findViewById(R.id.recipeImage);
        viewTitle = (TextView) findViewById(R.id.viewTitle);
        viewMethod = (TextView) findViewById(R.id.viewMethod);
        viewTime = (TextView) findViewById(R.id.viewTime);
        imageLayout = (RelativeLayout) findViewById(R.id.imageLayout);

        uploadPhoto   =  (TextView) findViewById(R.id.uploadPhoto);
        message       =  (TextView) findViewById(R.id.message);
        cameraIcon    =  (ImageView) findViewById(R.id.cameraIcon);

        recipeImage.setVisibility(View.VISIBLE);
        cameraIcon.setVisibility(View.INVISIBLE);
        uploadPhoto.setVisibility(View.INVISIBLE);
        message.setVisibility(View.INVISIBLE);


        Intent intent = getIntent();

        //call from viewAllRecipeList
        recipeTitle = intent.getStringExtra("recipeTitle");

        //get recipe title from search view
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            recipeTitle = intent.getStringExtra(SearchManager.QUERY);
            Log.d("ActionSearch Section!", recipeTitle);
            //use the query to search your data somehow
        }
        Log.d("getIntent title", recipeTitle);


        /*
        int width = imageLayout.getWidth();
        int height = imageLayout.getHeight();
        Log.d("ImageView Height", String.valueOf(imageLayout.getMeasuredHeight()) );
        Log.d("ImageView Width", String.valueOf(imageLayout.getWidth()) );
        */
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams)recipeImage.getLayoutParams();
        recipeImage.setLayoutParams(parms);

        RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
        recipe   =  recipedb.getRecipeByTitle(recipeTitle);

        recipeTitle    =  recipe.getTitle();
        recipeMethod   =  recipe.getMethod();
        cookingTime    = String.valueOf(recipe.getTime()) + " mins" + "\nCooked " + recipe.getCookTimes() + " times\n";
        viewTitle.setText(recipeTitle);
        viewMethod.setText(recipeMethod);
        viewTime.setText(cookingTime);

        byte[] photo = recipe.getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        Bitmap theImage= BitmapFactory.decodeStream(imageStream);
        recipeImage.setImageBitmap(theImage);
    }

    public void back(final View view){
        Intent intent = new Intent(this, ViewAllRecipeActivity.class);
        startActivity(intent);
    }

    public void deleteRecipe(final View view){
        final RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
        recipedb.deleteRecipeByTitle(recipe.getTitle(), true);
        String message = "Deleted recipe: " + recipe.getTitle();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
    }

}
