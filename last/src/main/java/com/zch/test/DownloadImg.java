package com.zch.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadImg {

    private static final String[] htmlUrl = new String[]{
            "https://user.qzone.qq.com/726711645"
    };

    public static void main(String[] a) {

        for (String html : htmlUrl) {
            decodeHtml(html);
        }

    }

    private static void decodeHtml(String html) {
        ImgThread imgThread = new ImgThread();
        imgThread.start();
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL URL = new URL(html);
            URLConnection urlConnection = URL.openConnection();
            urlConnection.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            Pattern compile = Pattern.compile("\"http://m.qpic.cn/psc\\?.*viewer.*\"");
            Matcher matcher = compile.matcher(builder);
            while (matcher.find()) {
                String group = matcher.group();
                System.out.println(group);
                System.out.println("\n");
//                imgThread.addUrl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static class ImgThread extends Thread {
        private LinkedBlockingQueue<String> urlQueues = new LinkedBlockingQueue<>();

        @Override
        public void run() {
            while (true) {
                try {
                    String url = urlQueues.take();
                    FileOutputStream outputStream = null;
                    InputStream inputStream = null;
                    try {
                        URL URL = new URL(url);
                        URLConnection urlConnection = URL.openConnection();
                        urlConnection.connect();
                        long currentTimeMillis = System.currentTimeMillis();
                        inputStream = urlConnection.getInputStream();
                        int len;
                        byte[] BUFFER = new byte[1024];
                        outputStream = new FileOutputStream(new File("C:\\Users\\MSI1\\Desktop\\image\\" + currentTimeMillis + ".jpg"));
                        while ((len = inputStream.read(BUFFER)) != -1) {
                            outputStream.write(BUFFER, 0, len);
                        }
                        outputStream.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        public void addUrl(String url) {
            if (url == null || url.length() == 0) return;
            try {
                urlQueues.put(url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void downloadImg(String url) {

    }

}
