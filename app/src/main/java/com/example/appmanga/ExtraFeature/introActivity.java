package com.example.appmanga.ExtraFeature;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.appmanga.Login;
import com.example.appmanga.R;
import com.example.appmanga.Register;


public class introActivity extends AppCompatActivity {

        ViewPager viewPager;
        LinearLayout heading;
        Button button;
        TextView[] dots;
        TextView textView;

        SliderAdapter sliderAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_intro);
            viewPager = findViewById(R.id.viewpager);
            heading = findViewById(R.id.dotslayout);
            textView = findViewById(R.id.textlogin);
            //gọi slideradapter
            sliderAdapter = new SliderAdapter(this);
            viewPager.setAdapter(sliderAdapter);
            button = findViewById(R.id.start);
            addDots(0);
            viewPager.addOnPageChangeListener(changeListener);

        }
        // click vào chữ bạn đã có tài khoản đăng nhập dê
        public void Txtlogin(View view)
        {
            Intent intent = new Intent(introActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        // ấn vào tham gia miễn phi
        public void Start(View view){
            Intent intent = new Intent(introActivity.this, Register.class);
            startActivity(intent);
            finish();
        }

        // dotclass
        private void addDots(int position){
            dots = new TextView[3];
            heading.removeAllViews();

            for(int i = 0; i < dots.length; i++){
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);

                heading.addView(dots[i]);
            }
            if(dots.length>0)
            {
                dots[position].setTextColor(getResources().getColor(R.color.dot_color));
            }
        }
        ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }

