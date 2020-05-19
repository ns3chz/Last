package com.zch.last.widget.alert;

import android.content.Context;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.zch.last.utils.UtilDevice;
import com.zch.last.vmodel.ViewModelDefaultDialogAsk;

/**
 * 简单的询问弹框
 */
public class GlobalAlertAsk extends GlobalAlert<ViewModelDefaultDialogAsk> {

    public GlobalAlertAsk(@NonNull Context context) {
        super(new ViewModelDefaultDialogAsk(context));
        addGlobalLayoutRunnable(new ViewObserverRunnable(viewModel.dataBinding.containerBtn) {
            @Override
            public void run() {
                viewModel.refreshButtonLines();
            }
        });
    }

    @Override
    public void handleViewParams(@NonNull WindowManager.LayoutParams layoutParams) {
        viewModel.dataBinding.containerBody.setOnClickListener(EMPTY_ONCLICKLISTENER);
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        int[] screenSize = UtilDevice.getScreenSize(viewModel.context);
        ViewGroup.LayoutParams params = viewModel.dataBinding.containerBody.getLayoutParams();
        int size = viewModel.btnViewClicks == null ? 0 : viewModel.btnViewClicks.length;
        if (size > 6) {
            size = 6;
        } else if (size < 3) {
            size = 3;
        }
        params.width = (int) ((1f * size / 6) * (screenSize[0] - 50));
        viewModel.dataBinding.containerBody.setLayoutParams(params);
    }

    @Override
    public void handleWindowLayoutParams(@NonNull WindowManager.LayoutParams layoutParams) {

    }

    @Override
    public void show() {
        super.show();
    }
}
