package com.example.socialapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Khai báo views
    EditText edtEmail, edtPassword, edtRePassword;
    Button btnRegis;
    TextView tvhaveAccount;

    //Progressbar - hiển thị trong khi Đăng ký
    ProgressDialog progressDialog;

    //Khai báo User hiện tại của FirebaseAuth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Actionbar và Tiêu đề
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tạo tài khoản");

        //Nút trở về
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Khởi tạo views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        tvhaveAccount = findViewById(R.id.tvHaveAccount);
        btnRegis = findViewById(R.id.btnLogin_MainActivity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng ký tài khoản người dùng...");

        //Khởi tạo User
        firebaseAuth = FirebaseAuth.getInstance();

        //Xử lý Đăng ký
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Email + Password
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String rePassword = edtRePassword.getText().toString().trim();

                //Validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //Thông báo lỗi, focus edtEmail
                    edtEmail.setError("Email không hợp lệ");
                    edtEmail.setFocusable(true);
                } else if (password.length() < 6) {
                    //Thông báo lỗi, focus edtPassword
                    edtPassword.setError("Mật khẩu không ngắn hơn 6 kí tự");
                    edtPassword.setFocusable(true);
                } else if (!password.equals(rePassword)) {
                    //Thông báo lỗi, focus edtRePassword
                    edtRePassword.setError("Mật khẩu nhập lại không khớp");
                    edtRePassword.setFocusable(true);
                } else {
                    //Đăng ký
                    registerUser(email, password);
                }
            }
        });

        //Xử lý Đã có tài khoản
        tvhaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password) {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //User uid và email
                            String email = user.getEmail();
                            String uid = user.getUid();

                            //Sử dụng Hashmap lưu thông tin User khi đăng ký thành công
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");
                            hashMap.put("onlineStatus", "Online");
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            hashMap.put("cover", "");

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Đã đăng ký thành công tài khoản: " + user.getEmail(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        //Trở vê activity trước
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}