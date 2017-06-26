package com.popfu.dailynote.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import de.greenrobot.event.EventBus;

/**
 * Created by pengfu on 26/06/2017.
 */

public class BaseActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
