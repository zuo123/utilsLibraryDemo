package com.cnbs.utilslibrary.viewUtils.toast;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/22.
 */

public class CenterHintToast extends Toast {

    public CenterHintToast(Context context, String hintMsg) {
        super(context);
        if (TextUtils.isEmpty(hintMsg))return;      //加上非空判断
        Toast toast = Toast.makeText(context, hintMsg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
