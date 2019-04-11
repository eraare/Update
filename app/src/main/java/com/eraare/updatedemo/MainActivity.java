package com.eraare.updatedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.eraare.update.DialogInfo;
import com.eraare.update.SimpleUpdateListener;
import com.eraare.update.UpdateConfig;
import com.eraare.update.UpdateManager;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new UpdateManager.Builder(this)
                .setUpdateConfig(new UpdateConfig.Builder()
                        .downloadMode(UpdateConfig.DownloadMode.DOWNLOAD_IN_APPLICATION)
                        .build())
                .setUpdateListener(new SimpleUpdateListener() {
                    @Override
                    public boolean onCheckSuccess(long versionCode, String versionName, Map<String, Object> map) {
                        String serverVersionName = map.get("versionName").toString();
                        return !TextUtils.equals(serverVersionName, versionName);
                    }

                    @Override
                    public String getDownloadUrl(Map<String, Object> map) {
                        return map.get("downloadUrl").toString();
                    }

                    @Override
                    public DialogInfo onConfigDialog(Map<String, Object> map) {
//                        return super.onConfigDialog(map);
                        String description = map.get("description").toString();
                        DialogInfo dialogInfo = new DialogInfo();
                        dialogInfo.message = description;
                        return dialogInfo;
                    }
                })
                .build()
                .check("https://lxsc.eraare.com/update.html");
    }
}
