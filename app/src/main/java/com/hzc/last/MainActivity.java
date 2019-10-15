package com.hzc.last;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hzc.widget.picker.file.FilePicker;
import com.hzc.widget.picker.file.FilePickerUiParams;

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
                FilePicker.build(MainActivity.this, 1)
                        .setPickFileType(FilePickerUiParams.PickType.FOLDER)
                        .setSinglePick(new FilePicker.OnSinglePickListener() {
                            @Override
                            public void pick(@Nullable String path) {
                                tvResult.setText("单选 : \n" + path);
                                Log.d("FilePicker", "pick path = " + path);
                            }

                            @Override
                            public void cancel() {
                                tvResult.setText("取消选择了");
                            }
                        })
//                        .setMultiPick(new FilePicker.OnMultiPickListener() {
//                            @Override
//                            public void pick(@Nullable List<String> pathList) {
//                                StringBuilder path = new StringBuilder("多选：\n");
//                                if (pathList != null) {
//                                    for (int i = 0; i < pathList.size(); i++) {
//                                        path.append(pathList.get(i)).append("\n\n");
//                                    }
//                                } else {
//                                    path.append("空的");
//                                }
//                                tvResult.setText(path.toString());
//                            }
//
//                            @Override
//                            public void cancel() {
//                                tvResult.setText("取消选择了");
//                            }
//                        })
                        .open();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FilePicker.onActivityResult(this, requestCode, resultCode, data);
    }
}
