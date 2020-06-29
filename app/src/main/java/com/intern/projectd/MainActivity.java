package com.intern.projectd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import static android.Manifest.*;
import static android.Manifest.permission.*;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //org,activity cards
    private CardView organ_card, activity_card;

    //camera capture
    Button btn_captureimage;
    ImageView image_display;


    private  static final int REQUEST_CODE_STORAGE_PERMISSION=1;
    private  static final int REQUEST_CODE_SELECT_IMAGE=2;


    private ImageView imageSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageSelected = findViewById(R.id.image_view);

        findViewById(R.id.choose_image_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } {
          //  @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission
                        (getApplicationContext(), READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION );
            }
                else {
                    selectimage();
                }
        };

};
       //button capture
        btn_captureimage =(Button) findViewById(R.id.bn_capture);
        image_display = (ImageView) findViewById(R.id.image_capture);


        btn_captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent ,0);
            }
        });




        //define cards
        organ_card = (CardView) findViewById(R.id.org_card);
        activity_card = (CardView) findViewById(R.id.act_card);
        //add cards
       organ_card.setOnClickListener(this);
        activity_card.setOnClickListener(this);





        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemReselectedListener(navListener);
         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new search_fragment()).commit();


    }

    private void selectimage() {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                 selectimage();
             }else {
                 Toast.makeText(this,"PERMISSION DENIED..",Toast.LENGTH_SHORT).show();
             }
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode== RESULT_OK) {
           if (data!=null) {
               Uri selectedImageUri1 = data.getData();
              // if (selectedImageUri1 != null)
               // try {
                   // InputStream inputStream = getContentResolver().openInputStream(selectedImageUri1);
                    //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //imageSelected.setImageBitmap(bitmap);

                   // File selectedimageFile  = new File(Uri(selectedImageUri1));
                //}catch (Exception  exception){
                  // Toast.makeText(this, exception.getMessage(),Toast.LENGTH_SHORT).show();
               };
           }
        }
        private String getpathFromUri(final Uri contenturi ){
            String filepath;
            Cursor cursor = getContentResolver().query(contenturi, null, null, null, null);
            if (cursor == null) {
                //filepath == contenturi.getPath();
            } else {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex("_data");
                filepath = cursor.getString(index);
                cursor.close();
            }
            //return filepath;
        }


    }

    private BottomNavigationView.OnNavigationItemReselectedListener navListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_Feed:
                            selectedFragment = new feed_fragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new search_fragment();
                            break;
                        case R.id.nav_Donate:
                            selectedFragment = new donate_fragment();
                            break;

                        case R.id.nav_MyProfile:
                            selectedFragment = new Myprofile_fragment();


                            break;
                        case R.id.nav_uploads:
                            selectedFragment = new uploads_fragment();
                            startActivity(new Intent(MainActivity.this, uploads_fragment.class));
                            break;


                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();


                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_v, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.org_card:
                i = new Intent(this, OrgA.class);
                startActivity(i);
                break;
            case R.id.act_card:
                i = new Intent(this, AvtiA.class);
                startActivity(i);
                break;
            default:
                break;


        }
    }
}









