package com.zch.last.core;

import android.app.Application;
import android.os.Looper;
import android.os.Process;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.model.clickview.TextViewClick;
import com.zch.last.utils.UtilAlert;
import com.zch.last.utils.UtilFiles;
import com.zch.last.utils.UtilLogger;
import com.zch.last.utils.UtilSystem;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;

class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static UncaughtExceptionHandler INSTANCE;

    static void catchCrash(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new UncaughtExceptionHandler(application);
        }
    }

    @Nullable
    private final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private final String TAG = "UncaughtExceptionHandler";
    @NonNull
    private final Application APP;
    @Nullable
    private final String DIRECTORY;


    private UncaughtExceptionHandler(@NonNull Application app) {
        this.APP = app;

        //文件夹路径
        File logsFile;
        if (UtilFiles.hasSDmounted()) {
            logsFile = APP.getExternalFilesDir("logs");
        } else {
            logsFile = new File(APP.getFilesDir(), "logs");
        }
        logsFile = UtilFiles.createDir(logsFile);
        if (logsFile != null) {
            this.DIRECTORY = logsFile.getAbsolutePath();
        } else {
            this.DIRECTORY = null;
        }

        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

        initParams();
    }

    private void initParams() {

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (defaultUncaughtExceptionHandler == null || handleException(t, e)) {
            showTips(t, e);
            //一秒后重启
//            UtilSystem.restartApp(APP, 1000);
        } else {
            defaultUncaughtExceptionHandler.uncaughtException(t, e);
        }
    }

    private void showTips(final Thread t, final Throwable e) {
        //检查是否有“显示在其它应用上层”的权限

        final boolean hasAlertPermission = UtilAlert.checkGlobalAlertPermission(APP);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                if (hasAlertPermission) {
                    showAlert();
                } else {
                    Toast.makeText(APP, "程序即将重启", Toast.LENGTH_LONG).show();
                }

                Looper.loop();
            }
        }).start();

        if (!hasAlertPermission) {
            try {
                Thread.sleep(3000);
            } catch (Exception ignored) {
            }
            UtilSystem.prepareRestartApp(APP, 1000);
            Process.killProcess(Process.myPid());
//            System.exit(1);
        }
    }

    private void showAlert() {
        UtilAlert.showGlobalAlertDefaultAsk(APP, null, "即将推出程序", false,
                new TextViewClick("立即退出") {
                    @Override
                    public void onClick(View v) {
                        Process.killProcess(Process.myPid());
                    }
                },
                new TextViewClick("重新启动") {
                    @Override
                    public void onClick(View v) {
                        UtilSystem.prepareRestartApp(APP, 1000);
                        Process.killProcess(Process.myPid());
                    }
                });
    }

    private boolean handleException(@Nullable Thread t, @Nullable Throwable throwable) {
        if (throwable == null) return false;
        //保存错误日志
        saveError(t, throwable);
        return true;
    }

    private void saveError(@Nullable Thread t, @NonNull Throwable throwable) {
        String threadMsg = null;
        if (t != null) {
            threadMsg = t.toString();
            log(threadMsg);
        }
        if (DIRECTORY != null) {
            String throwableMsg = getThrowableString(throwable);
            StringBuilder builder = new StringBuilder();
            Calendar calendar = Calendar.getInstance();
            builder.append(calendar.get(Calendar.YEAR)).append(".")
                    .append(calendar.get(Calendar.MONTH)).append(".")
                    .append(calendar.get(Calendar.DAY_OF_MONTH)).append("_\n");
            builder.append(threadMsg).append("\n")
                    .append(throwableMsg).append("\n\n");
            UtilFiles.writeText(getErrorLogFile(), builder.toString(), true);
        }

    }

    @NonNull
    private String getThrowableString(@Nullable Throwable t) {
        if (t == null) return "";

//        StringBuilder msg = new StringBuilder(t.toString());
//        StackTraceElement[] stackTrace = t.getStackTrace();
//        for (StackTraceElement element : stackTrace) {
//            msg.append("\n").append(element);
//        }
//        return msg.toString();
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        t.printStackTrace(pw);
        pw.close();
        return writer.toString();
    }

    private String getErrorLogFile() {
        return DIRECTORY + File.separator + "crash.txt";
    }

    private void log(String text) {
        UtilLogger.logE(TAG, text);
    }
}
