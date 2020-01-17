package com.hzc.last;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.utils.UtilPermission;

import java.util.Arrays;
import java.util.List;

public class Main2Fragment extends Fragment {
    private TextView tvResult;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main2, null);
        initView();
        return rootView;
    }

    private void initView() {
        tvResult = rootView.findViewById(R.id.tv_result);
        rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilPermission.request(Main2Fragment.this, 10, new String[]{
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UtilPermission.listen(this, requestCode, permissions, grantResults);
    }
}
