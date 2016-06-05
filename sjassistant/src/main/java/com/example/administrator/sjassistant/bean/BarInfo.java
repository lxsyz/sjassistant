package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/5/19.
 */
public class BarInfo {


    boolean isDownLoad;// 是否已经下载,这个参数
    // 我暂时没有使用，但是想要将下载系统做的更完善，应该是需要它的，大家可以完善下~嘿嘿
    boolean visible;// bar是否显示
    int currentProgress;// 进度条当前的值
    int maxProgress;// 进度条的最大值


    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }


}
