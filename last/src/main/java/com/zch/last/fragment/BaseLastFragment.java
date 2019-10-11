package com.zch.last.fragment;

import android.os.Build;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.zch.last.utils.UtilThread;

abstract class BaseLastFragment extends Fragment {


    @LayoutRes
    protected abstract int layoutRes();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    public void finish() {
        finish(0);
    }

    public void finish(long delay) {
        if (getActivity() != null) {
            if (delay <= 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (getActivity() != null) {
                        getActivity().finishAfterTransition();
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            } else {
                UtilThread.runOnUiThread(delay, new Runnable() {
                    @Override
                    public void run() {
                        finish(0);
                    }
                });
            }
        }
    }


}
