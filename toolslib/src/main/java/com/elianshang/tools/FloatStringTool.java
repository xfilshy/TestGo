package com.elianshang.tools;

import java.text.DecimalFormat;

public class FloatStringTool {

    /**
     * 保留两位小数
     */
    public static String twoDecimalPlaces(float fl) {
        DecimalFormat fnum = new DecimalFormat("0.00");
        String dd = fnum.format(fl);

        return dd;
    }

    /**
     * 三位小数
     * @param fl
     * @return
     */
    public static String threeDecimalPlaces(float fl) {
        DecimalFormat fnum = new DecimalFormat("0.000");
        String dd = fnum.format(fl);

        return dd;
    }
    /**
     * 整数float去小数点及0
     * eg：1.0 -> 1
     * @param fl
     * @return
     */
    public static String integerDecimalPlace(float fl) {
        return fl % 1 == 0 ? "" + (int) fl : "" + fl;
    }
}
