package com.xue.oss;

import android.text.TextUtils;

import com.xue.parsers.MasterParser;

import org.json.JSONObject;

public class OssConfigParser extends MasterParser<OssConfig> {

    @Override
    public OssConfig parse(JSONObject data) throws Exception {

        OssConfig ossConfig = null;
        if (data != null) {
            data = optJSONObject(data, "oss");
            if (data != null) {
                String endPoint = optString(data, "endpoint");
                String bucket = optString(data, "bucket");
                String uploadPath = optString(data, "upload_path");

                if (!TextUtils.isEmpty(endPoint) && !TextUtils.isEmpty(bucket)) {
                    ossConfig = new OssConfig();
                    ossConfig.setEndPoint(endPoint);
                    ossConfig.setBucketName(bucket);
                    ossConfig.setUploadPath(uploadPath);
                }
            }
        }
        return ossConfig;
    }
}
