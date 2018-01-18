package com.elianshang.tools;

import android.database.Cursor;

/**
 * Created by xfilshy on 15/12/2.
 */
public class CursorTool {

    public static void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
    }
}
