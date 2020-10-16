package com.example.orderfood.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import info.hoang8f.widget.FButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView tvTitle, tvthongbao;
    EditText edtEmail;
    FButton btnLayMatKhau;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        btnLayMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.fetchSignInMethodsForEmail(edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.getResult().getSignInMethods().isEmpty()){
                            // trả về kết quả là rỗng thì..
                            progressBar.setVisibility(View.GONE);
                            tvthongbao.setText("Email này chưa đăng ký, bạn cần tạo tài khoản mới với email này!");
                        }else {
                            //
                            firebaseAuth.sendPasswordResetEmail(edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()){
                                        tvthongbao.setText("Một email đặt lại mật khẩu đã được gửi đến email của bạn!");
                                        finish();

                                    }else {
                                        // lỗi
                                        tvthongbao.setText("Lỗi! Vui lòng thử lại");

                                    }
                                }
                            });
                        }
                    }
                });

            }
        });


    }

    private void AnhXa() {
        tvTitle =findViewById(R.id.tvquenmatkhau);
        tvthongbao =findViewById(R.id.tvthongbaomatkhaumoi);
        edtEmail= findViewById(R.id.edtemailquenmatkhau);
        btnLayMatKhau = findViewById(R.id.btnlaymatkhau);
        progressBar = findViewById(R.id.progressbarquenmatkhau);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvTitle.setTypeface(typeface);
    }
}