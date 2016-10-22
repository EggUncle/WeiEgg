package com.app.egguncle.weiegg.entities;

import android.util.Log;

import com.app.egguncle.weiegg.utils.LogUtils;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 用于获取和使用用户的UID
 * Created by egguncle on 16.10.16.
 */
public class Uid {
    private static long lUid;
    private static Uid intance;

    private Uid() {

    }

    public long getlUid() {
        Log.e("MY_TAG","========4444============= "+lUid);
        if (intance == null) {
            return 0;
        }
        return lUid;
    }

    public void setlUid(long uid) {
        if (intance != null) {
            this.lUid = uid;
        }
    }

    public static Uid getIntance() {
        if (intance == null) {
            intance = new Uid();
        }
        return intance;
    }
}
