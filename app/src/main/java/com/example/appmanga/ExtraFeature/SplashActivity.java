package com.example.appmanga.ExtraFeature;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmanga.MainActivity;
import com.example.appmanga.R;

public class SplashActivity extends AppCompatActivity {

    private static int tg_cho =3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        loadData();
        //bắt đầu mainactivity sau 5 giây

            }

    private void loadData() {
        if (IsNetwork.IsNetworkAvailable(this)) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();
                }
            },tg_cho);
        }
        else {

            Intent intent = new Intent(SplashActivity.this, CheckInternet.class);
            startActivity(intent);
            finish();
        }
    }
}
