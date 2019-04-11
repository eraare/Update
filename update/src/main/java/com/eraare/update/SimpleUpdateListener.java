package com.eraare.update;

import java.util.Map;

public abstract class SimpleUpdateListener implements UpdateListener {
    @Override
    public void onCheckFailure(int errorCode) {

    }

    @Override
    public void onNegativeButtonClick(int which, Map<String, Object> map) {

    }

    @Override
    public void onPositiveButtonClick(int which, Map<String, Object> map) {

    }

    @Override
    public DialogInfo onConfigDialog(Map<String, Object> map) {
        return null;
    }

    @Override
    public DownloaderInfo onConfigDownloader(Map<String, Object> map) {
        return null;
    }
}
