package com.eraare.update;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public final class FileUtils {
    public static String getCachePath(Context context) {
        if (TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                return file.getPath();
            }
        }
        return context.getCacheDir().getPath();
    }
}
