package com.hultron.nerdlauncher;

import android.support.v4.app.Fragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    private static final String TAG = "NerdLauncherActivity";
    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
