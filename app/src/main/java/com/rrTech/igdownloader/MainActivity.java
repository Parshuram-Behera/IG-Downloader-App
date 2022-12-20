package com.rrTech.igdownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView photo , reel , igtv , about ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photo = findViewById(R.id.tab_photo);
        reel = findViewById(R.id.tab_reel);
        igtv = findViewById(R.id.tab_igtv);
        about = findViewById(R.id.about_tab);



        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n1 = new Intent(MainActivity.this , PhotoDownload.class);
                startActivity(n1);
            }
        });
        reel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n1 = new Intent(MainActivity.this , ReelDownload.class);
                startActivity(n1);
            }
        });
        igtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n1 = new Intent(MainActivity.this , IGTVDownload.class);
                startActivity(n1);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "This Will Be Added Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}