package com.elianshang.tools;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpTool {


    /**
     * 判定输入汉字
     *
     * @param text
     * @return
     */
    public static boolean isChinese(String text) {
        if (!TextUtils.isEmpty(text)) {
            return false;
        }

        for (char c : text.toCharArray()) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 匹配手机号
     *
     * @param input
     * @return
     */
    public static boolean matchPhoneNum(String input) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    /**
     * 匹配密码
     *
     * @param input
     * @return
     */
    public static boolean matchPwd(String input) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    /**
     * 匹配验证码
     *
     * @param input
     * @return
     */
    public static boolean matchVerifyCode(String input) {
        Pattern pattern = Pattern.compile("^\\d{6}$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    public static String findVerifyCode(String input) {

        Pattern pattern = Pattern.compile("链商");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            pattern = Pattern.compile("验证码");
            matcher = pattern.matcher(input);
            if (matcher.find()) {
                pattern = Pattern.compile("\\d{6}");
                matcher = pattern.matcher(input);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }
        return "";
    }
}
