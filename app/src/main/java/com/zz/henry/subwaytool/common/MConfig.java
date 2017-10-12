package com.zz.henry.subwaytool.common;

/**
 * Created by henry on 15/6/9.
 */
public class MConfig {

    String appName = "";
    String serverAddr = null;
    int serverPort = 0;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
