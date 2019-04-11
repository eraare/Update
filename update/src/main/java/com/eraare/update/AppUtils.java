package com.eraare.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @author Leo
 * @version 1
 * @since 2019-04-07
 * 应用程序工具类
 */
public final class AppUtils {
    private AppUtils() {
    }

    /**
     * 获取应用包信息
     *
     * @param context Context
     * @return PackageInfo
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            return manager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地版本号
     *
     * @param context Context
     * @return long
     */
    public static long getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packageInfo.getLongVersionCode();
            } else {
                return packageInfo.versionCode;
            }
        }
        return 0;
    }

    /**
     * 获取本地版本名
     *
     * @param context Context
     * @return String
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return null;
    }

    /**
     * 安装APK文件
     *
     * @param context  Context
     * @param pathname String
     */
    public static void installApk(Context context, String pathname) {
        File file = new File(pathname);
        if (!file.exists()) return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            String authority = context.getPackageName() + ".fileprovider";
            uri = FileProvider.getUriForFile(context, authority, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
