package com.eraare.update;

public class DownloaderInfo {
    public String title;
    public String description;

    public DownloaderInfo() {
        this.title = "版本更新";
        this.description = "新版本下载中...";
    }

    public DownloaderInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
