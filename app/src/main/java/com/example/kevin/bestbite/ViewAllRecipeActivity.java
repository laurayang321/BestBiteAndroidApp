package com.example.kevin.bestbite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ViewAllRecipeActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mListView = (ListView) findViewById(R.id.recipe_list_view);

        RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
        recipeList = recipedb.getAllrecipes();

        RecipeAdapter adapter = new RecipeAdapter(this, recipeList);
        mListView.setAdapter(adapter);

        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = recipeList.get(position);// 1
                Intent detailIntent = new Intent(context, ViewRecipeActivity.class);// 2
                detailIntent.putExtra("recipeTitle", selectedRecipe.getTitle());// 3
                startActivity(detailIntent);// 4
            }
        });
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Recipe> mRecipesSource;
    public class RecipeAdapter extends BaseAdapter {
        public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
            mContext = context;
            mRecipesSource = recipes;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(ViewAllRecipeActivity.this);
            int count = recipedb.getRecipeCount();
            return count;
        }
        @Override
        public Recipe getItem(int position) {
            return mRecipesSource.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get view for row item
            View rowView = mInflater.inflate(R.layout.list_item_recipe, parent, false);

            ImageView thumbnailImageView = (ImageView) rowView.findViewById(R.id.list_image);
            TextView titleTextView = (TextView) rowView.findViewById(R.id.title);
            TextView methodTextView = (TextView) rowView.findViewById(R.id.description);
            TextView durationView = (TextView) rowView.findViewById(R.id.duration);
            ImageView detailImageView = (ImageView) rowView.findViewById(R.id.detail);

            // 1
            Recipe recipe = (Recipe) getItem(position);// 2
            titleTextView.setText(recipe.getTitle());
            methodTextView.setText(recipe.getMethod());

            byte[] photo = recipe.getImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);
            thumbnailImageView.setImageBitmap(theImage);

            durationView.setText(recipe.getTime() + " mins");// 3

            return rowView;
        }
    }


}


