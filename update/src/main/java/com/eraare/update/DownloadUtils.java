package com.eraare.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public final class DownloadUtils {
    private DownloadUtils() {
    }

    public static void downloadByBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static long downloadInApplication(Context context, String url, String title, String description) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(description);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationUri(getUri(context));

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }

    private static Uri getUri(Context context) {
        String cachePath = FileUtils.getCachePath(context);
        String pathname = cachePath + "/download" + "/temp.apk";
        File file = new File(pathname);
        if (file.exists()) {
            boolean delete = file.delete();
        }
        return Uri.fromFile(file);
    }
}
