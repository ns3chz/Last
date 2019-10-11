package com.zch.last.utils;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zch.last.model.QrCodeBuilder;

import java.util.HashMap;
import java.util.Map;

public class UtileQrcode {
    /**
     * @param content 内容
     * @param builder 构建builder
     * @return bitmap
     */
    @Nullable
    public static Bitmap encodeQRCode(@Nullable String content, @Nullable QrCodeBuilder builder) {
        if (content == null || content.length() == 0) return null;
        if (builder == null) return null;
        if (builder.getWith() < 1) return null;
        if (builder.getHeight() < 1) return null;
        try {
            // 设置字符集编码
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //设置纠错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, builder.getWith(), builder.getHeight(), hints);
            //根据builder生成bitmap
            int[] rectangle = bitMatrix.getEnclosingRectangle();
            if (rectangle == null) return null;
            int codeWidth = rectangle[2];
            int codeHeight = rectangle[3];
            int realWidth = codeWidth + builder.getPadding_left() + builder.getPadding_right();
            int realHeight = codeHeight + builder.getPadding_top() + builder.getPadding_bot();
            int[] pixels = new int[realWidth * realHeight];
            rectangle[0] -= builder.getPadding_left();//优化计算
            rectangle[1] -= builder.getPadding_top();//优化计算
            for (int w = 0; w < realWidth; w++) {
                for (int h = 0; h < realHeight; h++) {
                    if (w < builder.getPadding_left() || w > builder.getPadding_left() + codeWidth ||
                            h < builder.getPadding_top() || h > builder.getPadding_top() + codeHeight) {
                        //边框颜色
                        pixels[h * realWidth + w] = builder.getBgColor();
                    } else if (bitMatrix.get(w + rectangle[0], h + rectangle[1])) {
                        //二维码颜色
                        pixels[h * realWidth + w] = builder.getCodeColor();
                    } else {
                        //非二维码的颜色
                        pixels[h * realWidth + w] = builder.getBgColor();
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(realWidth, realHeight, builder.getBitmapConfig());
            bitmap.setPixels(pixels, 0, realWidth, 0, 0, realWidth, realHeight);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
