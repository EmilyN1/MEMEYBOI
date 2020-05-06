package com.example.memeyboi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static int PERMISSIONCode = 1;
    private static int RESULT_PHOTO = 2;

    private String imagename = "";

    private Button attempt, save, photo;
    private ImageView imageview;
    private TextView pictureid;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONCode);
            }
        }

        attempt = (Button) findViewById(R.id.attempt);
        save = (Button) findViewById(R.id.save);
        photo = (Button) findViewById(R.id.photo);
        imageview = (ImageView) findViewById(R.id.imageview);
        pictureid = (TextView) findViewById(R.id.pictureid);
        text = (EditText) findViewById(R.id.text);
        //main = (RelativeLayout) findViewById(R.id.main);

        save.setEnabled(false);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_PHOTO);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = screenshot(findViewById(R.id.main));
                imagename = "mboi" + System.currentTimeMillis() + ".png";
                saveimage(bitmap, imagename);
            }
        });

        attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureid.setText(text.getText().toString());
                text.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == RESULT_PHOTO && resultCode == RESULT_OK) {
            Uri chosen = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(chosen);
                Bitmap map = BitmapFactory.decodeStream(inputStream);
                imageview.setImageBitmap(map);
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            save.setEnabled(true);
        }
    }

    public static Bitmap screenshot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void saveimage(Bitmap bit, String filename) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        File name = new File(path + "/mboi");
        if (!name.exists()) {
            name.mkdir();
        }
        try {
            File file = new File(path, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } catch (Exception e) {
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            finish();
        }
        return;
    }
}
