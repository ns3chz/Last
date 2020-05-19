package com.zch.last.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.zch.last.listener.OnProgressStatesListener;
import com.zch.last.model.Progress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class UtilFiles {
    private static final String TAG = "FileTools";

    private static final int FILE_WRITE_SIZE = 1024;
    private static final byte[] WRITE_BUFFER = new byte[1024];
    private static MediaScannerConnection mediaScannerConnection;

    public static String getSdPath() {
        return Environment.getExternalStorageDirectory() + File.separator;
    }

    /**
     * @return 判断是否有sd卡，并可以读写
     */
    public static boolean hasSDmounted() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            UtilLogger.logE(TAG, "there is no sd card !!!");
            return false;
        }
        return true;
    }

    /**
     * @return 有无写的权限
     */
    public static boolean hasWpermiss(@Nullable Context context) {
        if (context != null && !(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            UtilLogger.logE(TAG, "has no write permission , please get WRITE_EXTERNAL_STORAGE permission !!!");
        }
        return true;
    }

    /**
     * @return 有无读的权限
     */
    public static boolean hasRpermiss(Context context) {
        boolean b;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            //TODO fit
            b = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        if (!b) {
            UtilLogger.logE(TAG, "has no read permission , please get READ_EXTERNAL_STORAGE permission !!!");
        }
        return b;
    }

    @Nullable
    public static File createDir(String path) {
        return createDir(new File(path));
    }

    /**
     * <p>在SD卡上创建文件夹
     *
     * @param file sd卡下的文件夹
     */
    @Nullable
    public static File createDir(File file) {
        try {
            if (file == null) return null;
            if (file.exists() && file.isDirectory()) {
                return file;
            }
            boolean result = file.mkdirs();
            if (!result) {
                throw new Exception("can't mkdirs File,path = " + file.getPath());
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static File createFile(String path) {
        return createFile(new File(path));
    }

    /**
     * <p>在SD卡上创建文件,当有同名文件夹时创建失败
     *
     * @param file sd卡下文件
     */
    @Nullable
    public static File createFile(File file) {
        try {
            if (file == null) return null;
            if (file.exists()) {
                if (file.isFile()) {
                    return file;
                }
            }
            File parentFile = file.getParentFile();
            if (parentFile == null) {
                //找不到父文件夹，或者父文件夹路径错误，则创建失败
//                UtilLogger.logE(TAG, "can't find parent File,path= " + path);
                throw new Exception("can't find parent File,path = " + file.getPath());
            }
            if (!parentFile.exists()) {
                //当父文件夹不存在时，创建文件夹
                boolean mkdirs = parentFile.mkdirs();
                if (!mkdirs) {
                    //创建父文件夹失败,返回null
//                    UtilLogger.logE(TAG, "can't mkdirs parent File,path= " + path);
                    throw new Exception("can't mkdirs parent File,path = " + file.getPath());
                }
            }

            boolean result = file.createNewFile();
            if (!result) {
                //创建文件夹失败,返回null
//                UtilLogger.logE(TAG, "can't create File,path= " + path);
                throw new Exception("can't create File,path= " + file.getPath());
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(String path) {
        File file = new File(path);
        return delete(file);
    }

    /**
     * <p>若该路径指向一个文件夹，则删除文件夹及文件夹中所有文件
     * <p>若该路径指向一个文件,则删除该文件
     *
     * @return 该文件删除, 或该文件夹清空是否成功
     */
    private static boolean delete(File file) {
        try {
            if (file == null) {
                UtilLogger.logE(TAG, "file field is null !!!");
                return true;
            }
            if (!file.exists()) {
                //若不存在，返回成功
                return true;
            }
            if (!file.isDirectory()) {
                return file.delete();
            }
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }
            boolean result = true;
            for (int i = 0; i < childFiles.length; i++) {
                File child = childFiles[i];
                if (child == null) continue;
                result = result && delete(child);
            }
            if (result) {
                //文件夹已清空
                return file.delete();//删除文件夹
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取文件/文件夹大小
     *
     * @return error:-1
     */
    public static int getFileSize(File file) {
        if (file == null) {
            return -1;
        }
        int s;
        if (file.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                s = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
                s = -1;
            }
        } else {
            s = -1;
        }
        return s;
    }

    /**
     * @return
     */
    public static int getFileSizesByFullPath(String path) {
        File file = new File(path);
        return getFileSize(file);
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String path) {
        if (path == null || path.length() == 0) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    /**
     * 保存bitmap图片到sd卡
     */
    public static boolean saveBitmap2Img(Bitmap bitmap, String filePath, @IntRange(from = 0, to = 100) int quality) {
        try {
            if (bitmap == null) {
                return false;
            }
            File file = createFile(filePath);
            if (file == null || !file.exists()) {
                UtilLogger.logE("saveBitmap2Img", "create dir failed!!!");
                return false;
            }
            FileOutputStream out;
            out = new FileOutputStream(filePath);

            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
            // Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    public static File write2SDFromPath(String goalPath, String sourcePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(sourcePath);
            return write2SDFromInput(goalPath, fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中,
     */
    @Nullable
    public static File write2SDFromInput(String filePath, InputStream input) {
        return write2SDFromInput(filePath, input, null);
    }

    @Nullable
    public static File write2SDFromInput(String filePath, InputStream input, OnProgressStatesListener<Object> listener) {
        File file = createFile(filePath);
        if (file == null || !file.exists()) {
            UtilLogger.logE(TAG, "write2SDFromInput createFile failed !!!");
            if (listener != null) {
                try {
                    listener.onException(null, new Exception("文件不存在且创建失败"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            int available = input.available();
            byte[] buffer = new byte[FILE_WRITE_SIZE];
            int length;
            Progress<Object> progress = new Progress<>(available);
            while ((length = (input.read(buffer))) != -1) {
                output.write(buffer, 0, length);
                if (listener != null) {
                    progress.refresh(length);
                    listener.onProgress(progress);
                }
            }
            output.flush();
            if (listener != null) {
                listener.onComplete(true, "成功");
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            UtilLogger.logE("write2SDFromInput", "write 2 filePath: " + filePath);
            if (listener != null) {
                try {
                    listener.onException(null, new Exception("异常:" + e.getMessage()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readText(String filePath) {
        if (filePath == null || filePath.length() == 0) return null;
        return readText(new File(filePath));
    }

    @Nullable
    public static String readText(File file) {
        if (!file.exists()) return null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    public static boolean writeText(String filePath, @Nullable String text) {
        return writeText(filePath, text, false);
    }

    public static boolean writeText(String filePath, @Nullable String text, boolean append) {
        return writeText(filePath, text, append, false);
    }

    public static boolean writeText(String filePath, @Nullable String text, boolean append, boolean sync) {
        File file = createFile(filePath);
        if (file == null || !file.exists()) {
            UtilLogger.logE(TAG, "write2SDFromInput createFile failed !!!");
            return false;
        }
        OutputStreamWriter fileWriter = null;
        try {
            FileOutputStream fos = new FileOutputStream(file, append);
            fileWriter = new OutputStreamWriter(fos);
            if (text == null) {
                fileWriter.write("");
            } else {
                fileWriter.write(text);
            }
            fileWriter.flush();
            if (sync) {
                fos.getFD().sync();//
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * @throws IOException 清空文件
     */
    public static boolean clearFile(String filePath) throws IOException {
        return writeText(filePath, null);
    }


    public static boolean copyFile(String from, String to) throws Exception {
        if (from == null || from.length() == 0 || to == null || to.length() == 0 || from.equals(to)) {
            return false;
        }
        return copyFile(new File(from), new File(to));
    }

    /**
     * 拷贝文件夹
     *
     * @param fromFile 来自
     * @param toFile   去处
     */
    public static boolean copyFile(File fromFile, File toFile) {
        try {
            if (fromFile == null || toFile == null) {
                return false;
            }
            if (!fromFile.exists()) {
                return false;
            }
            File createToFile;//创建的tofile
            if (fromFile.isDirectory()) {//复制文件夹
                createToFile = createDir(toFile);
                if (createToFile == null) {
                    return false;
                }
                // 要复制的文件目录
                // 如果存在则获取当前目录下的全部文件 填充数组
                File[] currentFiles = fromFile.listFiles();
                if (currentFiles == null || currentFiles.length == 0) {
                    //                Logger.w(TAG, "copyDir dir's child files list is null ...");
                    return true;
                }

                boolean result = true;
                // 遍历要复制该目录下的全部文件
                for (int i = 0; i < currentFiles.length; i++) {
                    File curFile = currentFiles[i];
                    if (curFile == null || !curFile.exists()) continue;
                    result = result && copyFile(curFile, new File(toFile.getPath() + File.separator + curFile.getName()));
                }
                return result;//返回
            } else {//复制文件
                createToFile = createFile(toFile);
                if (createToFile == null) {
                    return false;
                }
                InputStream fosfrom = null;
                OutputStream fosto = null;
                try {
                    fosfrom = new FileInputStream(fromFile);
                    fosto = new FileOutputStream(createToFile);
                    int c;
                    while ((c = fosfrom.read(WRITE_BUFFER)) != -1) {
                        fosto.write(WRITE_BUFFER, 0, c);
                    }
                    fosto.flush();
                    return true;
                } catch (Exception e) {
                    UtilLogger.logE(TAG, "copy file : " + createToFile.getPath() + " failed !!!");
                    return false;
                } finally {
                    try {
                        if (fosto != null) {
                            fosto.close();
                        }
                        if (fosfrom != null) {
                            fosfrom.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //复制文件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return 0:简名，1:包括后缀，2:后缀名，3：简名前的路径
     */
    @NonNull
    public static String[] getFileNameByPath(String path) {
        if (path == null || path.length() == 0) {
            return new String[]{"", "", "", ""};
        }
        path = clearSlash(path);
        int xieIndex = path.lastIndexOf("/");
        String headPath = path.substring(0, xieIndex + 1);
        String normalName = path.substring(xieIndex + 1);
        int dianIndex = normalName.lastIndexOf(".");
        String zhuiName;
        if (dianIndex == -1) {
            zhuiName = "";
            dianIndex = normalName.length();
        } else {
            zhuiName = normalName.substring(dianIndex);
        }
        String simpleName = normalName.substring(0, dianIndex);
        return new String[]{simpleName, normalName, zhuiName, headPath};
    }

    public static String changeSuffix(String oldPath, String suffix) {
        oldPath = clearSlash(oldPath);
        int index = oldPath.lastIndexOf(File.separator);
        String headPath, simpleName;
        if (index == -1) {
            headPath = "";
            simpleName = oldPath;
        } else {
            headPath = oldPath.substring(0, index + 1);
            simpleName = oldPath.substring(index + 1);
        }
        index = simpleName.lastIndexOf(".");
        if (index == -1) {
            return headPath + simpleName + suffix;
        }
        return headPath + simpleName.substring(0, index) + suffix;
    }

    /**
     * 去掉末尾的斜杠
     */
    @NonNull
    public static String clearSlash(String str) {
        String res = str;
        if (res == null || res.length() == 0) {
            return "";
        }
        if (res.endsWith("/") || res.endsWith("\\")) {
            res = res.substring(0, res.length() - 1);
            res = clearSlash(res);
        }
        return res;
    }

    public static String getFileType(String path) {
        return getFileNameByPath(path)[2];
    }

    /*
     * 是否为图片格式
     */
    public static boolean isImage(String type) {
        if (type.equals("png")) {
            return true;
        } else if (type.equals("jpg")) {
            return true;
        } else if (type.equals("jpeg")) {
            return true;
        } else if (type.equals("bmp")) {
            return true;
        }

        return false;
    }

    public static boolean isGif(String type) {
        if (type.equals("gif")) {
            return true;
        }
        return false;
    }

    /*
     * 是否为音频格式
     */
    public static boolean isVoice(String type) {
        if (type.equals("mp3")) {
            return true;
        } else if (type.equals("WMA")) {
            return true;
        } else if (type.equals("WAV")) {
            return true;
        } else if (type.equals("ASF")) {
            return true;
        } else if (type.equals("FLAC")) {
            return true;
        } else if (type.equals("APE")) {
            return true;
        }

        return false;
    }

    /*
     * 是否为视频格式
     */
    public static boolean isVideo(String type) {
        if (type.equals("mp4")) {
            return true;
        } else if (type.equals("MPEG-1")) {
            return true;
        } else if (type.equals("MPEG-2")) {
            return true;
        } else if (type.equals("MPEG-4")) {
            return true;
        } else if (type.equals("AVI")) {
            return true;
        } else if (type.equals("MOV")) {
            return true;
        } else if (type.equals("ASF")) {
            return true;
        } else if (type.equals("WMV")) {
            return true;
        } else if (type.equals("NAVI")) {
            return true;
        } else if (type.equals("3GP")) {
            return true;
        } else if (type.equals("MKV")) {
            return true;
        } else if (type.equals("FLV")) {
            return true;
        } else if (type.equals("RMVB")) {
            return true;
        } else if (type.equals("WebM")) {
            return true;
        }

        return false;
    }

    public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
        String packageName = context.getApplicationContext().getPackageName();
        String authority = packageName + ".fileProvider";
        return FileProvider.getUriForFile(context, authority, file);
    }

    /**
     * 项图库中添加一个bitmap
     *
     * @param context
     * @param bitmap
     */
    public synchronized static void updateGallery(Context context, Bitmap bitmap, String title, String desc) {
        String result = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, title, desc);
        Uri imageUri = Uri.parse(result);
        final String filePath = getFilePathByContentResolver(context, imageUri);
        //                msc.scanFile("/sdcard/image.jpg", "image/jpeg");
//                msc.disconnect();
        mediaScannerConnection = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {

            public void onMediaScannerConnected() {
                if (mediaScannerConnection != null) {
                    mediaScannerConnection.scanFile(filePath, "image/jpeg");
                }
            }

            public void onScanCompleted(String path, Uri uri) {
                if (mediaScannerConnection != null) {
                    mediaScannerConnection.disconnect();
                }
            }
        });
        mediaScannerConnection.connect();
    }

    /**
     * 通过文件的uri，获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByContentResolver(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (cursor == null) {
            throw new IllegalArgumentException("Query on " + uri + " returns null result.");
        }
        try {
            if (cursor.getCount() != 1 || !cursor.moveToFirst()) {

            } else {
                filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            cursor.close();
        }
        return filePath;
    }

    /**
     * gradle 中需要 targetSdkVersion >=26否则 canRequestPackageInstalls始终为false
     * 安装apk
     */
    public static void installApk(Context context, String filePath) {
        try {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                boolean canRequestPackageInstalls = context.getPackageManager().canRequestPackageInstalls();
//                if (!canRequestPackageInstalls) {
//                    Toaster.toast("请打开安装权限");
//                    SystemUI.openInstallPermissionSetting(context);
//                    return;
//                }
//            }

            File apkFile = new File(filePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", apkFile);
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            UtilToast.toast("安装失败，请检查权限或手动安装");
        }
    }
}