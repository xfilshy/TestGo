package com.elianshang.tools;

public class TextTool {

    public static int getFullWidth(String text, boolean isTrim) {
        if (text == null || text.length() == 0) {
            return 0;
        }

        if (isTrim) {
            text = text.trim();
        }

        return text.getBytes().length / 3;
    }

    public static String getFullWidthText(String text, int maxLen, boolean isTrim) {
        if (maxLen == 0) {
            return "";
        }

        int len = getFullWidth(text, isTrim);
        if (getFullWidth(text, isTrim) <= maxLen) {
            return text;
        }

        if ((len - maxLen) > len / 2) {
            char[] chars = text.toCharArray();
            String tmpString = "";
            int tmpLen = 0;
            int index = 0;

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
