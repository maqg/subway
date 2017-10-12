package com.zz.henry.subwaytool.network;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class StationSource {
    public final static int LOAD_PROGRESS = 0;
    public final static int LOAD_COMPLETE = 1;

    TextView mTextView = null;
    Long mLoadStart = 0L;
    Long mLoadEndt = 0L;
    String content = null;

    //接受传过来得消息
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_PROGRESS:
                    content = (String) msg.obj;
                    mTextView.setText("参数：" + msg.arg1 + "， 消息休：" + content);
                    break;
                case LOAD_COMPLETE:
                    content = (String) msg.obj;
                    mTextView.setText("Finished: " + content);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public void GetResourceInfo() {
        new Thread() {
            public void run() {
                mLoadStart = System.currentTimeMillis();
                String msgBody = "";

                for (int i = 0; i < 1; i++) {
                    Message msg = new Message();
                    msg.what = LOAD_PROGRESS;
                    msg.arg1 = i + 1;
                    msgBody = getResourcesInfo(i + 1) ;
                    msg.obj = msgBody;

                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {

                    }
                    handler.sendMessage(msg);
                }

                mLoadEndt = System.currentTimeMillis();
                Message msg = new Message();
                msg.what = LOAD_COMPLETE;
                msg.obj = msgBody;
                msg.arg1 = (int) (mLoadEndt - mLoadStart);
                handler.sendMessage(msg);

            }
        }.start();
    }


    public String getResourcesInfo(int paras) {

        String out = null;

        try {

            URL subwayUrl = new URL("http://119.147.91.104:8888/api/subway/resources/info/");

            HttpURLConnection conn = (HttpURLConnection) subwayUrl.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");

            int retCode = conn.getResponseCode();
            if (retCode != 200) {
                out = "bad http reply code " + retCode;
                conn.disconnect();
                return out;
            }

            InputStream stream = conn.getInputStream();
            byte[] data = new byte[102400];
            int length = stream.read(data);

            out = new String(data, 0, length);

            conn.disconnect();
            stream.close();

        } catch (Exception e) {
            out = "do http request error";
            System.out.println("do http request error");
        }

        return out;
    }
}
