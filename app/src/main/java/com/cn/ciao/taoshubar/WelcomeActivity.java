package com.cn.ciao.taoshubar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

public class WelcomeActivity extends AppCompatActivity {

    AVUser myUserLog;
    String username;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        myUserLog = (AVUser) getIntent().getParcelableExtra("AVuser");
        String username = myUserLog.getUsername();
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        welcomeText.setText("Welcome, " + username);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intentToMainApplication = new Intent(this, ApplicationMainFace.class);
        intentToMainApplication.putExtra("AVuser", myUserLog);
        startActivity(intentToMainApplication);

        return super.onTouchEvent(event);
    }
}
