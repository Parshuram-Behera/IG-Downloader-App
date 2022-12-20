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
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

public class IGTVDownload extends AppCompatActivity {



    String URL = "NULL";
    VideoView PlayVideo;
    EditText UserIgTvUrl;
    TextView LoadIgTv;
    TextView DwnIGTv;
    LinearLayout IGTv_box;
    String IGTv_URL = "1";
    private Uri PlayableUri;
    private MediaController mediaController ;
    ProgressDialog progressDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igtv_download);



        UserIgTvUrl = findViewById(R.id.user_igtv_url);
        LoadIgTv = findViewById(R.id.load_igtv);

        DwnIGTv = findViewById(R.id.dwn_IGTv);
        DwnIGTv.setVisibility(View.INVISIBLE);
        IGTv_box = (LinearLayout) findViewById(R.id.IGTv_box);
        IGTv_box.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(IGTVDownload.this);

        PlayVideo = findViewById(R.id.play_IGTv);


        mediaController = new MediaController(this);
        mediaController.setAnchorView(PlayVideo);



        LoadIgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (UserIgTvUrl.getText().toString().isEmpty()) {
                    Toast.makeText(IGTVDownload.this, "Enter Link", Toast.LENGTH_SHORT).show();
                } else {
                    URL = UserIgTvUrl.getText().toString().trim();
                    String result = StringUtils.substringBefore(URL, "/?");
                    URL = result + "/?__a=1&__d=dis";
                }

                processdata();

            }
        });
    }

    private void processdata() {


        progressDialog.setTitle("Getting IGTV");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                MainURL mainURL = gson.fromJson(response , MainURL.class);

                IGTv_URL = mainURL.getGraphql().getShortcode_media().getVideo_url();
                progressDialog.dismiss();

                PlayableUri = Uri.parse(IGTv_URL);

                PlayVideo.setMediaController(mediaController);
                PlayVideo.setVideoURI(PlayableUri);
                PlayVideo.requestFocus();
                PlayVideo.start();

                IGTv_box.setVisibility(View.VISIBLE);
                DwnIGTv.setVisibility(View.VISIBLE);

                DwnIGTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(ActivityCompat.checkSelfPermission(IGTVDownload.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(IGTVDownload.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            // this will request for permission when user has not granted permission for the app
                            ActivityCompat.requestPermissions(IGTVDownload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                        else{
                            //Download Script
                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(IGTv_URL);
                            DownloadManager.Request request1 = new DownloadManager.Request(uri);
                            request1.setVisibleInDownloadsUi(true);
                            request1.setDescription("Reel is Downloading..");
                            request1.setTitle("IG Reel Downloader");
                            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
                            downloadManager.enqueue(request1);

                            IGTv_box.setVisibility(View.INVISIBLE);
                        }
                    }
                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(IGTVDownload.this, "Enter Instagram IGTV Link only", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }
}