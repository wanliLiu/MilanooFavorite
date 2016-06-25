package com.superdeal;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.superdeal.R;
import com.superdeal.base.BaseActivity;
import com.superdeal.callback.Callback.NavigationDrawerCallbacks;
import com.superdeal.favorite.DisplayFavoriteActivity;
import com.superdeal.fragment.GridViewFragment;
import com.superdeal.login.UseFacebookLoginActitiy;
import com.superdeal.util.UserUtil;

public class MainActivity extends BaseActivity implements NavigationDrawerCallbacks {

//    private NavigationDrawerFragment mNavigationDrawerFragment;

//    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        //产生debug签名
//		try {
//	         PackageInfo info = getPackageManager().getPackageInfo("com.superdeal", PackageManager.GET_SIGNATURES);
//	         for (Signature signature : info.signatures) {
//                    MessageDigest md = MessageDigest.getInstance("SHA");
//                    md.update(signature.toByteArray());
//                    Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                  }
//            } catch (PackageManager.NameNotFoundException e) {
//            	e.printStackTrace();
//
//            } catch (NoSuchAlgorithmException e) {
//            	e.printStackTrace();
//	        }
    }

    @Override
    protected void getLayoutView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {

//        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

//		mTitle = getTitle();

//		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

//      ForceShowOverflowMenu();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, GridViewFragment.newInstance(getIntent().getStringExtra("listData"))).commit();
    }

    /*
     * force to display overflow menu in action bar
     */
    private void ForceShowOverflowMenu() {

//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.color.theme_default_primary);

        ViewConfiguration viewConfig = ViewConfiguration.get(this);
        try {
            Field overflowMenuField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (null != overflowMenuField) {
                overflowMenuField.setAccessible(true);
                overflowMenuField.setBoolean(viewConfig, false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.container, GridViewFragment.newInstance(position)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_show_favorite) {

            if (UserUtil.getLoginStaus(ctx)) {
                startActivity(new Intent(ctx, DisplayFavoriteActivity.class));
            } else {
                startActivityForResult(new Intent(ctx, UseFacebookLoginActitiy.class), RESULT_action);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg1 == RESULT_OK) {

            if (arg0 == RESULT_action) {
                startActivity(new Intent(ctx, DisplayFavoriteActivity.class));
            }
        }

    }
}
