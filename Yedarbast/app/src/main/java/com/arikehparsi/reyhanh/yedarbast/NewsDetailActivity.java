package com.arikehparsi.reyhanh.yedarbast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView titr,matn;
    private ImageView img;
    String bb,f="http://185.8.175.151/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        titr=findViewById(R.id.main_matn_titr);
        matn=findViewById(R.id.main_matn_matn);
        img=findViewById(R.id.main_matn_img);


        Bundle ex=getIntent().getExtras();
        titr.setText(ex.getString("titr"));
        matn.setText(ex.getString("matn"));
        bb=ex.getString("img");

        //Toast.makeText(this, bb+"", Toast.LENGTH_LONG).show();
        Picasso.get()
                .load(bb)
                .fit()
                .into(img);



    }



}
