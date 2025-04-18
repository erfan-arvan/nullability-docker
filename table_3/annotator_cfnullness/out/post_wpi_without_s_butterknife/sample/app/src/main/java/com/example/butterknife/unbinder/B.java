package com.example.butterknife.unbinder;

import android.support.annotation.ColorInt;
import android.view.View;
import butterknife.BindColor;
import butterknife.ButterKnife;
import android.support.annotation.Nullable;

public class B extends A {

    @BindColor(android.R.color.white)
    @ColorInt
    int whiteColor;

    public B(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
