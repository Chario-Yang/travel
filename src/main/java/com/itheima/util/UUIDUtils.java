package com.itheima.util;

import java.util.UUID;

/**
 * 产生UUID随机字符串工具类
 * 生成的结果是：32位的，十六进制随机字符。  16的32次方个
 */
public final class UUIDUtils {
    private UUIDUtils() {
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(UUIDUtils.getUuid());
        System.out.println(UUIDUtils.getUuid());
        System.out.println(UUIDUtils.getUuid());
        System.out.println(UUIDUtils.getUuid());
    }
}
