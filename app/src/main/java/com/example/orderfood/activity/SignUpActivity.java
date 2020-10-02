package com.example.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.R;
import com.example.orderfood.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.hoang8f.widget.FButton;

public class SignUpActivity extends AppCompatActivity {

    TextView tvDangKy;
    EditText edtSDTDangKy, edtTenDangKy, edtMatKhau;
    FButton btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AnhXa();

        //init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog thông báo chờ đăng nhập
                final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage("Chờ xíu nha...");
                dialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //check đã có tài khoản hay chưa
                        if (dataSnapshot.child(edtSDTDangKy.getText().toString()).exists()){
                            dialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Số điện thoại này đã đăng ký!", Toast.LENGTH_SHORT).show();
                        }else {
                            dialog.dismiss();
                            //lấy dữ liệu gán vào user
                            User user = new User(edtTenDangKy.getText().toString(),edtMatKhau.getText().toString());
                            //gán dữ liệu user vào cho key sdt
                            table_user.child(edtSDTDangKy.getText().toString()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    private void AnhXa() {
        tvDangKy = findViewById(R.id.tvdangky);
        edtSDTDangKy = findViewById(R.id.edtsdtdangky);
        edtTenDangKy = findViewById(R.id.edttendangky);
        edtMatKhau = findViewById(R.id.edtmatkhaudangky);
        btnDangKy = findViewById(R.id.btndangky);

        //set font tv
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvDangKy.setTypeface(typeface);
    }
}