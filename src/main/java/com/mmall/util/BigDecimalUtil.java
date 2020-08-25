package com.mmall.util;

import java.math.BigDecimal;

/**
 * Created By Zzyy
 **/
public class BigDecimalUtil {

    private BigDecimalUtil() {

    }

    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位小数
    }

    public static void main(String[] args) {
        BigDecimal totalMount = new BigDecimal("3299.000");
        BigDecimal totalMount2 = new BigDecimal("3299.00");
        System.out.println(totalMount.compareTo(totalMount2));

        BigDecimal totalMount3 = new BigDecimal("3299.000");
        BigDecimal totalMount4 = new BigDecimal("3299.00");
        System.out.println(totalMount3.equals(totalMount4));

        BigDecimal totalMount5 = new BigDecimal("3299.00");
        BigDecimal totalMount6 = new BigDecimal("3299.00");
        System.out.println(totalMount5 == totalMount6);
    }
}

