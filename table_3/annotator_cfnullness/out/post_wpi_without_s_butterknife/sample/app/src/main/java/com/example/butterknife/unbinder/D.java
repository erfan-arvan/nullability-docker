package com.example.butterknife.unbinder;

import android.support.annotation.ColorInt;
import android.view.View;
import butterknife.BindColor;
import butterknife.ButterKnife;
import android.support.annotation.Nullable;

public class D extends C {

    @BindColor(android.R.color.darker_gray)
    @ColorInt
    int grayColor;

    public D(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
