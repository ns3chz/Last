package com.zch.last.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class UtilHTTP {

    /**
     * 将http url分为两段
     * http://baidu.com/ 123456/123
     * ->
     * baseUrl = http://baidu.com/
     * rul =123456/123
     *
     * @param url old url
     * @return 0:baseUrl
     * 1:url
     */
    @NonNull
    public static String[] splitHTTPUrl(String url) {
        if (url == null || url.length() == 0) {
            return new String[]{"", ""};
        }

        try {
            URL urlParse = new URL(url);
            String path = urlParse.getPath();
            if (path == null || path.length() == 0 || path.equalsIgnoreCase("/")) {
                throw new MalformedURLException();
            }
            int indexPath = url.indexOf(path);
            String baseUrl = url.substring(0, indexPath + 1);
            String lastUrl = url.substring(indexPath + 1);
            if (lastUrl.endsWith("/")) {
                lastUrl = lastUrl.substring(0, lastUrl.length() - 1);
            }
            return new String[]{baseUrl, lastUrl};
        } catch (Exception e) {
            if (!url.endsWith("/")) {
                url += "/";
            }
            return new String[]{url, ""};
        }

    }

    /**
     * @return 从url中获取文件名
     */
    @NonNull
    public static String getNameFromUrl(String url) {
        if (url == null || url.length() == 0) {
            return "" + System.currentTimeMillis();
        }
        try {
            int indexGang = url.lastIndexOf("/") + 1;
            return url.substring(indexGang);
        } catch (Exception ignored) {
        }
        return url;
    }

    /**
     * @return 从头文件中获取文件名
     */
    @Nullable
    public static String getNameFromHeaders(Headers headers) {
        if (headers == null || headers.size() == 0) return null;
        String key;
        String value;
        for (int i = 0; i < headers.size(); i++) {
            key = headers.name(i);
            if ("Content-Disposition".equalsIgnoreCase(key)) {
                value = headers.value(i);
                if (value == null || value.length() == 0) break;
                int index = value.indexOf("filename=");
                if (index == -1) break;
                index += 9;
                value = value.substring(index);
                String[] split = value.split(" ");
                if (split.length == 0) break;
                return split[0];
            }
        }
        return null;
    }

    public static String getText(Response<ResponseBody> response) {
//        StringBuilder builder = new StringBuilder("\nResponse -->");
        StringBuilder builder = new StringBuilder("\n");

        int code = response.code();
        builder.append("\ncode : ").append(code);

        boolean successful = response.isSuccessful();
        builder.append("\nsuccessful : ").append(successful);

        String message = response.message();
        builder.append("\nmessage : ").append(message).append("\n");

        ResponseBody body = response.body();
        builder.append("\n").append(getText(body)).append("\n");

        ResponseBody errorBody = response.errorBody();
        builder.append("\n").append(getText(errorBody)).append("\n");

        Headers headers = response.headers();
        builder.append("\n").append(getText(headers)).append("\n");

        okhttp3.Response raw = response.raw();
        builder.append("\n").append(getText(raw)).append("\n");

//        builder.append("\n\n<-- Response");
        return builder.toString();
    }

    @NonNull
    public static String getText(okhttp3.Response response) {
//        StringBuilder builder = new StringBuilder("\nokHttp3.Response -->");
        StringBuilder builder = new StringBuilder("\n");
        try {
            int code = response.code();
            builder.append("\ncode : ").append(code);

            boolean successful = response.isSuccessful();
            builder.append("\nsuccessful : ").append(successful);

            boolean redirect = response.isRedirect();
            builder.append("\nredirect : ").append(redirect);

            Protocol protocol = response.protocol();
            try {
                builder.append("\nprotocol : ").append(protocol.toString());
            } catch (Exception ignored) {
            }

            String message = response.message();
            builder.append("\nmessage : ").append(message);

            Headers headers = response.headers();
            builder.append("\nheaders : ").append(getText(headers));

            ResponseBody responseBody = response.body();
            builder.append("\n").append(getText(responseBody));

            okhttp3.Response networkResponse = response.networkResponse();
            builder.append("\nnetworkResponse : ").append(getText(networkResponse));
        } catch (Exception ignored) {
        }
//        builder.append("\n\n<-- okHttp3.Response");

        return builder.toString();
    }

    @NonNull
    public static String getText(ResponseBody responseBody) {
//        StringBuilder builder = new StringBuilder("\nResponseBody --> ");
        StringBuilder builder = new StringBuilder("\n");
        try {
            long contentLength = responseBody.contentLength();
            builder.append("\ncontentLength : ").append(contentLength);

            MediaType mediaType = responseBody.contentType();
            builder.append("\nmediaType : ");
            if (mediaType != null) {
                builder.append("subType = ").append(mediaType.subtype())
                        .append(" , type = ").append(mediaType.type());
            }
        } catch (Exception ignored) {
        }
//        builder.append("\n\n<-- ResponseBody");

        return builder.toString();

    }

    @NonNull
    public static String getText(Headers headers) {
//        StringBuilder builder = new StringBuilder("\nHeaders --> ");
        StringBuilder builder = new StringBuilder("\n");
        try {
            long byteCount = headers.byteCount();
            builder.append("\nbyteCount : ").append(byteCount);

            Set<String> names = headers.names();
            if (names.size() != 0) {
                String name;
                String value;
                for (int i = 0; i < names.size(); i++) {
                    name = headers.name(i);
                    value = headers.value(i);
                    builder.append("\n").append(name).append(" : ").append(value);
                }
            }
        } catch (Exception ignored) {
        }
//        builder.append("\n\n<-- Headers");
        return builder.toString();
    }

}
