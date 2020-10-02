package com.example.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class SignInActivity extends AppCompatActivity {

    TextView tvDangNhap, tvDangKyNgay;
    FButton btnDangNhap;
    EditText edtSDTDangNhap, edtMatKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        AnhXa();
        OnClick();

        //init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog thông báo chờ đăng nhập
                final ProgressDialog dialog = new ProgressDialog(SignInActivity.this);
                dialog.setMessage("Chờ xíu nha...");
                dialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check if user not exist in database
                        if (dataSnapshot.child(edtSDTDangNhap.getText().toString()).exists()){
                            //get user infomation
                            dialog.dismiss();
                            User user = dataSnapshot.child(edtSDTDangNhap.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtMatKhau.getText().toString())){
                                Toast.makeText(SignInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignInActivity.this, "Mật khẩu không đúng!!!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            dialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Không kết nối được database!!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void OnClick() {
        tvDangKyNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void AnhXa() {
        tvDangKyNgay = findViewById(R.id.tvdangkyngay);
        tvDangNhap = findViewById(R.id.tvdangnhap);
        btnDangNhap = findViewById(R.id.btndangnhap);
        edtMatKhau = findViewById(R.id.edtmatkhaudangnhap);
        edtSDTDangNhap = findViewById(R.id.edtsdtdangnhap);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvDangNhap.setTypeface(typeface);
    }
}