package com.eraare.update;

/**
 * @author Leo
 * @version 1
 * @since 2019-04-07
 * 服务器信息
 */
public class ServerInfo {
    public int versionCode; /*版本号*/
    public String versionName; /*版本名*/
    public String description; /*版本描述*/
    public boolean isForce; /*是否强制*/
    public String downloadUrl; /*下载链接*/

    public ServerInfo() {
    }

    public ServerInfo(int versionCode, String versionName, String description, boolean isForce, String downloadUrl) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.description = description;
        this.isForce = isForce;
        this.downloadUrl = downloadUrl;
    }
}
