package com.zch.last.utils;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zch.last.model.Progress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

public class UtilFiles {

    public final static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * @param path file path
     * @see UtilFiles#createFile(File)
     */
    public static boolean createFile(String path) {
        try {
            return createFile(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * create file
     *
     * @param file file
     * @return if file exists ,return true
     */
    public static boolean createFile(File file) {
        try {
            if (file.exists()) return true;
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                if (!createDirs(parentFile)) {
                    return false;
                }
            }
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param path path
     * @see UtilFiles#createDirs(File)
     */
    public static boolean createDirs(String path) {
        try {
            return createDirs(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param file file
     * @return created or exist
     */
    public static boolean createDirs(File file) {
        try {
            if (file.exists()) return true;
            return file.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制文件夹
     * 源文件为空或不存在时返回true
     *
     * @param src  源文件
     * @param dest 目标文件
     * @return 结果
     */
    public static boolean copyDir(File src, File dest) {
        if (src == null) return true;
        if (!src.exists()) return true;
        if (src.isFile()) {
            return copyFile(src, dest);
        } else {
            File[] files = src.listFiles();
            if (files == null || files.length == 0) {
                return dest.mkdirs();
            }
            boolean result = true;
            for (File file : files) {
                result = result && copyDir(file, new File(dest, file.getName()));
            }
            return result;
        }
    }

    /**
     * 复制文件
     * 源文件为空或不存在时返回true
     *
     * @param src  源文件
     * @param dest 目标文件
     * @return 结果
     */
    public static boolean copyFile(File src, File dest) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (src == null) return true;
            if (!src.exists()) return true;
            if (dest == null) {
                printStackTraceException("when copy \"" + src.getPath() + "\"，dest file is null !");
                return false;
            }
            if (!dest.exists()) {
                File parentFile = dest.getParentFile();
                if (parentFile == null) {
                    printStackTraceException("when copy \"" + src.getPath() + "\"，dest file \"" + dest.getPath() + "\" parent dir is null !");
                    return false;
                }
                if (!parentFile.exists()) {
                    if (!parentFile.mkdirs()) {
                        printStackTraceException("when copy \"" + src.getPath() + "\"，dest file \"" + dest.getPath() + "\" create parent dir failed !");
                        return false;
                    }
                }
                if (!dest.createNewFile()) {
                    printStackTraceException("when copy \"" + src.getPath() + "\"，dest file \"" + dest.getPath() + "\" createNewFile failed !");
                    return false;
                }
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @see UtilFiles#renameFile(File, String)
     */
    @Nullable
    public static File renameFile(String filePath, String rename) {
        try {
            return renameFile(new File(filePath), rename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param file   f
     * @param rename name
     * @return rename file
     */
    @Nullable
    public static File renameFile(File file, String rename) {
        try {
            File parentFile = file.getParentFile();
            File renameFile = new File(parentFile.getAbsolutePath() + File.separator + rename);
            if (file.renameTo(renameFile)) {
                return renameFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @see UtilFiles#deleteFile(File)
     */
    public static boolean deleteFile(String path) {
        try {
            return deleteFile(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * rename and delete
     *
     * @param file f
     * @return if not exist ,return true
     */
    public static boolean deleteFile(File file) {
        try {
            if (!file.exists()) return true;
            File renameFile = renameFile(file, System.currentTimeMillis() + "");
            if (renameFile == null) {
                //重命名失败，尝试删除源文件
                return file.delete();
            }
            //重命名成功，则表示删除，不关心新文件是否删除
            renameFile.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    public static File flushFile(String path, String text) {
        try {
            return flushFile(new File(path), text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 1.file is exist 2.write success
     *
     * @param file f
     * @param text t
     * @return is success ?
     */
    @Nullable
    public static File flushFile(File file, String text) {
        FileWriter fileWriter = null;
        try {
            if (!file.exists()) {
                boolean create = createFile(file);
                if (!create) {
                    return null;
                }
                //内容为空时，只需创建空文件
                if (text == null || text.length() == 0) {
                    return file;
                }
            } else {
                if (text == null || text.length() == 0) {
                    text = "";
                }
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * read is ,flush os
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param progress     进度信息
     */
    public static boolean writeStream(InputStream inputStream, OutputStream outputStream, Progress<?> progress) {
        try {
            long count = 0;
            long totalWriteToEndPosition = -1;//写入结束位置，-1：写完
            byte[] BYTE;
            if (progress != null) {
                if (progress.needWriteLenth == 0) {
                    return true;
                }
                if (progress.startReadPosition < 0) {
                    progress.startReadPosition = 0;
                } else if (progress.startReadPosition > 0) {
                    count = inputStream.skip(progress.startReadPosition);
                }
                if (progress.needWriteLenth > 0) {
                    totalWriteToEndPosition = count + progress.needWriteLenth;
                }
                BYTE = new byte[progress.WRITE_SIZE];
            } else {
                BYTE = new byte[1024];
            }

            int length;
            while ((length = inputStream.read(BYTE)) != -1) {
                if (progress != null) {
                    //判断是否到达指定结束大小
                    if (totalWriteToEndPosition > 0) {
                        if (totalWriteToEndPosition <= count) {
                            break;
                        } else if (totalWriteToEndPosition < count + length) {
                            length = (int) (totalWriteToEndPosition - count);
                        }
                    }
                }
                count += length;
                outputStream.write(BYTE, 0, length);
                if (count % 4096 == 0) {
                    outputStream.flush();
                }

                if (progress != null) {
                    progress.refresh(length);
                    if (totalWriteToEndPosition == count) {
                        break;
                    }
                }
            }
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param inputStream      输入
     * @param randomAccessFile 输出文件
     * @param progress         进度信息
     * @return 是否没出异常
     */
    public static boolean writeStream(InputStream inputStream, RandomAccessFile randomAccessFile, Progress<?> progress) {
        try {
            long totalWriteToEndPosition = -1;//写入结束位置，-1：写完
            byte[] BYTE;
            if (progress != null) {
                if (progress.needWriteLenth == 0) {
                    return true;
                }
                if (progress.isUseStartReadPosition && progress.startReadPosition > 0) {//开始读取位置
                    progress.startReadPosition = inputStream.skip(progress.startReadPosition);
                }
                if (progress.startWritePosition > 0) {//开始写入位置
                    randomAccessFile.seek(progress.startWritePosition);
                }
                if (progress.needWriteLenth > 0) {
                    //计算结束为止
                    totalWriteToEndPosition = progress.needWriteLenth + progress.startReadPosition;
                } else {
                    //设置需要写入的长度
                    progress.needWriteLenth = progress.totalSize - progress.startReadPosition;
                }
                progress.currentWritedSize = progress.startReadPosition;
                BYTE = new byte[progress.WRITE_SIZE];
            } else {
                BYTE = new byte[1024];
            }
            int length;
            while ((length = inputStream.read(BYTE)) != -1) {
                if (progress != null) {
                    //判断是否到达指定结束读取位置
                    if (totalWriteToEndPosition > 0) {
                        if (totalWriteToEndPosition <= progress.startReadPosition) {
                            break;
                        } else if (totalWriteToEndPosition < progress.startReadPosition + length) {
                            length = (int) (totalWriteToEndPosition - progress.startReadPosition);
                        }
                    }
                }
                randomAccessFile.write(BYTE, 0, length);

                if (progress != null) {
                    //刷新开始读写位置，用于断点续写
                    progress.startReadPosition += length;
                    progress.startWritePosition += length;

                    progress.refresh(length);
                    if (totalWriteToEndPosition == progress.startReadPosition) {
                        break;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param inputStream in
     * @return string
     */
    @NonNull
    public static String stream2String(InputStream inputStream) {
        if (inputStream == null) return "";
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private static final String REG_IMAGE = "^(?:.+\\.|\\.?)(?:png|jpg|jpeg|bmp)$";

    /**
     * @param type 类型
     * @return 是否为图片格式
     */
    public static boolean isImage(String type) {
        return UtilCom.matches(type, REG_IMAGE, Pattern.CASE_INSENSITIVE);
    }

    private static final String REG_GIF = "^(?:.+\\.|\\.?)gif$";

    /**
     * @param type 类型
     * @return 是否为gif格式
     */
    public static boolean isGif(String type) {
        return UtilCom.matches(type, REG_GIF, Pattern.CASE_INSENSITIVE);
    }

    private static final String REG_AUDIO = "^(?:.+\\.|\\.?)(?:mp3|wma|wav|asf|flac|ape)$";

    /**
     * @param type 类型
     * @return 是否为音频格式
     */
    public static boolean isAudio(String type) {
        return UtilCom.matches(type, REG_AUDIO, Pattern.CASE_INSENSITIVE);
    }

    private static final String REG_VIDEO = "^(?:.+\\.|\\.?)(?:mp4|mpeg-1|mpeg-2|mpeg-4|avi|mov|asf|wmv|navi|3gp|mkv|flv|rmvb|webm)$";

    /**
     * @param type 类型
     * @return 是否为视频格式
     */
    public static boolean isVideo(String type) {
        return UtilCom.matches(type, REG_VIDEO, Pattern.CASE_INSENSITIVE);
    }


    /**
     * 给文件的文件名添加后缀
     *
     * @param filePath fp
     * @param suffix   s
     * @return res
     */
    @NonNull
    public static String addFileSuffix(String filePath, String suffix) {
        if (filePath == null || filePath.length() == 0) {
            return "" + suffix;
        }
        if (suffix == null || suffix.length() == 0) {
            return filePath;
        }
        int pointIndex = filePath.lastIndexOf(".");
        if (pointIndex == -1) {
            return filePath + suffix;
        }
        String before = filePath.substring(0, pointIndex);
        String last = filePath.substring(pointIndex);
        return before + suffix + last;
    }

    /**
     * @param text 在控制台打印异常
     */
    private static void printStackTraceException(String text) {
        Exception exception = new Exception(text);
        exception.printStackTrace();
    }

    public static void main(String[] a) {
        String str = "123456";
        System.out.println(str.substring(0, 1));
        System.out.println(str.substring(0, 2));
        System.out.println(str.substring(0, 3));
        System.out.println(str.substring(0, 4));
        System.out.println(str.substring(0, 5));
        System.out.println(str.substring(0, str.length()));
    }
}
