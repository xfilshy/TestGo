package com.xue.oss;

import android.text.TextUtils;

import com.xue.parsers.MasterParser;

import org.json.JSONObject;

public class SignConentParser extends MasterParser<SignContent> {

    @Override
    public SignContent parse(JSONObject data) throws Exception {
        SignContent signContent = null;
        if (data != null) {
            String sign = optString(data, "sign");
            if (!TextUtils.isEmpty(sign)) {
                signContent = new SignContent();
                signContent.setSign(sign);
            }
        }
        return signContent;
    }
}
