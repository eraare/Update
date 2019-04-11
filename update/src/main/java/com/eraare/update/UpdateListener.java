package com.eraare.update;

import java.util.Map;

public interface UpdateListener {
    void onCheckFailure(int errorCode);

    boolean onCheckSuccess(long versionCode, String versionName, Map<String, Object> map);

    void onNegativeButtonClick(int which, Map<String, Object> map);

    void onPositiveButtonClick(int which, Map<String, Object> map);

    String getDownloadUrl(Map<String, Object> map);

    DialogInfo onConfigDialog(Map<String, Object> map);

    DownloaderInfo onConfigDownloader(Map<String, Object> map);
}
