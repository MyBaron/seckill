package com.github.lyrric.service;

import cn.easyproject.easyocr.EasyOCR;
import cn.easyproject.easyocr.ImageType;
import com.sun.corba.se.impl.orb.ParserTable;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.apache.commons.io.FileUtils.writeByteArrayToFile;

public class OCR {


    public static String ocr(String images) throws IOException, TesseractException {
        Tesseract instance = new Tesseract();
        instance.setDatapath("E:\\code\\tessdata");
        byte[] decode = Base64.getDecoder().decode(images);
        File file = new File("E:\\code\\text.png");
        writeByteArrayToFile(file, decode);
        return instance.doOCR(file);
    }


    public static String essyOcr(String images) {
        EasyOCR e = new EasyOCR();
        //直接识别图片内容
        return e.discernAndAutoCleanImage("E:\\\\code\\text.png", ImageType.CAPTCHA_INTERFERENCE_LINE);
    }


    public static void main(String[] args) throws IOException, TesseractException {
        String images = "iVBORw0KGgoAAAANSUhEUgAAAEQAAAAdCAYAAAATksqNAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAASUSURBVFhH7VfBbeQwDNx+8nQtaWFfSQ9pIMjbLQQI0shifynhgO1hHz4OxZGGsq1kDznggMsAtEmJpKgRrU0O1+t1Ubm8Py7Tw9ty6cavp+dlmiaT5+Xcz7mcl+fpcXn7RfuyvD3Af1qeT+LnedQPcRt+JueXMr4rL2fxZx7NXXI8vl/CZk3ZR+XrhLhwUSWlbbzE7RCRpPm0YteSN7OeK4SMN7mdY3/9GwkpMir0X5Nba10R8r+LE3I4HJbTx8kF+nfKn+T9W7XsyZCQ7wbz3pL/Ft8t3BK/S4iCqb7rDfRFfiWG+MwXb82fdH/uv4eEuNOHvZHwlSGwQzGo7r6hQgeYo0J1A/KmgsN2dDk0t+oAfQjNM6o/5TJsEqIFAmrv6v4soD4aq+/IwYL7nGoDTWt62rw/W9yWP5D8EB/2p4QkHaKLi751av3Yph6greu5X9gkDEgnbEgxBsyvxvDQuKih9xt+MoAHSDEER+a72fybTE9lXCNUn+9Oyxx6T4zrUjTnRhtM8RJLaM6aL95bGBJSE0gxqUOejIR7s93CHEg5VlIALRJxk807eRZHuPZJ0akGfzaonfSeILPrvOj0g71LiDvTEcFRkBY23+eNAT5mGz5GLFA9nLAi02sZStGcty5K6+OBGtwySF3Els5adD0irSsYdgjAwHRfGFDyEYs9lTF/ohBuqiMK8I6KAmfdgEn+9KyLSEoH+uoG1c/1Snx0o6zn853OeLyHhNBR0caCkL71DfM9Cil3RW1HI/Mom4av+xvRrfDyPvabrSTbnL3niDnaYfg8ybU1Js+jUnL2B0CoDow7pGPPIWPzXds4gTl2QtoYCOHG494p8VJ8dBuAjqxEwd9tdkaQ2x+GE8KcObde5EDxd9XBg/t6hwgRAPSycTspK1K/4XpSsXGHnN5kJ91OshSeWpoIEgqxhdD6KxZd43WUoQTOU0iI+lLXsXGHGNJGA/Uz4KJ6svHk51QXDfJakaIrcYYaEydeL2C/xI0EGydZlciO0HZf2Tp6H5mf6gTHdgnBor5woC4UbwDz5TLMnw3g90gU4iIXaiHDYuQU+40R+kvGAzgaGcgK0tExWhNxehLCrUbfT8wRtH3Oc36RkFrkBqP9r4o/zS//JMv9gRNnR1VCShekNa1At53IINxsfGq8m5z0ugbi423SfnKLD/MCSjp0XXeXEKClKKCdEkLiNNolWgiodmykEBdjjvi0XGzTUlgtMkhjB211jHYiUX7pYp5/GnR1b2FICKCsK9yWBcpPqhSBU4m5dsEZSV2+cpI9oXk9v0y1K2KT/T3lsPyw2981LRZwouPzYJy+dwmh0wqxILHrJ9D7AyedYj7aXL856iRtsnl0GrulR4tl5xXy9J6pnaeQfMMOqd8xDGykS5a/RfF1O97xXJ0m8rmixa/j8bnNcsLEah3zUxKaRPd1/3T2nc/3mBBu0kTtrS7Zm/vUNrQLsH02NR90sTVWfeBVCMlE9Hr/awh4hiBolxAKF9WxkdzqD/EYv2dQ+NTGJM9W3q2xJiSDQlK2fLOsCPmRJj+EdPJDSJLr8htN4b6viKmq/QAAAABJRU5ErkJggg==";

        System.out.println(essyOcr(images));

    }
}
