package net.arvin.socialhelper.entities;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by arvinljw on 17/11/28 17:26
 * Function：
 * Desc：对于qq可自己构造参数
 */
public class ShareEntity {

    /**
     * type 值
     * qq==0
     * qzone==1
     * 微信==2
     * 朋友圈==3
     * 微博==4
     * 发布qq说说==5
     */
    public static final int TYPE_QQ = 0;
    public static final int TYPE_Q_ZONE = 1;
    public static final int TYPE_WX = 2;
    public static final int TYPE_PYQ = 3;
    public static final int TYPE_WB = 4;
    public static final int TYPE_Q_ZONE_PUBLISH=5;

    private int type;
    Bundle params;

    public ShareEntity(int type) {
        this.type = type;
        this.params = new Bundle();
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public int getType() {
        return type;
    }

    protected static void addParams(Bundle params, String key, String value) {
        if (params == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        params.putString(key, value);
    }

    protected static void addParams(Bundle params, String key, int value) {
        if (params == null || TextUtils.isEmpty(key)) {
            return;
        }
        params.putInt(key, value);
    }

    protected static void addParams(Bundle params, String key, ArrayList<String> value) {
        if (params == null || TextUtils.isEmpty(key) || value == null || value.size() == 0) {
            return;
        }
        params.putStringArrayList(key, value);
    }
}
