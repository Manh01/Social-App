package com.example.socialapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ActionBar actionBar;

    String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Actionbar + tiêu đề
        actionBar = getSupportActionBar();
        actionBar.setTitle("Trang cá nhân");

        //Khởi tạo User
        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        checkUserStatus();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //Xử lý sự kiện Bottom Nav
                    switch (item.getItemId()) {
                        case R.id.nav_home: {
                            actionBar.setTitle("Trang chủ");
                            HomeFragment fragment = new HomeFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content, fragment, "");
                            fragmentTransaction.commit();

                            return true;
                        }
                        case R.id.nav_profile: {
                            actionBar.setTitle("Trang cá nhân");
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction2.replace(R.id.content, fragment2, "");
                            fragmentTransaction2.commit();

                            return true;
                        }
                        case R.id.nav_users: {
                            actionBar.setTitle("Người dùng");
                            UsersFragment fragment3 = new UsersFragment();
                            FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction3.replace(R.id.content, fragment3, "");
                            fragmentTransaction3.commit();

                            return true;
                        }

                        case R.id.nav_chat: {
                            actionBar.setTitle("Trò chuỵện");
                            ChatListFragment fragment4 = new ChatListFragment();
                            FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction4.replace(R.id.content, fragment4, "");
                            fragmentTransaction4.commit();

                            return true;
                        }

                    }
                    return false;
                }
            };

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            mUID = user.getUid();

            //Lưu lại UID của user hiện tại trong Shared Preferences
            SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();
        } else {
            //User chưa đăng nhập -> MainActivity
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        //Kiểm tra User
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }
}