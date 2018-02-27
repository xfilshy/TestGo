package com.elianshang.tools;

public class TextTool {

    /**
     * 计算文本全角字符长度
     */
    public static int getFullWidth(String text, boolean isTrim) {
        if (text == null || text.length() == 0) {
            return 0;
        }

        if (isTrim) {
            text = text.trim();
        }

        return text.getBytes().length / 3;
    }

    /**
     * 截取文本全角字符
     */
    public static String getFullWidthText(String text, int maxLen, boolean isTrim) {
        if (maxLen == 0) {
            return "";
        }

        int len = getFullWidth(text, isTrim);
        if (len <= maxLen) {
            return text;
        }

        if ((len - maxLen) > len / 2) {
            char[] chars = text.toCharArray();
            int index = maxLen;
            String tmpString = text.toString().substring(0, index);
            int tmpLen = tmpString.getBytes().length;

            while (tmpLen <= maxLen * 3) {
                tmpString += chars[index];
                tmpLen = tmpString.getBytes().length;
                index++;
            }

            return tmpString.substring(0, tmpString.length() - 1);
        } else {
            int tmpLen = len;
            String tmpString = text;

            while (tmpLen > maxLen) {
                tmpString = tmpString.substring(0, tmpString.length() - ((tmpLen - maxLen) / 3 == 0 ? 1 : (tmpLen - maxLen) / 3));
                tmpLen = getFullWidth(tmpString, isTrim);
            }

            return tmpString;
        }
    }
}
