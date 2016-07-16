package com.int403.jabong.gifviewer3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.int403.jabong.gifviewer3.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView im = (ImageView) findViewById(R.id.image);
        Glide.with(this).load("https://www.wired.com/images_blogs/design/2013/09/tumblr_inline_mjx5ioXh8l1qz4rgp.gif").into(im);



    }
}
