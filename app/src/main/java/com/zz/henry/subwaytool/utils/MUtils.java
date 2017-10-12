package com.zz.henry.subwaytool.utils;

import android.content.Context;
import com.zz.henry.subwaytool.R;
import com.zz.henry.subwaytool.common.MConfig;
import com.zz.henry.subwaytool.common.MContextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;

/**
 * Created by henry on 15/6/9.
 */
public class MUtils {

    public static MConfig mConfig = null;

    static {
        if (null == mConfig) {
            init();
        }
    }

    public static synchronized void init() {

        if (mConfig == null) {
            // TBD
            JSONObject configJson = fileToJson(R.raw.mconfig);
            if (configJson == null) {
                System.out.println("load json config error");
            } else {
                try {
                    mConfig = new MConfig();
                    mConfig.setAppName(configJson.getString("appName"));
                    mConfig.setServerAddr(configJson.getString("serverAddr"));
                    mConfig.setServerPort(configJson.getInt("serverPort"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONObject fileToJson(int sourceFile) {

        try {
            Context context = MContextUtils.getInstance();
            InputStream is = context.getResources().openRawResource(sourceFile);

            byte[] buffer = null;

            try {
                buffer = new byte[is.available()];
                is.read(buffer);
            } catch (Exception e) {
                System.out.println("load resources from file error");
                return null;
            }

            try {
                String jsonString = new String(buffer, "utf-8");
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject;
            } catch (Exception e) {
                System.out.println("not json context");
                return null;
            }

        } catch (Exception e) {
            System.out.println("handle context error");
            return null;
        }
    }

    public static String getFullUrl(String subUrl) {
        return getServerUrl() + subUrl;
    }

    public static String getServerUrl() {
        return "http://" + mConfig.getServerAddr() + ":" + mConfig.getServerPort() + "/";
    }
}
