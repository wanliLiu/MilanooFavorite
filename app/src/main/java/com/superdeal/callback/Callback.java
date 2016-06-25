package com.superdeal.callback;

/**
 * 所有的接口定义
 *
 * @author milanoouser
 */
public class Callback {

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    /**
     * 获取数据有问题或是网络没有的时候回掉
     *
     * @author milanoouser
     */
    public static interface RetryListener {

        public void onRetry();
    }
}
