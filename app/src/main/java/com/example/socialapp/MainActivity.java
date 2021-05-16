package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //Khai báo views
    Button btnRegis_MainActivity, btnLogin_MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Khởi tạo views
        btnLogin_MainActivity = findViewById(R.id.btnLogin_MainActivity);
        btnRegis_MainActivity = findViewById(R.id.btnRegis_MainActivity);

        //Xử lý Đăng ký
        btnRegis_MainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start RegisterActivity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        //Xử lý Đăng nhập
        btnLogin_MainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}