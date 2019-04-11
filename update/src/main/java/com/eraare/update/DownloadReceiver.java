package com.eraare.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * @author Leo
 * @version 1
 * @since 2019-04-07
 * 下载广播接收器
 */
public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            installApk(context);
        } else if (TextUtils.equals(action, DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            viewDownloads(context);
        }
    }

    /**
     * 查看下载列表
     *
     * @param context 上下文
     */
    private void viewDownloads(Context context) {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 安装APK文件
     *
     * @param context 上下文
     */
    public static void installApk(Context context) {
        String cachePath = FileUtils.getCachePath(context);
        String pathname = cachePath + "/download" + "/temp.apk";
        AppUtils.installApk(context, pathname);
    }
}
