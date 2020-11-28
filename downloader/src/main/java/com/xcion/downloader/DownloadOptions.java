package com.xcion.downloader;

import android.os.Environment;

/**
 * @Author: Kern Hu
 * @E-mail: sky580@126.com
 * @CreateDate: 2020/10/10 15:13
 * @UpdateUser: Kern Hu
 * @UpdateDate: 2020/10/10 15:13
 * @Version: 1.0
 * @Description: java类作用描述
 * @UpdateRemark: 更新说明
 */
public class DownloadOptions {

    private int connectTimeout = 1000 * 15;
    private int readTimeout = 1000 * 15;
    private int maxConcurrencyCount = 3;
    private int maxThreadCount = 5;
    private String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.xcion.downloader/";
    private String userAgentProperty = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
    private String acceptProperty = "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*";

    public DownloadOptions() {
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxConcurrencyCount() {
        return maxConcurrencyCount;
    }

    public void setMaxConcurrencyCount(int maxConcurrencyCount) {
        this.maxConcurrencyCount = maxConcurrencyCount;
    }

    public int getMaxThreadCount() {
        return maxThreadCount;
    }

    public void setMaxThreadCount(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + storagePath;
    }

    public String getUserAgentProperty() {
        return userAgentProperty;
    }

    public void setUserAgentProperty(String userAgentProperty) {
        this.userAgentProperty = userAgentProperty;
    }

    public String getAcceptProperty() {
        return acceptProperty;
    }

    public void setAcceptProperty(String acceptProperty) {
        this.acceptProperty = acceptProperty;
    }
}
