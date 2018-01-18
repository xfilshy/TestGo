package com.xue.tools;

import com.elianshang.tools.DateTool;

public class TimestampTool {

    /**
     * 毫秒级
     */
    public static long timeOffset = 0;

    public final static long currentTimeMillis() {
        return DateTool.getDateLong() + timeOffset;
    }
}
