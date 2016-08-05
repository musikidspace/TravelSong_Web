package com.lg.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭流等得工具类
 */
public class MyCloseUtils {
    /**
     * 关闭流等
     * @param closeables 可变参数
     */
    public static void doClose(Closeable... closeables){
        for (Closeable closeable : closeables) {
            if (closeable != null){
                try {
                    closeable.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
    }
}