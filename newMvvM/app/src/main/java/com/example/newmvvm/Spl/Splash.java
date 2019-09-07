package com.example.newmvvm.Spl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newmvvm.MainActivity;
import com.example.newmvvm.Move.Save_Info;
import com.example.newmvvm.R;
import com.example.newmvvm.VP.CustomPagerAdapter;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class Splash extends AppCompatActivity {

    private Button btn;
    private TextView txt;
    private ImageView next;
    private CustomPagerAdapter adapter;
    int pos = 0;
    public static int c;
    private Save_Info saveInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        adapter=new CustomPagerAdapter(this);
        btn = findViewById(R.id.splash_btn);
        txt = findViewById(R.id.splash_txt);
        next=findViewById(R.id.row_lst_img);


        saveInfo=new Save_Info(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo.SplashSave("ok");
                startActivity(new Intent(Splash.this, MainActivity.class));
            }
        });

        ViewPager viewPager =  findViewById(R.id.viewpager);

        viewPager.setAdapter(new CustomPagerAdapter(this));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos+=1;
                if (pos==3)
                {
                    txt.setText("بعدا");
                    next.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                }else {
                    next.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.GONE);
                    txt.setText("بستن");
                }
                viewPager.setCurrentItem(pos);
            }
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pos=position;
                if (position==3)
                {
                    txt.setText("بعدا");
                    next.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                }else {
                    next.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.GONE);
                    txt.setText("بستن");
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //Toast.makeText(Splash.this, c+"", Toast.LENGTH_SHORT).show();
        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(viewPager);
    }

    public void ext(View view) {
        Toasty.warning(getApplicationContext(),"No!",Toasty.LENGTH_LONG).show();
    }
}
