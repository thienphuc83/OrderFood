package com.example.orderfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    TextView tvTenBu, tvTenNho, tvSdt, tvNgaySinh, tvSua, tvGioiTinh, tvDangXuat, tvDoiMatKhau;
    CircleImageView img, imgsua, imgthoat;

    ImageView imgaccount;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    int REQUEST_CODE_FOLDER = 1;
    int REQUEST_CODE_CAMERA = 2;
    private StorageReference mStorageRef;
    // upload ảnh
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();


        AnhXa();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;
                tvTenBu.setText(user.getName());
                tvTenNho.setText(user.getName());
                tvGioiTinh.setText(user.getGender());
                tvSdt.setText(user.getPhone());
                if (user.getImageURL().equals("default")) {
                    img.setImageResource(R.drawable.cat);
                } else {
                    Picasso.with(getApplicationContext()).load(user.getImageURL()).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khởi tạo dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                //set layout cho dialog
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.update_img_account, null);
                ImageView imgcamera = view.findViewById(R.id.imgcamera);
                ImageView imgcancel = view.findViewById(R.id.imgcancel);
                ImageView imgfolder = view.findViewById(R.id.imgfolder);
                imgaccount = view.findViewById(R.id.imganh);
                FButton btnupload = view.findViewById(R.id.btnupload);
                //mở dialog
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                imgcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                imgfolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_FOLDER);
                    }
                });

                imgcamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    }
                });

                btnupload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        StorageReference mountainsRef = mStorageRef.child("image" + calendar.getTimeInMillis() + ".png");

                        // Get the data from an ImageView as bytes
                        imgaccount.setDrawingCacheEnabled(true);
                        imgaccount.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imgaccount.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(MainActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
//                                        Log.d("AAA", uri + "");
                                        String linkimage = String.valueOf(uri);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("imageURL", linkimage);
                                        databaseReference.updateChildren(hashMap);
                                        alertDialog.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d("AAA", "Failed to get uri");
                                    }
                                });
                                Toast.makeText(MainActivity.this, "Tải ảnh thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.account_menu,menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//        if (id == R.id.changepass){
//            startActivity(new Intent(MainActivity.this, ChangePassActivity.class));
//        }
//        if (id == R.id.dangxuat){
//            firebaseAuth.signOut();
//            startActivity(new Intent(MainActivity.this, SignInActivity.class));
//            finish();
//        }
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgaccount.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgaccount.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AnhXa() {
        tvGioiTinh = findViewById(R.id.tvgioitinh);
        tvNgaySinh = findViewById(R.id.tvngaysinh);
        tvSdt = findViewById(R.id.tvsdt);
        tvSua = findViewById(R.id.tvsua);
        tvTenNho = findViewById(R.id.tvtennho);
        tvTenBu = findViewById(R.id.tvtenbu);
        img = findViewById(R.id.img);
        imgsua = findViewById(R.id.imgsua);
        imgthoat = findViewById(R.id.imgtrove);
        tvDangXuat = findViewById(R.id.tvdangxuat);
        tvDoiMatKhau = findViewById(R.id.tvdoimatkhau);

        imgthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        });
        tvDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangePassActivity.class));
            }
        });


    }
}