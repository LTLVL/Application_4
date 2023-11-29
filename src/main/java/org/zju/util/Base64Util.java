package org.zju.util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64Util {
    public static String convertimgtoBase64(BufferedImage image) {
        String png_base64 = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节
//            png_base64 = new BASE64Encoder().encode(bytes);//jdk1.8写法
            png_base64 = Base64.encodeBase64String(bytes);//JDK11写法
//        String png_base64 = Base64.encodeBase64String(bytes).trim();//转换成base64串
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        } catch (IOException e) {
            e.printStackTrace();
        }
        return png_base64;
    }
}

