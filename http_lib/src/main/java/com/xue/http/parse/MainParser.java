package com.xue.http.parse;

import android.text.TextUtils;

import com.xue.http.hook.BaseBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主解析器，封装部分解析方法(面向JSON 解析)
 */
public abstract class MainParser<T extends BaseBean, D> extends BaseParser<T, D> {

    protected boolean has(JSONObject jsonObject, String name) {
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.has(name);
    }

    protected boolean has(JSONArray jsonArray, int index) {
        if (index < 0) {
            return false;
        }
        return getLength(jsonArray) >= index + 1;
    }

    protected int getLength(JSONArray jsonArray) {
        if (jsonArray == null) {
            return 0;
        }

        return jsonArray.length();
    }

    protected int optInt(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        int value = -1;

        if (has(jsonObject, name)) {
            String valueString = optString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Integer.parseInt(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected int getInt(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        int value = -1;

        String valueString = getString(jsonObject, name);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected int optInt(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        int value = -1;
        if (has(jsonArray, index)) {
            String valueString = optString(jsonArray, index);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Integer.parseInt(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected int getInt(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        int value = -1;
        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected long optLong(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        long value = -1;

        if (has(jsonObject, name)) {
            String valueString = optString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Long.parseLong(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected long getLong(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        long value = -1;

        String valueString = getString(jsonObject, name);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Long.parseLong(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected long optLong(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        long value = -1;

        if (has(jsonArray, index)) {
            String valueString = optString(jsonArray, index);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Long.parseLong(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected long getLong(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        long value = -1;

        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Long.parseLong(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected boolean optBoolean(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        boolean value = false;

        if (has(jsonObject, name)) {
            value = jsonObject.optBoolean(name);
        }

        return value;
    }

    protected boolean getBoolean(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        boolean value = false;

        value = jsonObject.getBoolean(name);

        return value;
    }

    protected boolean optBoolean(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        boolean value = false;

        if (has(jsonArray, index)) {
            value = jsonArray.optBoolean(index);
        }

        return value;
    }

    protected boolean getBoolean(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        boolean value = false;

        value = jsonArray.getBoolean(index);

        return value;
    }

    protected float optFloat(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        float value = -1;

        if (has(jsonObject, name)) {
            String valueString = optString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Float.parseFloat(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected float getFloat(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        float value = -1;

        String valueString = getString(jsonObject, name);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Float.parseFloat(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected float optFloat(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        float value = -1;
        if (has(jsonArray, index)) {
            String valueString = optString(jsonArray, index);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Float.parseFloat(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected float getFloat(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        float value = -1;
        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Float.parseFloat(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }
    protected double optDouble(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        double value = -1;

        if (has(jsonObject, name)) {
            String valueString = optString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Double.parseDouble(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected double getDouble(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        double value = -1;

        String valueString = getString(jsonObject, name);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Double.parseDouble(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected double optDouble(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        double value = -1;
        if (has(jsonArray, index)) {
            String valueString = optString(jsonArray, index);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Double.parseDouble(valueString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    protected double getDouble(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        double value = -1;
        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Double.parseDouble(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    protected String optString(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        String value = "";

        if (!jsonObject.has(name)) {
            return value;
        }

        value = jsonObject.optString(name);

        if ("null".equalsIgnoreCase(value)) {
            value = "";
        }

        return value;
    }

    protected String getString(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONArray is null");
        }

        String value = "";

        value = jsonObject.getString(name);

        if ("null".equalsIgnoreCase(value)) {
            value = "";
        }

        return value;
    }

    protected String optString(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        String value = "";

        if (has(jsonArray, index)) {
            value = jsonArray.optString(index);

            if ("null".equalsIgnoreCase(value)) {
                value = "";
            }
        }

        return value;
    }

    protected String getString(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        String value = "";

        value = jsonArray.getString(index);

        if ("null".equalsIgnoreCase(value)) {
            value = "";
        }

        return value;
    }

    protected JSONArray optJSONArray(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONObject is null");
        }
        JSONArray array = null;
        if (has(jsonObject, name)) {
            array = jsonObject.optJSONArray(name);
        }

        return array;
    }

    protected JSONArray getJSONArray(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONObject is null");
        }
        JSONArray array = jsonObject.getJSONArray(name);

        return array;
    }

    protected JSONArray optJSONArray(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        JSONArray array = null;
        if (has(jsonArray, index)) {
            array = jsonArray.optJSONArray(index);
        }

        return array;
    }

    protected JSONArray getJSONArray(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        JSONArray array = jsonArray.getJSONArray(index);

        return array;
    }

    protected JSONObject optJSONObject(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONObject is null");
        }
        JSONObject object = null;
        if (has(jsonObject, name)) {
            object = jsonObject.optJSONObject(name);
        }

        return object;
    }

    protected JSONObject getJSONObject(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONObject is null");
        }
        JSONObject object = jsonObject.getJSONObject(name);

        return object;
    }

    protected JSONObject optJSONObject(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        JSONObject object = null;
        if (has(jsonArray, index)) {
            object = jsonArray.optJSONObject(index);
        }

        return object;
    }

    protected JSONObject getJSONObject(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        JSONObject object = jsonArray.getJSONObject(index);

        return object;
    }
}
