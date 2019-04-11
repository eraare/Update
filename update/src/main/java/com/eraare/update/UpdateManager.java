package com.eraare.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Leo
 * @version 1
 * @since 2019-04-05
 * 在线更新
 */
public final class UpdateManager {
    private Context context; /*上下文*/
    private UpdateConfig updateConfig; /*更新配置*/
    private UpdateListener updateListener; /*更新事件*/

    private UpdateManager(Builder builder) {
        this.context = builder.context;
        this.updateConfig = builder.updateConfig;
        this.updateListener = builder.updateListener;
    }

    /**
     * 建造者模式
     */
    public static class Builder {
        private Context context;
        private UpdateConfig updateConfig;
        private UpdateListener updateListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setUpdateConfig(UpdateConfig updateConfig) {
            this.updateConfig = updateConfig;
            return this;
        }

        public Builder setUpdateListener(UpdateListener updateListener) {
            this.updateListener = updateListener;
            return this;
        }

        public UpdateManager build() {
            return new UpdateManager(this);
        }
    }

    private Map<String, Object> serverCache = null; /*服务端信息缓存*/

    private static final int WHAT_CHECK_FAILURE = 0x01;
    private static final int WHAT_CHECK_SUCCESS = 0x02;
    private static final int WHAT_SHOW_DIALOG = 0x03;
    public static final int WHAT_NEGATIVE_CLICK = 0x04;
    public static final int WHAT_POSITIVE_CLICK = 0x05;
    private static final int WHAT_SHOULD_DOWNLOAD = 0x06;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CHECK_FAILURE: {
                    updateListener.onCheckFailure(msg.arg1);
                }
                break;
                case WHAT_CHECK_SUCCESS: {
                    long versionCode = AppUtils.getVersionCode(context);
                    String versionName = AppUtils.getVersionName(context);
                    if (updateListener.onCheckSuccess(versionCode, versionName, serverCache)) {
                        if (updateConfig.showDialog) {
                            handler.sendEmptyMessage(WHAT_SHOW_DIALOG);
                        }
                    }
                }
                break;
                case WHAT_SHOW_DIALOG: {
                    showDialog();
                }
                break;
                case WHAT_NEGATIVE_CLICK: {
                    updateListener.onNegativeButtonClick(msg.arg1, serverCache);
                }
                break;
                case WHAT_POSITIVE_CLICK: {
                    updateListener.onPositiveButtonClick(msg.arg1, serverCache);
                    if (updateConfig.shouldDownload) {
                        handler.sendEmptyMessage(WHAT_SHOULD_DOWNLOAD);
                    }
                }
                break;
                case WHAT_SHOULD_DOWNLOAD: {
                    String downloadUrl = updateListener.getDownloadUrl(serverCache);
                    if (updateConfig.downloadMode == UpdateConfig.DownloadMode.DOWNLOAD_BY_BROWSER) {
                        DownloadUtils.downloadByBrowser(context, downloadUrl);
                    } else if (updateConfig.downloadMode == UpdateConfig.DownloadMode.DOWNLOAD_IN_APPLICATION) {
                        DownloaderInfo downloaderInfo = updateListener.onConfigDownloader(serverCache);
                        if (downloaderInfo == null) {
                            downloaderInfo = new DownloaderInfo();
                        }
                        DownloadUtils.downloadInApplication(context, downloadUrl, downloaderInfo.title, downloaderInfo.description);
                    }
                }
                break;
                default:
                    break;
            }
            return true;
        }
    });

    private static final int ERROR_CODE_FAILURE = 10001; /*错误码-failure*/
    private static final int ERROR_CODE_RESPONSE = 10002; /*错误码-response*/
    private static final int ERROR_CODE_BODY = 10003; /*错误码-body*/

    /**
     * @param url 更新检测链接
     */
    public void check(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = WHAT_CHECK_FAILURE;
                msg.arg1 = ERROR_CODE_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) {
                Message msg = Message.obtain();

                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, Object>>() {
                        }.getType();
                        serverCache = gson.fromJson(body.charStream(), type); /*缓存服务端信息*/

                        msg.what = WHAT_CHECK_SUCCESS;
                    } else {
                        msg.what = WHAT_CHECK_FAILURE;
                        msg.arg1 = ERROR_CODE_BODY;
                    }
                } else {
                    msg.what = WHAT_CHECK_FAILURE;
                    msg.arg1 = ERROR_CODE_RESPONSE;
                }

                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 更新提示对话框
     */
    private void showDialog() {
        DialogInfo dialogInfo = updateListener.onConfigDialog(serverCache);
        if (dialogInfo == null) {
            dialogInfo = new DialogInfo();
        }

        new AlertDialog.Builder(context)
                .setTitle(dialogInfo.title)
                .setMessage(dialogInfo.message)
                .setCancelable(false)
                .setNegativeButton(dialogInfo.negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = Message.obtain();
                        msg.what = WHAT_NEGATIVE_CLICK;
                        msg.arg1 = which;
                        handler.sendMessage(msg);
                    }
                })
                .setPositiveButton(dialogInfo.positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = Message.obtain();
                        msg.what = WHAT_POSITIVE_CLICK;
                        msg.arg1 = which;
                        handler.sendMessage(msg);
                    }
                })
                .create()
                .show();
    }
}
