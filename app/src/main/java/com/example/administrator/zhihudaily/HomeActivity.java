package com.example.administrator.zhihudaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        setTitle("首页",1);
    }
}
