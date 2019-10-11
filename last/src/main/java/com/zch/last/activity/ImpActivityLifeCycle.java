package com.zch.last.activity;

import android.content.Intent;

public interface ImpActivityLifeCycle {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
