package com.eraare.update;

public class DialogInfo {
    public String title;
    public String message;
    public String negativeText;
    public String positiveText;

    public DialogInfo() {
        title = "版本更新";
        message = "发现新版本，是否需要更新？";
        negativeText = "稍后再说";
        positiveText = "立即下载";
    }

    public DialogInfo(String title, String message, String negativeText, String positiveText) {
        this.title = title;
        this.message = message;
        this.negativeText = negativeText;
        this.positiveText = positiveText;
    }
}
