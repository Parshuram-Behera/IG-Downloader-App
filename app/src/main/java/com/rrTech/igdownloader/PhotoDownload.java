package com.rrTech.igdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

public class PhotoDownload extends AppCompatActivity {

    TextView LoadPhoto;
    EditText UserPhotoUrl;
    String URL = "NULL";
    String PhotoURL ;
    LinearLayout photo_box;
    TextView DwnPhoto;
    ImageView Show_Photo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_download);

        LoadPhoto = findViewById(R.id.load_photo);
        UserPhotoUrl = findViewById(R.id.user_photo_url);
        photo_box = (LinearLayout)findViewById(R.id.photo_box);
        DwnPhoto = findViewById(R.id.dwn_photo);
        Show_Photo = findViewById(R.id.show_photo);

        progressDialog = new ProgressDialog(PhotoDownload.this);



        LoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (UserPhotoUrl.getText().toString().isEmpty()) {
                    Toast.makeText(PhotoDownload.this, "Enter Link", Toast.LENGTH_SHORT).show();
                } else {
                    URL = UserPhotoUrl.getText().toString().trim();
                    String result = StringUtils.substringBefore(URL, "/?");
                    URL = result + "/?__a=1&__d=dis";
                }

                processdata();

            }
        });

    }
    private void processdata() {

        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Getting Photo");
        progressDialog.show();

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                MainURL mainURL = gson.fromJson(response , MainURL.class);

                PhotoURL = mainURL.getGraphql().getShortcode_media().getDisplay_url();


//                Show_Photo.setImageDrawable();
//                try {
//                    ImageView i = (ImageView)findViewById(R.id.show_photo);
//                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new Uri().);
//                    i.setImageBitmap(bitmap);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
////                }
//                InputStream URLcontent = (InputStream) new URL(PhotoURL).getContent();
//                Drawable image = Drawable.createFromStream(URLcontent, PhotoURL);

                Picasso.get().load(Uri.parse(PhotoURL)).into(Show_Photo);

                progressDialog.dismiss();


                photo_box.setVisibility(View.VISIBLE);
                DwnPhoto.setVisibility(View.VISIBLE);

                DwnPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(ActivityCompat.checkSelfPermission(PhotoDownload.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(PhotoDownload.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            // this will request for permission when user has not granted permission for the app
                            ActivityCompat.requestPermissions(PhotoDownload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                        else{
                            //Download Script
                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(PhotoURL);
                            DownloadManager.Request request1 = new DownloadManager.Request(uri);
                            request1.setVisibleInDownloadsUi(true);
                            request1.setDescription("Photo is Downloading..");
                            request1.setTitle("IG Photo Downloader");
                            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
                            downloadManager.enqueue(request1);

                            photo_box.setVisibility(View.INVISIBLE);
                        }
                    }
                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(PhotoDownload.this, "Enter Instagram Photo Link only", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);



    }

}