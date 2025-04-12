package android.support.v4.view;

import android.support.annotation.Nullable;

/**
 * STUB! Required for test sources to compile.
 */
public interface ViewPager {

    void addOnPageChangeListener(OnPageChangeListener listener);

    void removeOnPageChangeListener(OnPageChangeListener listener);

    interface OnPageChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
