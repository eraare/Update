package com.eraare.update;

/**
 * @author Leo
 * @version 1
 * @since 2019-04-07
 * 更新配置
 */
public class UpdateConfig {
    public enum DownloadMode {
        DOWNLOAD_BY_BROWSER,
        DOWNLOAD_IN_APPLICATION
    }

    public boolean showDialog; /*是否显示更新对话框*/
    public boolean shouldDownload; /*是否进行下载安装*/
    public DownloadMode downloadMode; /*下载模式*/

    private UpdateConfig(Builder builder) {
        this.showDialog = builder.showDialog;
        this.shouldDownload = builder.shouldDownload;
        this.downloadMode = builder.downloadMode;
    }

    public static class Builder {
        private boolean showDialog;
        private boolean shouldDownload;
        private DownloadMode downloadMode;

        public Builder() {
            this.showDialog = true;
            this.shouldDownload = true;
            this.downloadMode = DownloadMode.DOWNLOAD_BY_BROWSER;
        }

        public Builder showDialog(boolean showDialog) {
            this.showDialog = showDialog;
            return this;
        }

        public Builder shouldDownload(boolean shouldDownload) {
            this.shouldDownload = shouldDownload;
            return this;
        }

        public Builder downloadMode(DownloadMode downloadMode) {
            this.downloadMode = downloadMode;
            return this;
        }

        public UpdateConfig build() {
            return new UpdateConfig(this);
        }
    }

    public static UpdateConfig createDefault() {
        return new UpdateConfig.Builder().build();
    }
}
