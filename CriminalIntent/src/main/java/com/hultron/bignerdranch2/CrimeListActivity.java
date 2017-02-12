package com.hultron.bignerdranch2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

  private static final String EXTRA_SUBTITLE_VISIBLE = "crimelist_subtitle_visible";

  public static Intent newIntent(Context packageContext, boolean subtitleVisible) {
    Intent intent = new Intent(packageContext,CrimeListActivity.class);
    intent.putExtra(EXTRA_SUBTITLE_VISIBLE,subtitleVisible);
    return intent;
  }

  @Override
  protected Fragment createFragment() {
    boolean subtitleVisible = getIntent().getBooleanExtra(EXTRA_SUBTITLE_VISIBLE,false);
    return CrimeListFragment.newInstance(subtitleVisible);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_masterdetail;
  }
}
