package com.hultron.toolbartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
  private Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mToolbar.setTitle("Title");
    mToolbar.setSubtitle("SubTitle");
    mToolbar.setLogo(R.mipmap.ic_launcher);

    //添加导航图标，添加菜单点击事件要在setSupportActionBar方法之后
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationIcon(R.drawable.ic_drawer_home);
    mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.action_search:
            Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
            break;
          case R.id.action_notification:
            Toast.makeText(MainActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
            break;
          case R.id.action_settings:
            Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
            break;
          default:
            Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }
}
