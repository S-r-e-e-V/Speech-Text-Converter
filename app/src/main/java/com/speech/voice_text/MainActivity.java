package com.speech.voice_text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Speech-Text Converter");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        tableLayout=findViewById(R.id.tablayout);
        tableLayout.addTab(tableLayout.newTab().setText("Speech  to  Text"));
        tableLayout.addTab(tableLayout.newTab().setText("Text  to  Speech"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager=findViewById(R.id.view_pager);
        Adapter adapter=new Adapter(getSupportFragmentManager(),tableLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 12:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    if (!checkPermission()){
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            finishAffinity();
                        }
//                        ActivityCompat.requestPermissions(Splash_screen.this, new String[]{Manifest.permission.CAMERA}, 12);
                    }
                }
        }

    public boolean checkPermission()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }
}
