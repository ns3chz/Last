package com.hzc.last;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zch.last.utils.UtilPermission;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tv_result);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilPermission.request(MainActivity.this, 10, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                }, new UtilPermission.OnPermissionRequestListener() {
                    @Override
                    public void listen(int requestCode, @Nullable String[] requestPermissions,
                                       @Nullable List<String> grantedPermissions, @Nullable List<String> deniedPermissions) {
                        tvResult.setText("requestCode : " + requestCode +
                                "\n\n" +
                                "requestPermissions : " + Arrays.toString(requestPermissions) +
                                "\n\n" +
                                "grantedPermissions : \n" + (grantedPermissions == null ? "null" : Arrays.toString(grantedPermissions.toArray())) +
                                "\n\n" +
                                "deniedPermissions : \n" + (deniedPermissions == null ? "null" : Arrays.toString(deniedPermissions.toArray()))
                        );
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.listen(this, requestCode, permissions, grantResults);
    }
}
