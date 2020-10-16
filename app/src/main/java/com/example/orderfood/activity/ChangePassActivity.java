package com.example.orderfood.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.hoang8f.widget.FButton;

public class ChangePassActivity extends AppCompatActivity {

    TextView tvDoiMatKhau;
    EditText edtMatKhauCu, edtMatKhauMoi, edtNhapLaiMatKhau;
    FButton btnDoiMatKhau;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);


        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass= edtMatKhauCu.getText().toString();
                String newpass= edtMatKhauMoi.getText().toString();
                String confirnpass= edtNhapLaiMatKhau.getText().toString();
                if (oldpass.isEmpty()||newpass.isEmpty()||confirnpass.isEmpty()){
                    Toast.makeText(ChangePassActivity.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else if(newpass.length()<6){
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu mới phải trên 6 ký tự!", Toast.LENGTH_SHORT).show();
                }else if(!confirnpass.equals(newpass)){
                    Toast.makeText(ChangePassActivity.this, "Nhập lại sai mật khẩu!", Toast.LENGTH_SHORT).show();
                }else {
                    ChagePass(oldpass, confirnpass);
                }
            }
        });




    }

    private void ChagePass(String oldpass, final String confirnpass) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),oldpass);
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updatePassword(confirnpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePassActivity.this, SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thất bại! Thử lại sau!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangePassActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AnhXa() {
        tvDoiMatKhau = findViewById(R.id.tvdoimatkhau);
        edtMatKhauCu = findViewById(R.id.edtmatkhaucu);
        edtMatKhauMoi = findViewById(R.id.edtmatkhaumoi);
        edtNhapLaiMatKhau = findViewById(R.id.edtnhaplaimatkhau);
        btnDoiMatKhau = findViewById(R.id.btndoimatkhau);
        progressBar = findViewById(R.id.progressbardoimatkhau);

        //set font tvlogan
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvDoiMatKhau.setTypeface(typeface);
    }
}