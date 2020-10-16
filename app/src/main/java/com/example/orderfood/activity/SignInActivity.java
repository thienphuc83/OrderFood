package com.example.orderfood.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class SignInActivity extends AppCompatActivity {

    TextView tvDangNhap, tvDangKyNgay, tvQuenMatKhau;
    FButton btnDangNhap;
    EditText edtEmailDangNhap, edtMatKhau;
    ProgressBar progressBarDanhNhap;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        AnhXa();
        auth = FirebaseAuth.getInstance();


        tvDangKyNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailDangNhap.getText().toString();
                String pass = edtMatKhau.getText().toString();
                if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass)){
                    Toast.makeText(SignInActivity.this, "Điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else{
                    DangNhap(email,pass);
                }
            }
        });


    }

    private void DangNhap(String email, String pass) {
        progressBarDanhNhap.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AnhXa() {
        tvDangKyNgay = findViewById(R.id.tvdangkyngay);
        tvDangNhap = findViewById(R.id.tvdangnhap);
        tvQuenMatKhau = findViewById(R.id.tvquamanhinhquenmatkhau);
        btnDangNhap = findViewById(R.id.btndangnhap);
        edtMatKhau = findViewById(R.id.edtmatkhaudangnhap);
        edtEmailDangNhap = findViewById(R.id.edtemaildangnhap);
        progressBarDanhNhap = findViewById(R.id.progressbardangnhap);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvDangNhap.setTypeface(typeface);
    }
}