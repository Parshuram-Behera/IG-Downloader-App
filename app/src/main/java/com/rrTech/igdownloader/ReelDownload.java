package com.rrTech.igdownloader;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;


public class ReelDownload extends AppCompatActivity {


    String URL = "NULL";
    VideoView PlayVideo;
    EditText UserReelUrl;
    TextView LoadReel;
    TextView DwnReel;
    LinearLayout reel_box;
    String Reel_URL = "1";
    private Uri PlayableUri;
    private MediaController mediaController ;
    ProgressDialog progressDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reel_download);


        UserReelUrl = findViewById(R.id.user_reel_url);
        LoadReel = findViewById(R.id.load_reel);

        DwnReel = findViewById(R.id.dwn_reel);
        DwnReel.setVisibility(View.INVISIBLE);
        reel_box = (LinearLayout) findViewById(R.id.reel_box);
        reel_box.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(ReelDownload.this);

        PlayVideo = findViewById(R.id.play_reel);


        mediaController = new MediaController(this);
        mediaController.setAnchorView(PlayVideo);

//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(PlayVideo);



        LoadReel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (UserReelUrl.getText().toString().isEmpty()) {
                    Toast.makeText(ReelDownload.this, "Enter Link", Toast.LENGTH_SHORT).show();
                } else {
                    URL = UserReelUrl.getText().toString().trim();
                    String result = StringUtils.substringBefore(URL, "/?");
                    URL = result + "/?__a=1&__d=dis";
                }

                processdata();

            }
        });




//        LoadReel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                URL = UserReelUrl.getText().toString().trim();
//
//                if (UserReelUrl.equals("NULL")) {
//                    Toast.makeText(ReelDownload.this, "Enter Link", Toast.LENGTH_SHORT).show();
//                } else {
//                    String result = StringUtils.substringBefore(URL, "/?");
//                    URL = result + "/?__a=1&__d=dis";
//                }
//
//                RequestQueue requestQueue = Volley.newRequestQueue(ReelDownload.this);
//
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        GsonBuilder gsonBuilder = new GsonBuilder();
//                        Gson gson = gsonBuilder.create();
//
//                        MainReel mainReel = gson.fromJson(String.valueOf(response), MainReel.class);
//
//                        Reel_URL = mainReel.getVideo_versions().get(0).getUrl();
//
//
//                        PlayableUri = Uri.parse(Reel_URL);
//
//                        PlayVideo.setMediaController(mediaController);
//                        PlayVideo.setVideoURI(PlayableUri);
//                        PlayVideo.requestFocus();
//                        PlayVideo.start();
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//
//                RequestQueue queue = Volley.newRequestQueue(ReelDownload.this);
//                queue.add(jsonObjectRequest);
//
//
//            }
//        });


    }

    private void processdata() {

        progressDialog.setTitle("Getting Reel");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                MainURL mainURL = gson.fromJson(response , MainURL.class);

                Reel_URL = mainURL.getGraphql().getShortcode_media().getVideo_url();
                progressDialog.dismiss();

                PlayableUri = Uri.parse(Reel_URL);

                PlayVideo.setMediaController(mediaController);
                PlayVideo.setVideoURI(PlayableUri);
                PlayVideo.requestFocus();
                PlayVideo.start();

                reel_box.setVisibility(View.VISIBLE);
                DwnReel.setVisibility(View.VISIBLE);

                DwnReel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(ActivityCompat.checkSelfPermission(ReelDownload.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(ReelDownload.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            // this will request for permission when user has not granted permission for the app
                            ActivityCompat.requestPermissions(ReelDownload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                        else{
                            //Download Script
                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(Reel_URL);
                            DownloadManager.Request request1 = new DownloadManager.Request(uri);
                            request1.setVisibleInDownloadsUi(true);
                            request1.setDescription("Reel is Downloading..");
                            request1.setTitle("IG Reel Downloader");
                            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
                            downloadManager.enqueue(request1);

                            reel_box.setVisibility(View.INVISIBLE);
                        }
                    }
                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(ReelDownload.this, "Enter Instagram Reel Link only", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);



    }
}







//
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        LoadReel.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//
//                                            URL = UserReelUrl.getText().toString().trim();
//
////
//                                            if (UserReelUrl.equals("NULL")) {
//                                                Toast.makeText(ReelDownload.this, "Enter Link", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                String result = StringUtils.substringBefore(URL, "/?");
//                                                URL = result + "/?__a=1&__d=dis";
//
//
//                                                StringRequest requests = new StringRequest(URL, new Response.Listener<String>() {
//                                                    @Override
//                                                    public void onResponse(String response) {
//                                                        GsonBuilder gsonBuilder = new GsonBuilder();
//                                                        Gson gson = gsonBuilder.create();
//
//
//                                                        MainReel mainURL = gson.fromJson(response, MainReel.class);
//
////                Reel_URL = mainURL.getVideo_versions().getUrl();
//                                                        String lasturi = mainURL.getVideo_versions().getUrl();
//
//                                                        PlayableUri = Uri.parse(lasturi);
//
//                                                        PlayVideo.setMediaController(mediaController);
//                                                        PlayVideo.setVideoURI(PlayableUri);
//                                                        PlayVideo.requestFocus();
//                                                        PlayVideo.start();
//                                                    }
//                                                }, new Response.ErrorListener() {
//                                                    @Override
//                                                    public void onErrorResponse(VolleyError error) {
//                                                        Toast.makeText(ReelDownload.this, "No Response", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//
//                                                RequestQueue queue = Volley.newRequestQueue(ReelDownload.this);
//                                                queue.add(requests);
//
//
//                                            }
//
//
//                                        }
//
//                                    }
//    }
//}
//
////
////                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
////                            new Response.Listener<JSONObject>() {
////                                @Override
////                                public void onResponse(JSONObject response) {
////
////
////                                    GsonBuilder gsonBuilder = new GsonBuilder();
////                                    Gson gson = gsonBuilder.create();
//////
////                                    MainReel mainReel = new MainReel();
//////
////                                    mainReel = gson.fromJson(String.valueOf(response), MainReel.class);
//////
////                                    Reel_URL = mainReel.getVideo_versions().get(0).getUrl();
//
////                String murl = Reel_URL.getUrl();
////             String   lasturi = mainURL.getVideo_versions().getUrl();
////
////                                    PlayableUri = Uri.parse(Reel_URL);
//////
////                                    PlayVideo.setMediaController(mediaController);
////                                    PlayVideo.setVideoURI(PlayableUri);
////                                    PlayVideo.requestFocus();
////                                    PlayVideo.start();
//
////
////                            GsonBuilder builder = new GsonBuilder();
////                            Gson gson = builder.create();
//
////                            MainReel dataClass = gson.fromJson(String.valueOf(response),MainReel.class );
//
//
////                            String Urll = dataClass.getVideo_versions().getUrl();
////
////                            DataClass data[] = gson.fromJson(String.valueOf(response), DataClass[].class);
//
//
////                            Toast.makeText(ReelDownload.this,Urll, Toast.LENGTH_SHORT).show();
//
//
////
////                                PlayVideo.setMediaController(mediaController);
////                                PlayVideo.setVideoURI();
////                                PlayVideo.requestFocus();
////                                PlayVideo.start();
//
//
////
////                            try {
////                                JSONArray jsonArray = response.getJSONArray("graphql");
////
////                               JSONObject jsonObject =  jsonArray.getJSONObject(0);
////
////                               JSONArray jsonArray1 = jsonObject.getJSONArray("shortcode_media");
////
////                               JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
////
////                               String url = jsonObject1.getString("url");
////
////                                Toast.makeText(ReelDownload.this,url, Toast.LENGTH_SHORT).show();
////
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
//
////                            Toast.makeText(ReelDownload.this,response.toString(), Toast.LENGTH_SHORT).show();
//
//
////                            try {
////                                JSONArray jsonArray = response.getJSONArray("");
//////                                Toast.makeText(ReelDownload.this,jsonArray.toString(), Toast.LENGTH_SHORT).show();
////
////                                Log.d("Test" ,jsonArray.toString());
////
////                                JSONObject jsonObject = jsonArray.getJSONObject(0);
////
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
//
////                            try {
////
////                                JSONObject jsonObject = response.getJSONObject("items");
////
////                                JSONArray jsonArray = jsonObject.getJSONArray("")
////
//////
//////                                JSONArray jsonArray = resJSONObject jsonObject1 = jsonArray.getJSONObject(3);
//////
//////
//////                                String mUrl = jsonObject1.getString("url");
////
//////                                Toast.makeText(ReelDownload.this,mUrl, Toast.LENGTH_SHORT).show();
//////
//////                                PlayVideo.setMediaController(mediaController);
//////                                PlayVideo.setVideoURI(Uri.parse(mUrl));
//////                                PlayVideo.requestFocus();
//////                                PlayVideo.start();
//////
//////                                v.setMediaController(mediaController);
//////                                v.setVideoURI(uri);
//////                                v.requestFocus();
//////                                v.start();
////
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////
////                                }
////                            }, new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error) {
////                            Toast.makeText(ReelDownload.this, "No Hit Response", Toast.LENGTH_SHORT).show();
////                        }
////                    });
////
////                    requestQueue.add(jsonObjectRequest);
////
////                }
////            }
////        });
//
////        DwnReel.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                if (!Reel_URL.equals("1")){
////                    Toast.makeText(ReelDownload.this, "Failed To Download", Toast.LENGTH_SHORT).show();
////                }
////                else {
////
////                    DownloadManager.Request request = new DownloadManager.Request(PlayableUri);
////                    request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_MOBILE |DownloadManager.Request.NETWORK_WIFI);
////                    request.setTitle("Downloading");
////                    request.setDescription("Your Reel Is Downloading.....");
////                    request.allowScanningByMediaScanner();
////                    request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_VISIBLE);
////                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,"",System.currentTimeMillis()+".mp4");
////                    DownloadManager manager = (DownloadManager)getCallingActivity().getSystemServices()
////                }
////            }
////        });
////    }
//
////    private void processdata() {
//////         String lasturi = "1" ;
//////        Reel_URL = "1";
////
////        StringRequest requests = new StringRequest(URL, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                GsonBuilder gsonBuilder = new GsonBuilder();
////                Gson gson = gsonBuilder.create();
////
////
////                MainReel mainURL = gson.fromJson(response, MainReel.class);
////
//////                Reel_URL = mainURL.getVideo_versions().getUrl();
////             String   lasturi = mainURL.getVideo_versions().getUrl();
////
////                PlayableUri = Uri.parse(lasturi);
////
////                PlayVideo.setMediaController(mediaController);
////                PlayVideo.setVideoURI(PlayableUri);
////                PlayVideo.requestFocus();
////                PlayVideo.start();
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(ReelDownload.this, "No Response", Toast.LENGTH_SHORT).show();
////            }
////        });
////
////        RequestQueue queue = Volley.newRequestQueue(this);
////        queue.add(requests);
////    }
//    }
//}