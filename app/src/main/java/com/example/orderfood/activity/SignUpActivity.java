package com.example.orderfood.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import info.hoang8f.widget.FButton;

public class SignUpActivity extends AppCompatActivity {

    TextView tvDangKy;
    EditText edtEmailDangKy, edtTenDangKy, edtMatKhau,edtSDT;
    FButton btnDangKy;
    RadioGroup radioGroup;
    ProgressBar progressBarDangKy;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AnhXa();

        auth = FirebaseAuth.getInstance();

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name =edtTenDangKy.getText().toString();
                final String email =edtEmailDangKy.getText().toString();
                final String pass =edtMatKhau.getText().toString();
                final String phone =edtSDT.getText().toString();
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton select_gender = radioGroup.findViewById(checkedId);
                if (select_gender == null){
                    Toast.makeText(SignUpActivity.this, "Chọn giới tính đi bạn!", Toast.LENGTH_SHORT).show();
                }else {
                    final String gender = select_gender.getText().toString();
                    if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)|| TextUtils.isEmpty(email)||TextUtils.isEmpty(phone)){
                        Toast.makeText(SignUpActivity.this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    }else{
                        DangKy(name,email,pass,phone,gender);
                    }

                }
            }
        });

    }

    private void DangKy(final String name, final String email, String pass, final String phone, final String gender) {
        progressBarDangKy.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    String userId = user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("name", name);
                    hashMap.put("email", email);
                    hashMap.put("phone", phone);
                    hashMap.put("gender", gender);
                    hashMap.put("birthday", "default");
                    hashMap.put("point", "default");
                    hashMap.put("imageURL", "default");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBarDangKy.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    progressBarDangKy.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AnhXa() {
        tvDangKy = findViewById(R.id.tvdangky);
        edtEmailDangKy = findViewById(R.id.edtemaildangky);
        edtTenDangKy = findViewById(R.id.edttendangky);
        edtMatKhau = findViewById(R.id.edtmatkhaudangky);
        edtSDT = findViewById(R.id.edtsdtdangky);
        btnDangKy = findViewById(R.id.btndangky);
        progressBarDangKy = findViewById(R.id.progressbardangky);
        radioGroup = findViewById(R.id.radioButton);

        //set font tv
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        tvDangKy.setTypeface(typeface);
    }
}