package com.example.kevin.bestbite;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView uploadImage;
    private TextView uploadPhoto;
    private TextView message;
    private ImageView cameraIcon;
    private RelativeLayout cameraLayout;
    private ImageView closeIcon;
    private Uri selectedImage;

    private EditText title;
    private EditText method;
    private EditText time;

    private String recipeTitle;
    String recipeMethod;
    Integer cookingTime;
    Recipe recipe;

    private boolean recipeCreated = false;






    Bitmap bmp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        if( getSupportActionBar()!= null  ){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        uploadImage = (ImageView)findViewById(R.id.uploadImage);
        uploadImage.setVisibility(View.INVISIBLE);

        /*
        //upload Button
        Button buttonLoadImage = (Button) findViewById(R.id.button_upload);
        buttonLoadImage.setTextColor(Color.parseColor("#c40401"));
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // String recipe = recipe_enter.getText().toString();
                // String description  = recipe_enter.getText().toString();
                //intent.putExtra("recipe", recipe);
                // intent.putExtra("description", description);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        */

        cameraLayout = (RelativeLayout)findViewById(R.id.cameraLayout);

        cameraLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // String recipe = recipe_enter.getText().toString();
                // String description  = recipe_enter.getText().toString();
                //intent.putExtra("recipe", recipe);
                // intent.putExtra("description", description);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
    }

    public void close(final View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createRecipe(final View view){
        title   = (EditText) findViewById(R.id.title);
        method  = (EditText) findViewById(R.id.methodInput);
        time    = (EditText) findViewById(R.id.timeInput);

        recipeTitle  = title.getText().toString();
        recipeMethod = method.getText().toString();
        cookingTime  = Integer.parseInt(time.getText().toString());

        try {
            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData = stream.toByteArray();

            String method = "Step 1: Prepare noodle\nStep 2: Make soup\n";
            recipe = new Recipe(recipeTitle, recipeMethod, cookingTime, imageData, 0);

            RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
            // SQLiteDatabase database = recipedb.getWritableDatabase();

            recipedb.addRecipe(recipe);

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipeCreated = true;
        Toast.makeText(getApplicationContext(), "Recipe created", Toast.LENGTH_LONG).show();
    }

    public void retrieve(final View view){
        Log.d("recipeCreated", String.valueOf(recipeCreated));
        if(recipeCreated){
            Intent intent = new Intent(this, ViewRecipeActivity.class);
            intent.putExtra("recipeTitle",recipeTitle);
            startActivity(intent);

            //// startActivity(intent);
            String recipeInfo=null;

            uploadImage   =  (ImageView) findViewById(R.id.uploadImage);
            uploadPhoto   =  (TextView) findViewById(R.id.uploadPhoto);
            message       =  (TextView) findViewById(R.id.message);
            cameraIcon    =  (ImageView) findViewById(R.id.cameraIcon);
            cameraLayout  =  (RelativeLayout)findViewById(R.id.cameraLayout);

            uploadImage.setVisibility(View.VISIBLE);
            cameraIcon.setVisibility(View.INVISIBLE);
            uploadPhoto.setVisibility(View.INVISIBLE);
            message.setVisibility(View.INVISIBLE);

            int width = cameraLayout.getWidth();
            int height = cameraLayout.getHeight();
            Log.d("ImageView Height", String.valueOf(uploadImage.getMeasuredHeight()) );
            Log.d("ImageView Width", String.valueOf(uploadImage.getWidth()) );
            //int height = width * ( uploadImage.getHeight() / uploadImage.getWidth() ) ;

            /////RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams)uploadImage.getLayoutParams();
            //parms.width = width;
            //parms.height = height;
            //parms.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            //parms.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            //parms.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            //parms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            uploadImage.setLayoutParams(parms);


            RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
            //SQLiteDatabase database = recipedb.getWritableDatabase();
            //Recipe recipe = recipedb.getRecipeByTitle(recipe.getTitle());
            String reciptInfo = recipe.getTitle() +"\n" + recipe.getMethod() + "\n" + "Cook times: " + recipe.getCookTimes();

            byte[] photo = recipe.getImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage= BitmapFactory.decodeStream(imageStream);

            uploadImage.setImageBitmap(theImage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //String recipe2 = data.getStringExtra("recipe");
        //String description2 = data.getStringExtra("description");
        uploadImage   =  (ImageView) findViewById(R.id.uploadImage);
        uploadPhoto   =  (TextView) findViewById(R.id.uploadPhoto);
        message       =  (TextView) findViewById(R.id.message);
        cameraIcon    =  (ImageView) findViewById(R.id.cameraIcon);
        cameraLayout  =  (RelativeLayout)findViewById(R.id.cameraLayout);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            //recipeTextView.setText(recipe2);
            //descrTextView.setText(description2);

            selectedImage = data.getData();
            Log.d("image uriPath", selectedImage.getPath());

            uploadImage.setVisibility(View.VISIBLE);
            cameraIcon.setVisibility(View.INVISIBLE);
            uploadPhoto.setVisibility(View.INVISIBLE);
            message.setVisibility(View.INVISIBLE);

            try {
                uploadImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));


                /* Nov 27 1150pm
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageData = stream.toByteArray();

                String method = "Step 1: Prepare noodle\nStep 2: Make soup\n";
                Recipe recipe = new Recipe("Ramen Noodle", method, 15, imageData, 3);

                RecipeSQLiteHelper recipedb = new RecipeSQLiteHelper(this);
                RecipeSQLiteHelper db = new RecipeSQLiteHelper(this);
                SQLiteDatabase database = db.getWritableDatabase();
                db.addRecipe(recipe);
                */
            }catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //////// bmp = (Bitmap)data.getExtras().get("data");  ///////////////
             /////////////  imageView.setImageBitmap(bmp); //////////////


            /*
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Log.d("image uriPath", selectedImage.getPath());

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            picturePath = picturePath.replace("/storage", "file://storage");
            Log.d("picturePath string", picturePath);
            */

            /*
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));

            }catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            //ImageLoader.getInstance().displayImage(path, stub);
        }


    }
}